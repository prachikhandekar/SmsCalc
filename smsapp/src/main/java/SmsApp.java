import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import static spark.Spark.*;

public class SmsApp {

    public static String expressionEval(String expr) {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine engine = m.getEngineByName("JavaScript");
        try {
            return engine.eval(expr).toString();
        }
        catch (ScriptException e){
            return "Invalid expression";
        }
    }

    public static void main(String[] args) {

        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            String text = req.raw().getParameter("Body");
            String message = expressionEval(text);

            res.type("application/xml");
            Body body = new Body
                    .Builder(message)
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            return twiml.toXml();
        });

    }
}

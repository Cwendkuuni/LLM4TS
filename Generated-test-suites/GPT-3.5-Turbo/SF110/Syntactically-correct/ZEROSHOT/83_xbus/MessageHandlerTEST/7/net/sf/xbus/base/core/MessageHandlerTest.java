import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        // Setup
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Execution
        String result = messageHandler.getMessage(key, params);

        // Verification
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetMessageOptional() {
        // Setup
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Execution
        String result = messageHandler.getMessageOptional(key, params);

        // Verification
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetInstance() {
        // Setup
        String basename = "basename";

        // Execution
        MessageHandler result = MessageHandler.getInstance(basename);

        // Verification
        Assert.assertNotNull(result);
    }
}
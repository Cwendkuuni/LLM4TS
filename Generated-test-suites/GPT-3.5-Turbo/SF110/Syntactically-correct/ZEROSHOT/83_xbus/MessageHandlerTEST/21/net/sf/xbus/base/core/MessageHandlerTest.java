import org.junit.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage(key, params);
        
        // Assert statements to check the result
        Assert.assertNotNull(message);
        Assert.assertEquals("Expected message", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessageOptional(key, params);
        
        // Assert statements to check the result
        Assert.assertNotNull(message);
        Assert.assertEquals("Expected message", message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "testBasename";
        
        MessageHandler messageHandler = MessageHandler.getInstance(basename);
        
        // Assert statements to check the result
        Assert.assertNotNull(messageHandler);
        // You can add more assertions to check the instance properties if needed
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "testBasename";
        String dir = "testDir";
        
        messageHandler.addMessages(basename, dir);
        
        // Assert statements to check the result
        // You can add assertions to validate the added messages in mMessages if needed
    }
}
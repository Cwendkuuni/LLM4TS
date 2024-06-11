import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessage(key, params);
        assertNotNull(message);
        assertEquals("Key: testKey not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessageOptional(key, params);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("testBasename");
        assertNotNull(messageHandler);
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}
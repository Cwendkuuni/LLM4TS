import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // Typical use case
        MessageHandler messageHandler = new MessageHandler("test");
        List<String> params = new ArrayList<>();
        params.add("parameter1");
        String result = messageHandler.getMessage("key1", params);
        assertEquals("Message text1 with parameter1", result);
        
        // Edge case: key not found
        MessageHandler messageHandler2 = new MessageHandler("test");
        List<String> params2 = new ArrayList<>();
        params2.add("parameter2");
        String result2 = messageHandler2.getMessage("key2", params2);
        assertEquals("Key: key2 not found in message file", result2);
    }
    
    @Test
    public void testGetMessageOptional() {
        // Typical use case
        MessageHandler messageHandler = new MessageHandler("test");
        List<String> params = new ArrayList<>();
        params.add("parameter1");
        String result = messageHandler.getMessageOptional("key1", params);
        assertEquals("Message text1 with parameter1", result);
        
        // Edge case: key not found
        MessageHandler messageHandler2 = new MessageHandler("test");
        List<String> params2 = new ArrayList<>();
        params2.add("parameter2");
        String result2 = messageHandler2.getMessageOptional("key2", params2);
        assertNull(result2);
        
        // Edge case: null parameters
        MessageHandler messageHandler3 = new MessageHandler("test");
        String result3 = messageHandler3.getMessageOptional("key1", null);
        assertEquals("Message text1 with $1$", result3);
    }
    
    @Test
    public void testGetInstance() {
        // Typical use case
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNotNull(messageHandler);
        
        // Edge case: same instance for the same basename
        MessageHandler messageHandler2 = MessageHandler.getInstance("test");
        assertSame(messageHandler, messageHandler2);
    }
    
    @Test
    public void testAddMessages() {
        // Typical use case
        MessageHandler messageHandler = new MessageHandler("test");
        messageHandler.addMessages("basename", "dir");
        String result = messageHandler.getMessageOptional("key", null);
        assertEquals("Message text", result);
        
        // Error scenario: message file not found
        MessageHandler messageHandler2 = new MessageHandler("test");
        messageHandler2.addMessages("basename", "nonexistent_dir");
        String result2 = messageHandler2.getMessageOptional("key", null);
        assertNull(result2);
    }
}
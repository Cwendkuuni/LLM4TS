import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler("test");
        List<String> params = new ArrayList<>();
        
        // Typical use case
        String message = messageHandler.getMessage("key1", params);
        assertEquals("Key: key1 not found in message file", message);
        
        // Error scenario: null key
        message = messageHandler.getMessage(null, params);
        assertEquals("Key: null not found in message file", message);
        
        // Error scenario: null params
        message = messageHandler.getMessage("key1", null);
        assertEquals("Key: key1 not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler("test");
        List<String> params = new ArrayList<>();
        
        // Typical use case
        String message = messageHandler.getMessageOptional("key2", params);
        assertEquals("Message 2", message);
        
        // Edge case: empty params
        message = messageHandler.getMessageOptional("key2", new ArrayList<>());
        assertEquals("Message 2", message);
        
        // Error scenario: null key
        message = messageHandler.getMessageOptional(null, params);
        assertEquals(null, message);
        
        // Error scenario: null params
        message = messageHandler.getMessageOptional("key2", null);
        assertEquals(null, message);
        
        // Error scenario: key not found
        message = messageHandler.getMessageOptional("key5", params);
        assertEquals(null, message);
    }
    
    @Test
    public void testGetInstance() {
        // Typical use case
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNotNull(messageHandler);
        
        // Error scenario: null basename
        messageHandler = MessageHandler.getInstance(null);
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("test");
        
        // Typical use case
        messageHandler.addMessages("test", "/path/to/dir");
        assertNotNull(messageHandler.mMessages);
        assertEquals(2, messageHandler.mMessages.size());
        
        // Error scenario: null basename
        messageHandler.addMessages(null, "/path/to/dir");
        assertNotNull(messageHandler.mMessages);
        assertEquals(2, messageHandler.mMessages.size());
        
        // Error scenario: null dir
        messageHandler.addMessages("test", null);
        assertNotNull(messageHandler.mMessages);
        assertEquals(2, messageHandler.mMessages.size());
    }
}
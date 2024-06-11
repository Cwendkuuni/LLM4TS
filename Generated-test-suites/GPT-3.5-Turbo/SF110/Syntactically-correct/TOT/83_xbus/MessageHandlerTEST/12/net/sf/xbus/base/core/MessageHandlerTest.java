import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }
    
    @Test
    public void testGetMessage() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage(key, params);
        
        assertEquals("Existing key with params should return the message", "Test message with param1 and param2", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Existing key with one param should return the message", "Test message with param1", message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        String key = "existingKey";
        List<String> params = null;
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Existing key with null params should return the message", "Test message without any params", message);
    }
    
    @Test
    public void testGetMessageOptional_NonExistingKey() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNull("Non-existing key should return null", message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "test";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull("Instance should not be null", instance);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "test";
        String dir = "messages/";
        
        messageHandler.addMessages(basename, dir);
        
        assertNotNull("Messages should not be null", messageHandler.mMessages);
        assertFalse("Messages should not be empty", messageHandler.mMessages.isEmpty());
    }
}
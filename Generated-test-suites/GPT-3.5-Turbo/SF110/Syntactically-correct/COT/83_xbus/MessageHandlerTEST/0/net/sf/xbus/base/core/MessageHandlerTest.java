
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    private static MessageHandler messageHandler;
    
    @BeforeClass
    public static void setUp() {
        messageHandler = MessageHandler.getInstance("basename");
    }
    
    @Test
    public void testGetMessage() {
        String key = "key";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage(key, params);
        
        assertNotNull(message);
        assertEquals("Expected message is not received", "Expected message for key", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        String key = "key";
        List<String> params = new ArrayList<String>();
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNull("Message is not optional", message);
    }
    
    @Test
    public void testGetMessageOptionalWithParams() {
        String key = "key";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNotNull(message);
        assertEquals("Expected message is not received", "Expected message for key", message);
    }
    
    @Test
    public void testGetMessageOptionalWithNullParams() {
        String key = "key";
        List<String> params = null;
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNotNull(message);
        assertEquals("Expected message is not received", "Expected message without parameters", message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "basename";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull("Instance is not created", instance);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "basename";
        String dir = "directory";
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals("Messages are not added", 10, messageHandler.getMessages().size());
    }
    
}

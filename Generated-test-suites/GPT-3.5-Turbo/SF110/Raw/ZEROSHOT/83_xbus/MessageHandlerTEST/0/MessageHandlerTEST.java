import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance("test");
    }
    
    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage("test.key", params);
        
        assertEquals("Expected message not returned", "This is a test message with param1 and param2", message);
    }
    
    @Test
    public void testGetMessageNotFound() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage("non.existing.key", params);
        
        assertEquals("Expected message not returned", "Key: non.existing.key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessageOptional("test.key", params);
        
        assertEquals("Expected message not returned", "This is a test message with param1 and param2", message);
    }
    
    @Test
    public void testGetMessageOptionalNotFound() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessageOptional("non.existing.key", params);
        
        assertNull("Message exists for non-existing key", message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("test");
        
        assertNotNull("Instance is null", instance);
    }
    
    @Test
    public void testAddMessages() {
        messageHandler.addMessages("test", Constants.XBUS_ETC);
        
        assertNotNull("Messages are not added properly", messageHandler.getMessages());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMessagesInvalidDir() {
        messageHandler.addMessages("test", "non.existing.dir");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMessagesInvalidFile() {
        messageHandler.addMessages("test", Constants.XBUS_ETC + "/non.existing.file");
    }
}
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        // Test case for getting message with existing key
        MessageHandler handler = new MessageHandler("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = handler.getMessage("key1", params);
        String expected = "Message with key1 and param1";
        Assert.assertEquals(expected, message);
        
        // Test case for getting message with non-existing key
        message = handler.getMessage("key2", params);
        expected = "Key: key2 not found in message file";
        Assert.assertEquals(expected, message);
    }
    
    @Test
    public void testGetMessageOptional() {
        // Test case for getting message optional with existing key
        MessageHandler handler = new MessageHandler("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = handler.getMessageOptional("key1", params);
        String expected = "Message with key1 and param1";
        Assert.assertEquals(expected, message);
        
        // Test case for getting message optional with non-existing key
        message = handler.getMessageOptional("key2", params);
        Assert.assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        // Test case for getting instance with existing basename
        MessageHandler handler = MessageHandler.getInstance("test");
        Assert.assertNotNull(handler);
        
        // Test case for getting instance with non-existing basename
        handler = MessageHandler.getInstance("non-existing");
        Assert.assertNotNull(handler);
    }
    
    @Test
    public void testAddMessages() {
        // Test case for adding messages with existing basename and directory
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", "directory");
        // Assert method functionality
        
        // Test case for adding messages with non-existing basename and directory
        handler.addMessages("non-existing", "non-existing");
        // Assert method functionality
    }
}
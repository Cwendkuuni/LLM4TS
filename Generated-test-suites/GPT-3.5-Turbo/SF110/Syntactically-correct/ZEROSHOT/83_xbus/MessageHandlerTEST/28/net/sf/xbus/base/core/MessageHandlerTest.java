import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler("example");
        String message = messageHandler.getMessage("key1", null);
        assertNotNull(message);
        assertEquals("Key: key1 not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler("example");
        String message = messageHandler.getMessageOptional("key2", null);
        assertNull(message);
        
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        message = messageHandler.getMessageOptional("key3", params);
        assertNotNull(message);
        assertEquals("This is a message with parameters: $1$ and $2$", message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("example");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("example");
        
        // Test that messages are added from Constants.XBUS_ETC directory
        assertNotNull(messageHandler.mMessages.get("key1"));
        assertNotNull(messageHandler.mMessages.get("key2"));
        
        // Test that messages are added from Constants.XBUS_PLUGIN_ETC directory
        assertNotNull(messageHandler.mMessages.get("key3"));
        assertNotNull(messageHandler.mMessages.get("key4"));
    }
}
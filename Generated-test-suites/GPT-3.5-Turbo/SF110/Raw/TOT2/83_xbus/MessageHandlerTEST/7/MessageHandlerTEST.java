import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage("testKey", params);
        
        assertNotNull(message);
        assertEquals("Key: testKey not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        
        List<String> params = new ArrayList<>();
        params.add("param1");
        
        String message = messageHandler.getMessageOptional("testKey2", params);
        
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        
        assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        
        // Test case scenarios for adding messages
        // Add messages for a specific basename and directory
        // Add messages for a different locale (edge case)
        
        assertEquals(1, messageHandler.mMessages.size());
    }

}
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // typical use case
        MessageHandler handler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = handler.getMessage("key1", params);
        assertEquals("Expected message", message, "Message for key1 with param1");

        // edge case - key not found
        message = handler.getMessage("nonExistentKey", null);
        assertEquals("Expected message", message, "Key: nonExistentKey not found in message file");
    }

    @Test
    public void testGetMessageOptional() {
        // typical use case
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessageOptional("key2", null);
        assertEquals("Expected message", message, "Message for key2");

        // edge case - null parameters
        message = handler.getMessageOptional("key3", null);
        assertEquals("Expected message", message, "Message for key3");

        // exception handling scenario - empty message map
        handler.mMessages = new Hashtable<String, String>();
        message = handler.getMessageOptional("key4", null);
        assertNull("Expected null message", message);
    }

    @Test
    public void testGetInstance() {
        // typical use case
        MessageHandler handler = MessageHandler.getInstance("test");
        assertNotNull("Instance should not be null", handler);

        // edge case - getInstance twice with same basename
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertSame("Instances should be the same", handler, handler2);
    }

    @Test
    public void testAddMessages() {
        // typical use case
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "testDir");

        // edge case - exception handling scenario for file not found
        try {
            handler.addMessages("test", "nonExistentDir");
            fail("Expected exception for non-existent directory");
        } catch (Exception e) {
            // expected exception
        }
    }

}
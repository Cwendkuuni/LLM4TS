
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "test.key";
        List<String> params = new ArrayList<>();
        assertEquals("Key: test.key not found in message file", messageHandler.getMessage(key, params));
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "test.key";
        List<String> params = new ArrayList<>();
        assertNull(messageHandler.getMessageOptional(key, params));
        
        key = "existing.key";
        String message = "This is an existing key";
        messageHandler.mMessages.put(key, message);
        assertEquals(message, messageHandler.getMessageOptional(key, params));
    }

    @Test
    public void testGetInstance() {
        String basename = "basename";
        assertNotNull(MessageHandler.getInstance(basename));
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String dir = "directory";
        String basename = "basename";
        messageHandler.addMessages(basename, dir);
        // Verify that messages are properly added to mMessages
    }
}
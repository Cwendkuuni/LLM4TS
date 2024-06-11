
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessage("key1", params);
        assertNotNull(message);
        assertEquals("Expected message1", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("key1", params);
        assertNotNull(message);
        assertEquals("Expected message1", message);
    }

    @Test
    public void testGetMessageNotFound() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessage("unknown", params);
        assertNotNull(message);
        assertEquals("Key: unknown not found in message file", message);
    }

    @Test
    public void testGetMessageWithNullParam() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add(null);
        String message = messageHandler.getMessage("key1", params);
        assertNotNull(message);
        assertEquals("Expected message1", message);
    }

    @Test
    public void testGetMessageOptionalWithNullParam() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add(null);
        String message = messageHandler.getMessageOptional("key1", params);
        assertNotNull(message);
        assertEquals("Expected message1", message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("basename1");
        assertNotNull(messageHandler1);
        MessageHandler messageHandler2 = MessageHandler.getInstance("basename1");
        assertSame(messageHandler1, messageHandler2);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "basename";
        String dir = "dir";
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.mMessages);
    }
}
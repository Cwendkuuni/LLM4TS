import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessage(key, params);
        assertEquals("Key: testKey not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessageOptional(key, params);
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler handler = new MessageHandler();
        String basename = "testBaseName";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler();
        String basename = "testBaseName";
        String dir = "testDir/";
        handler.addMessages(basename, dir);
        assertNotNull(handler.mMessages);
    }

    // Test error scenarios

    @Test
    public void testGetMessageKeyNotFoundException() {
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = handler.getMessage("nonExistentKey", params);
        assertEquals("Key: nonExistentKey not found in message file", message);
    }

    @Test
    public void testAddMessagesFileNotFound() {
        MessageHandler handler = new MessageHandler();
        String basename = "testBaseName";
        String dir = "nonExistentDir/";
        handler.addMessages(basename, dir);
        assertNull(handler.mMessages);
    }

}
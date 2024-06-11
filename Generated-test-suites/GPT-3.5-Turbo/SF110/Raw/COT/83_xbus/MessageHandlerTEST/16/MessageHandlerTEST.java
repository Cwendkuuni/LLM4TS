
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "key1";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String result = handler.getMessage(key, params);

        assertNotNull(result);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "key1";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String result = handler.getMessageOptional(key, params);

        assertNotNull(result);
    }

    @Test
    public void testGetMessageOptional_NullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "key1";
        List<String> params = null;

        String result = handler.getMessageOptional(key, params);

        assertNotNull(result);
    }

    // Edge case test for message not found
    @Test
    public void testGetMessage_MessageNotFound() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "nonexistent";

        String result = handler.getMessage(key, null);

        assertNotNull(result);
        assertEquals("Key: nonexistent not found in message file", result);
    }

    // Exception handling test for file not found
    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_FileNotFound() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "nonexistent";

        handler.addMessages(basename, dir);
    }

    // Exception handling test for IOException
    @Test(expected = IOException.class)
    public void testAddMessages_IOError() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "error";

        handler.addMessages(basename, dir);
    }
}

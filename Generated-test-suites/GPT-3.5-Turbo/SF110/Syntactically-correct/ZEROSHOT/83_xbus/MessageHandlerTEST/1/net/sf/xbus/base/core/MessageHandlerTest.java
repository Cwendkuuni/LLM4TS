import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance("basename");
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String key = "key";
        String expected = "expectedMessage";

        String result = messageHandler.getMessage(key, params);

        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage_NullMessageText() {
        List<String> params = new ArrayList<>();
        String key = "key";
        String expected = "Key: " + key + " not found in message file";

        String result = messageHandler.getMessage(key, params);

        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String key = "key";
        String expected = "expectedMessage";

        String result = messageHandler.getMessageOptional(key, params);

        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional_NullMessages() {
        List<String> params = new ArrayList<>();
        String key = "key";

        String result = messageHandler.getMessageOptional(key, params);

        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_NullMessageText() {
        List<String> params = new ArrayList<>();
        String key = "key";

        String result = messageHandler.getMessageOptional(key, params);

        assertNull(result);
    }

    @Test
    public void testGetInstance() {
        String basename = "basename";
        MessageHandler instance = MessageHandler.getInstance(basename);

        assertNotNull(instance);
    }
    
    // Add more test methods for other methods in MessageHandler class

}
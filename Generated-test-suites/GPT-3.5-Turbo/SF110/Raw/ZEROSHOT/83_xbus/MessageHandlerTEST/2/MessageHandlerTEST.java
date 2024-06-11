import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private String key;
    private List<String> params;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("example");
        key = "testKey";
        params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
    }

    @Test
    public void testGetMessage() {
        String result = messageHandler.getMessage(key, params);
        assertNotNull(result);
        assertEquals("Key: testKey not found in message file", result);
    }

    @Test
    public void testGetMessageOptional() {
        String result = messageHandler.getMessageOptional(key, params);
        assertNull(result);
    }

    @Test
    public void testGetInstance() {
        String basename = "example";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
        assertSame(messageHandler, instance);
    }

    @Test
    public void testAddMessages() {
        String basename = "example";
        String dir = Constants.XBUS_ETC;
        messageHandler.addMessages(basename, dir);
        Hashtable<String, Object> messages = messageHandler.mMessages;
        assertNotNull(messages);
        assertFalse(messages.isEmpty());
    }
}
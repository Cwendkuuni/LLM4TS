import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("baseName");
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        params.add("param3");

        String result = messageHandler.getMessage("key1", params);

        assertNotNull(result);
        assertEquals("Key: key1 not found in message file", result);
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        params.add("param3");

        String result = messageHandler.getMessageOptional("key1", params);

        assertNull(result);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("baseName");

        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        messageHandler.addMessages("baseName", "dir");

        Hashtable<String, String> messages = messageHandler.mMessages;

        assertNotNull(messages);
        assertEquals(0, messages.size());
    }
}
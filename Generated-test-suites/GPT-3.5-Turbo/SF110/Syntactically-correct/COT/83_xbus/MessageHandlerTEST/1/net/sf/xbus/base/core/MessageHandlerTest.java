
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = new MessageHandler("testBasename");
    }

    @Test
    public void testGetMessage() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        String message = messageHandler.getMessage(key, params);
        assertNull(message);
    }

    @Test
    public void testGetMessageOptional() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        String message = messageHandler.getMessageOptional(key, params);
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        String basename = "testBasename";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        String basename = "testBasename";
        String dir = "testDir";
        messageHandler.addMessages(basename, dir);
        // add appropriate asserts
    }
}

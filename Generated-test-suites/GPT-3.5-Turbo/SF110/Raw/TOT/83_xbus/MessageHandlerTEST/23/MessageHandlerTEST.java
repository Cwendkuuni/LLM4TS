
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("example");
    }

    @Test
    public void testGetMessage_existingKey() {
        String key = "key1";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expectedMessage = "This is the message for key1 with param1 and param2.";
        String actualMessage = messageHandler.getMessage(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_nullKey() {
        String key = null;
        List<String> params = new ArrayList<>();

        String expectedMessage = "Key: null not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_optionalExistingKey() {
        String key = "key2";
        List<String> params = new ArrayList<>();
        params.add("param1");

        String expectedMessage = "This is an optional message for key2 with param1.";
        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_optionalNullKey() {
        String key = null;
        List<String> params = new ArrayList<>();

        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_optionalNullParams() {
        String key = "key2";
        List<String> params = null;

        String expectedMessage = "This is an optional message for key2 with $1$.";
        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetInstance_existingBasename() {
        String basename = "example";

        MessageHandler instance = messageHandler.getInstance(basename);

        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_nullBasename() {
        String basename = null;

        MessageHandler instance = messageHandler.getInstance(basename);

        assertNull(instance);
    }

    @Test
    public void testAddMessages_existingDir() {
        String basename = "example";
        String dir = "path/to/existing/dir/";

        messageHandler.addMessages(basename, dir);

        // Assert that messages were successfully added
        assertEquals(10, messageHandler.mMessages.size());
    }

    @Test
    public void testAddMessages_nullBasename() {
        String basename = null;
        String dir = "path/to/existing/dir/";

        messageHandler.addMessages(basename, dir);

        // Assert that no messages were added
        assertEquals(0, messageHandler.mMessages.size());
    }

    @Test
    public void testAddMessages_nullDir() {
        String basename = "example";
        String dir = null;

        messageHandler.addMessages(basename, dir);

        // Assert that no messages were added
        assertEquals(0, messageHandler.mMessages.size());
    }

    @Test
    public void testAddMessages_nonExistingDir() {
        String basename = "example";
        String dir = "path/to/non/existing/dir/";

        messageHandler.addMessages(basename, dir);

        // Assert that no messages were added
        assertEquals(0, messageHandler.mMessages.size());
    }
}


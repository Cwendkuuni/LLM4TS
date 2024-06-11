import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessage_ValidKey() {
        String message = messageHandler.getMessage("validKey", null);
        assertNotNull(message);
        assertEquals("Key: validKey not found in message file", message);
    }

    @Test
    public void testGetMessage_NullKey() {
        String message = messageHandler.getMessage(null, null);
        assertNotNull(message);
        assertEquals("Key: null not found in message file", message);
    }

    @Test
    public void testGetMessageOptional_ValidKeyWithParams() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String message = messageHandler.getMessageOptional("validKeyWithParams", params);
        assertNotNull(message);
        assertEquals("This is a valid key with params: param1 and param2", message);
    }

    @Test
    public void testGetMessageOptional_NullKeyWithParams() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String message = messageHandler.getMessageOptional(null, params);
        assertNull(message);
    }

    @Test
    public void testGetMessageOptional_ValidKeyWithNullParams() {
        String message = messageHandler.getMessageOptional("validKeyWithParams", null);
        assertNotNull(message);
        assertEquals("This is a valid key with params: $1$ and $2$", message);
    }

    @Test
    public void testGetInstance_ValidBasename() {
        MessageHandler instance = MessageHandler.getInstance("validBasename");
        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_NullBasename() {
        MessageHandler instance = MessageHandler.getInstance(null);
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages_ValidBasenameAndDirectory() {
        String basename = "test";
        String dir = "/path/to/directory";

        messageHandler.addMessages(basename, dir);
        // Assert that the messages were added successfully
    }

    @Test
    public void testAddMessages_NullBasenameAndDirectory() {
        String dir = "/path/to/directory";

        messageHandler.addMessages(null, dir);
        // Assert that no messages were added
    }

    @Test
    public void testAddMessages_ValidBasenameAndNullDirectory() {
        String basename = "test";

        messageHandler.addMessages(basename, null);
        // Assert that no messages were added
    }

    @Test
    public void testAddMessages_InvalidBasenameAndDirectory() {
        String basename = "invalid";
        String dir = "/path/to/directory";

        messageHandler.addMessages(basename, dir);
        // Assert that no messages were added
    }

    @After
    public void tearDown() {
        messageHandler = null;
    }

}
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
    public void testGetMessage() {
        String message = messageHandler.getMessage("key", null);
        assertEquals("Key: key not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("key", params);
        assertEquals("Key: key not found in message file", message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("test");
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        String basename = "test";
        String dir = ".";
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.mMessages.get("test_message"));
    }


    @Test(expected = NullPointerException.class)
    public void testGetMessageNullParams() {
        messageHandler.getMessage("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageOptionalNullParams() {
        messageHandler.getMessageOptional("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullBasename() {
        MessageHandler.getInstance(null);
    }

}
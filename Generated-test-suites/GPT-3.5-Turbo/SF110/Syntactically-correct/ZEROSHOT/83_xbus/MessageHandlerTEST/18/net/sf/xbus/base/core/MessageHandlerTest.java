import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("basename");
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessage("key", params);
        assertEquals("Key: key not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("key", params);
        assertNull("Message should be null", message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("basename");
        assertNotNull("Instance should not be null", instance);
    }

    @Test
    public void testAddMessages() {
        String basename = "basename";
        String dir = "testDir";
        messageHandler.addMessages(basename, dir);
        // Add assertions to verify that messages are added correctly
    }
}
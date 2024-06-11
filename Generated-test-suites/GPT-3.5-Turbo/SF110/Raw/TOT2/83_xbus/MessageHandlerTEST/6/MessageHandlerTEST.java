import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "INVALID_KEY";
        List params = Arrays.asList("param1", "param2");
        String result = messageHandler.getMessage(key, params);
        assertEquals("Key: INVALID_KEY not found in message file", result);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "VALID_KEY";
        List params = Arrays.asList("param1", "param2");
        String result = messageHandler.getMessageOptional(key, params);
        assertNull(result);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("test");
        // Test adding messages for a specific base name and directory
        // Add appropriate test cases for edge cases and error scenarios
    }

    // Add more tests as needed for edge cases and error scenarios

}
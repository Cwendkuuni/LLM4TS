import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "testKey";
        String expected = "Key: " + key + " not found in message file";
        assertEquals(expected, new MessageHandler().getMessage(key, params));
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "testKey";
        assertNull(new MessageHandler().getMessageOptional(key, params));
    }

    @Test
    public void testGetInstance() {
        assertNotNull(MessageHandler.getInstance("test"));
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("test");
        assertNotNull(messageHandler);
    }

    // Add more test cases for edge cases and exception scenarios here

}
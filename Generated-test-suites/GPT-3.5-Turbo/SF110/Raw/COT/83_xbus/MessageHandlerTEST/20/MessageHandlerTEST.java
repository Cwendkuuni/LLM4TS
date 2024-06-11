import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");

        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String result = messageHandler.getMessage(key, params);

        assertNotNull(result);
        // Additional assertions for the functionality of the getMessage method
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");

        // Test case for null parameters
        String key = "testKey";
        List<String> params = null;

        String result = messageHandler.getMessageOptional(key, params);

        assertNull(result);
        // Additional assertions for the functionality of the getMessageOptional method
    }

    @Test
    public void testGetInstance() {
        // Test case for creating a new instance
        MessageHandler messageHandler = MessageHandler.getInstance("basename");

        assertNotNull(messageHandler);
        // Additional assertions for the functionality of the getInstance method
    }

    // Test case for other public methods in the MessageHandler class

    // Test cases for potential edge cases and exception handling scenarios

}


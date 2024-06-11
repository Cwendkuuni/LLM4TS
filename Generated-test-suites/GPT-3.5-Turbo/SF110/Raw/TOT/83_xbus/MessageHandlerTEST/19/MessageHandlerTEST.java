
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @BeforeEach
    public void setup() {
        messageHandler = new MessageHandler("testBasename");
    }

    @Test
    public void testGetMessage_ValidKeyAndParams_ReturnsFormattedMessage() {
        String key = "validKey";
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expected = "This is a valid key: param1 param2";
        String actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage_KeyNotFound_ReturnsErrorMessage() {
        String key = "invalidKey";
        List<Object> params = new ArrayList<>();
        params.add("param1");

        String expected = "Key: invalidKey not found in message file";
        String actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage_NullParams_ReturnsFormattedMessageWithNull() {
        String key = "validKey";
        List<Object> params = null;

        String expected = "This is a valid key: <null>";
        String actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional_ValidKeyAndParams_ReturnsFormattedMessage() {
        String key = "validKey";
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expected = "This is a valid key: param1 param2";
        String actual = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional_KeyNotFound_ReturnsNull() {
        String key = "invalidKey";
        List<Object> params = new ArrayList<>();
        params.add("param1");

        assertNull(messageHandler.getMessageOptional(key, params));
    }

    @Test
    public void testGetMessageOptional_NullParams_ReturnsFormattedMessageWithNull() {
        String key = "validKey";
        List<Object> params = null;

        String expected = "This is a valid key: <null>";
        String actual = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, actual);
    }

    // Additional Test Cases for class methods can be added here

}

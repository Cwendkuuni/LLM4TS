import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("test");
    }

    @Test
    public void testGetMessage_KeyExists_ReturnsMessage() {
        String key = "key1";
        List<String> params = Arrays.asList("param1", "param2");
        String expected = "Message with params: param1, param2";
        String actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage_KeyDoesNotExist_ReturnsErrorMessage() {
        String key = "invalidKey";
        List<String> params = Arrays.asList("param1", "param2");
        String expected = "Key: invalidKey not found in message file";
        String actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional_KeyExists_ReturnsMessage() {
        String key = "key2";
        List<String> params = Arrays.asList("param1", "param2");
        String expected = "Optional message with params: param1, param2";
        String actual = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional_KeyDoesNotExist_ReturnsNull() {
        String key = "invalidKey";
        List<String> params = Arrays.asList("param1", "param2");
        String actual = messageHandler.getMessageOptional(key, params);
        assertNull(actual);
    }

    @Test
    public void testGetMessageOptional_NullParams_ReturnsMessageWithoutParams() {
        String key = "key2";
        List<String> params = null;
        String expected = "Optional message with params: $1$, $2$";
        String actual = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetInstance_BasenameExists_ReturnsExistingInstance() {
        String basename = "testBasename";
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
}
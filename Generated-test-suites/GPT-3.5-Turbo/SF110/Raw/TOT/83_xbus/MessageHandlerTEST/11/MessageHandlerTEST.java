import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    private static final String BASENAME = "test_messages";
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance(BASENAME);
    }

    @Test
    public void testGetMessage_WhenKeyExists_ShouldReturnMessageText() {
        String key = "key1";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expected = "This is a message with param1 and param2";
        String result = messageHandler.getMessage(key, params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage_WhenKeyIsNull_ShouldReturnErrorMessage() {
        String key = null;
        List<String> params = new ArrayList<>();
        String expected = "Key: null not found in message file";
        String result = messageHandler.getMessage(key, params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage_WhenKeyNotExists_ShouldReturnErrorMessage() {
        String key = "nonexistent_key";
        List<String> params = new ArrayList<>();
        String expected = "Key: nonexistent_key not found in message file";
        String result = messageHandler.getMessage(key, params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional_WhenKeyExists_ShouldReturnMessageText() {
        String key = "key2";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expected = "This is an optional message with param1 and param2";
        String result = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional_WhenKeyIsNull_ShouldReturnNull() {
        String key = null;
        List<String> params = new ArrayList<>();
        String result = messageHandler.getMessageOptional(key, params);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_WhenKeyNotExists_ShouldReturnNull() {
        String key = "nonexistent_key";
        List<String> params = new ArrayList<>();
        String result = messageHandler.getMessageOptional(key, params);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_WhenParamsIsNull_ShouldReturnMessageText() {
        String key = "key2";
        List<String> params = null;
        String expected = "This is an optional message";
        String result = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetInstance_WhenInstanceExists_ShouldReturnSameInstance() {
        String basename = BASENAME;
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetInstance_WhenInstanceNotExists_ShouldReturnNewInstance() {
        String basename = "nonexistent_basename";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages_WhenMessagesExistInDirectories_ShouldAddMessages() {
        String basename = BASENAME;
        String dir = "directory_with_messages";
        messageHandler.addMessages(basename, dir);
        String key = "key3";
        String expected = "This is a message from directory_with_messages";
        String result = messageHandler.getMessageOptional(key, null);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMessages_WhenMessagesNotExistInDirectories_ShouldThrowNullPointerException() {
        String basename = "nonexistent_basename";
        String dir = "nonexistent_directory";
        messageHandler.addMessages(basename, dir);
    }

}
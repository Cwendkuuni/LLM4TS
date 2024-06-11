import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage_keyExists_returnsMessageText() {
        MessageHandler messageHandler = new MessageHandler("test_messages");
        List<String> params = Arrays.asList("John", "Doe");
        String expected = "Hello John Doe!";
        String result = messageHandler.getMessage("hello_message", params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage_keyNotFound_returnsErrorMessage() {
        MessageHandler messageHandler = new MessageHandler("test_messages");
        List<String> params = new ArrayList<>();
        String expected = "Key: key_not_found not found in message file";
        String result = messageHandler.getMessage("key_not_found", params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional_keyExists_returnsMessageText() {
        MessageHandler messageHandler = new MessageHandler("test_messages");
        List<String> params = Arrays.asList("Jane", "Smith");
        String expected = "Hello Jane Smith!";
        String result = messageHandler.getMessageOptional("hello_message", params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional_keyNotFound_returnsNull() {
        MessageHandler messageHandler = new MessageHandler("test_messages");
        List<String> params = new ArrayList<>();
        String result = messageHandler.getMessageOptional("key_not_found", params);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_nullParams_returnsMessageText() {
        MessageHandler messageHandler = new MessageHandler("test_messages");
        String expected = "No params provided";
        String result = messageHandler.getMessageOptional("param_message", null);
        assertEquals(expected, result);
    }

    @Test
    public void testGetInstance_existingBasename_returnsExistingInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("test_messages");
        MessageHandler instance2 = MessageHandler.getInstance("test_messages");
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetInstance_newBasename_returnsNewInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("test_messages1");
        MessageHandler instance2 = MessageHandler.getInstance("test_messages2");
        assertNotSame(instance1, instance2);
    }

    @Test
    public void testGetInstance_synchronizedAccess_returnsSameInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("test_messages");
        MessageHandler instance2 = MessageHandler.getInstance("test_messages");
        MessageHandler instance3 = MessageHandler.getInstance("test_messages");
        assertSame(instance1, instance2);
        assertSame(instance2, instance3);
    }

    @Test
    public void testAddMessages_existingBasenameAndDir_updatesMessages() {
        MessageHandler messageHandler = new MessageHandler("test_messages");
        String key = "additional_key";
        String value = "Additional message";
        messageHandler.addMessages("test_messages", "test_dir");
        String result = messageHandler.getMessageOptional(key, null);
        assertEquals(value, result);
    }
}
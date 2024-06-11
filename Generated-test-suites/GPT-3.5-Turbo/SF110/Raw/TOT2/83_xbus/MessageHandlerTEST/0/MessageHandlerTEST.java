import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage(){
        MessageHandler messageHandler = new MessageHandler();

        // Typical use case
        String key = "TestKey";
        List<String> params = Arrays.asList("param1", "param2");
        String expected = "Test message: param1, param2";
        String actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);

        // Edge case: key not found
        key = "NonExistentKey";
        expected = "Key: NonExistentKey not found in message file";
        actual = messageHandler.getMessage(key, params);
        assertEquals(expected, actual);

        // Error scenario: params is null
        key = "TestKey";
        List<String> nullParams = null;
        expected = "Test message: <null>, <null>";
        actual = messageHandler.getMessage(key, nullParams);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional(){
        MessageHandler messageHandler = new MessageHandler();
        
        // Typical use case
        String key = "TestKey";
        List<String> params = Arrays.asList("param1", "param2");
        String expected = "Test message: param1, param2";
        String actual = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, actual);

        // Edge case: key not found
        key = "NonExistentKey";
        assertNull(messageHandler.getMessageOptional(key, params));

        // Error scenario: params is null
        key = "TestKey";
        List<String> nullParams = null;
        assertNull(messageHandler.getMessageOptional(key, nullParams));
    }

    @Test
    public void testGetInstance(){
        // Edge case: getInstance with same basename should return same instance
        MessageHandler instance1 = MessageHandler.getInstance("Test");
        MessageHandler instance2 = MessageHandler.getInstance("Test");
        assertSame(instance1, instance2);
    }

    @Test
    public void testAddMessages(){
        MessageHandler messageHandler = new MessageHandler();

        // Typical use case
        String basename = "TestMessages";
        String dir = "path/to/messages/";
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.mMessages);

        // Edge case: empty directory
        basename = "EmptyMessages";
        dir = "path/to/empty/";
        messageHandler.addMessages(basename, dir);
        assertNull(messageHandler.mMessages);

        // Error scenario: file not found
        basename = "NonExistentMessages";
        dir = "path/to/nonexistent/";
        assertThrows(FileNotFoundException.class, () -> {
            messageHandler.addMessages(basename, dir);
        });
    }
}
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    // Test getMessage method with a valid key and params
    @Test
    public void testGetMessage_ValidKeyAndParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = Arrays.asList("param1", "param2");
        String message = messageHandler.getMessage("key", params);
        assertNotNull(message);
    }

    // Test getMessage method with a null key and params
    @Test
    public void testGetMessage_NullKeyAndParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = null;
        String message = messageHandler.getMessage(null, params);
        assertEquals("Key: null not found in message file", message);
    }

    // Test getMessageOptional method with a valid key and params
    @Test
    public void testGetMessageOptional_ValidKeyAndParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = Arrays.asList("param1", "param2");
        String message = messageHandler.getMessageOptional("key", params);
        assertNotNull(message);
    }

    // Test getMessageOptional method with a null key and params
    @Test
    public void testGetMessageOptional_NullKeyAndParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = null;
        String message = messageHandler.getMessageOptional(null, params);
        assertNull(message);
    }

    // Test getInstance method with a valid basename
    @Test
    public void testGetInstance_ValidBasename() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);
    }

    // Test addMessages method with a valid basename and dir
    @Test
    public void testAddMessages_ValidBasenameAndDir() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "basename";
        String dir = "dir";
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.getMessage("key", new ArrayList<>()));
    }

    // Test addMessages method with an invalid basename and dir
    @Test
    public void testAddMessages_InvalidBasenameAndDir() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "invalid";
        String dir = "dir";
        messageHandler.addMessages(basename, dir);
        assertNull(messageHandler.getMessage("key", new ArrayList<>()));
    }

    // Test the edge case of empty params list in getMessage method
    @Test
    public void testGetMessage_EmptyParamsList() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        String message = messageHandler.getMessage("key", params);
        assertNotNull(message);
    }

    // Test the exception handling scenario of FileNotFoundException in addMessages method
    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_FileNotFoundException() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "basename";
        String dir = "invalid_dir";
        messageHandler.addMessages(basename, dir);
    }

    // Test the exception handling scenario of IOException in addMessages method
    @Test(expected = IOException.class)
    public void testAddMessages_IOException() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "basename";
        String dir = "invalid_dir";
        messageHandler.addMessages(basename, dir);
    }

    // Test the edge case of an empty Hashtable in MessageHandler initialization
    @Test
    public void testMessageHandler_EmptyHashtable() {
        MessageHandler messageHandler = new MessageHandler("basename");
        assertNotNull(messageHandler);
    }
    
    // Test the edge case of a null Hashtable in getMessageOptional method
    @Test
    public void testGetMessageOptional_NullHashtable() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.mMessages = null;
        String message = messageHandler.getMessageOptional("key", new ArrayList<>());
        assertNull(message);
    }
    
    // Test the edge case of an empty Hashtable in getMessageOptional method
    @Test
    public void testGetMessageOptional_EmptyHashtable() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.mMessages = new Hashtable<>();
        String message = messageHandler.getMessageOptional("key", new ArrayList<>());
        assertNull(message);
    }
}



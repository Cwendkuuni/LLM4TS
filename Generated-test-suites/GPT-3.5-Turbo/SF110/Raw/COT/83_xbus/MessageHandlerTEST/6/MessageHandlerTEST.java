
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        // Test case 1: Key exists in message file
        MessageHandler handler = MessageHandler.getInstance("basename");
        String key = "key1";
        List<String> params = new ArrayList<>();
        String expectedMessage = "Message 1";
        String actualMessage = handler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        // Test case 2: Key does not exist in message file
        key = "nonexistentKey";
        expectedMessage = "Key: " + key + " not found in message file";
        actualMessage = handler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        // Test case 3: Null key and params
        key = null;
        expectedMessage = null;
        actualMessage = handler.getMessage(key, null);
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional() {
        // Test case 1: Key exists in message file
        MessageHandler handler = MessageHandler.getInstance("basename");
        String key = "key1";
        List<String> params = new ArrayList<>();
        String expectedMessage = "Message 1";
        String actualMessage = handler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        // Test case 2: Key does not exist in message file
        key = "nonexistentKey";
        expectedMessage = null;
        actualMessage = handler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        // Test case 3: Key exists with multiple parameters
        key = "key2";
        params.add("param1");
        params.add("param2");
        expectedMessage = "Message with param1 and param2";
        actualMessage = handler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        // Test case 4: Key exists with null parameters
        key = "key3";
        params.clear();
        params.add(null);
        expectedMessage = "Message with <null>";
        actualMessage = handler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        // Test case 5: Null key and params
        key = null;
        expectedMessage = null;
        actualMessage = handler.getMessageOptional(key, null);
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetInstance() {
        // Test case 1: Existing basename
        String basename = "basename";
        MessageHandler expectedInstance = MessageHandler.mInstances.get(basename);
        MessageHandler actualInstance = MessageHandler.getInstance(basename);
        assertSame(expectedInstance, actualInstance);
        
        // Test case 2: Nonexistent basename
        basename = "nonexistentBasename";
        expectedInstance = new MessageHandler(basename);
        actualInstance = MessageHandler.getInstance(basename);
        assertNotSame(expectedInstance, actualInstance);
    }
    
    @Test
    public void testConstructor() {
        // Test case 1: Messages added with valid basename and directory
        String basename = "basename";
        MessageHandler handler = new MessageHandler(basename);
        assertNotNull(handler.mMessages);
        
        // Test case 2: Messages not added with invalid basename and directory
        basename = "invalidBasename";
        handler = new MessageHandler(basename);
        assertNull(handler.mMessages);
    }
    
    @Test
    public void testAddMessages() {
        // Test case 1: Valid basename and directory
        String basename = "basename";
        String dir = "path/to/directory";
        MessageHandler handler = new MessageHandler(basename);
        handler.addMessages(basename, dir);
        assertNotNull(handler.mMessages);
        
        // Test case 2: Invalid directory
        basename = "basename";
        dir = "invalidDirectory";
        handler = new MessageHandler(basename);
        handler.addMessages(basename, dir);
        assertNull(handler.mMessages);
    }
}

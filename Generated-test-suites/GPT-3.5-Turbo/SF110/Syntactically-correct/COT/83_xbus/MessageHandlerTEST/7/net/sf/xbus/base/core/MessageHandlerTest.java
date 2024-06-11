
import org.junit.*;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expectedMessage = "Expected message text";
        String actualMessage = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expectedMessage = "Expected message text";
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_nullParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "testKey";
        List<String> params = null;
        
        String expectedMessage = "Expected message text";
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_invalidKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertNull(actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_nullMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertNull(actualMessage);
    }
    
    @Test
    public void testGetInstance_newInstance() {
        String basename = "basename";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstance_existingInstance() {
        String basename = "basename";
        MessageHandler.mInstances.put(basename, new MessageHandler(basename));
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testAddMessages_validParameters() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String basename = "basename";
        String dir = "directory";
        
        messageHandler.addMessages(basename, dir);
        
        // Test if messages have been added to mMessages hashtable
        assertNotNull(messageHandler.mMessages);
    }
    
    @Test
    public void testAddMessages_invalidBasename() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String basename = "invalidBasename";
        String dir = "directory";
        
        messageHandler.addMessages(basename, dir);
        
        // Test if no messages have been added to mMessages hashtable
        assertNull(messageHandler.mMessages);
    }
    
    @Test
    public void testAddMessages_invalidDir() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String basename = "basename";
        String dir = "invalidDirectory";
        
        messageHandler.addMessages(basename, dir);
        
        // Test if no messages have been added to mMessages hashtable
        assertNull(messageHandler.mMessages);
    }
    
    @Test
    public void testMessagesFilter_acceptValidFile() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("prefix", "postfix");
        File dir = new File("directory");
        String filename = "prefix_filename_postfix";
        
        boolean result = filter.accept(dir, filename);
        
        assertTrue(result);
    }
    
    @Test
    public void testMessagesFilter_acceptInvalidFile() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("prefix", "postfix");
        File dir = new File("directory");
        String filename = "invalid_filename";
        
        boolean result = filter.accept(dir, filename);
        
        assertFalse(result);
    }
}

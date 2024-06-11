import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

public class MessageHandlerTest {
    
    @BeforeClass
    public static void setUp() {
        // Set up the test data
        // ...
    }
    
    @AfterClass
    public static void tearDown() {
        // Clean up the test data
        // ...
    }
    
    @Test
    public void testGetMessage() {
        // Test when message exists
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        assertEquals("Message with params", handler.getMessage("key", params));
        
        // Test when message does not exist
        assertNull(handler.getMessage("nonexistentKey", params));
    }
    
    @Test
    public void testGetMessageOptional() {
        // Test when message exists
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        assertEquals("Message with params", handler.getMessageOptional("key", params));
        
        // Test when message does not exist
        assertNull(handler.getMessageOptional("nonexistentKey", params));
        
        // Test when params are null
        assertNull(handler.getMessageOptional("key", null));
    }
    
    @Test
    public void testGetInstance() {
        // Test when instance does not exist
        MessageHandler instance = MessageHandler.getInstance("basename");
        assertNotNull(instance);
        
        // Test when instance already exists
        MessageHandler instance2 = MessageHandler.getInstance("basename");
        assertSame(instance, instance2);
    }
    
    @Test
    public void testAddMessages() {
        // Test when messages files exist in directory
        MessageHandler handler = new MessageHandler();
        String basename = "basename";
        String dir = "directory";
        handler.addMessages(basename, dir);
        Properties messages = handler.mMessages;
        assertNotNull(messages);
        assertEquals("Message 1", messages.get("key1"));
        assertEquals("Message 2", messages.get("key2"));
        
        // Test when messages files do not exist in directory
        handler.addMessages(basename, "nonexistentDir");
        Properties messages2 = handler.mMessages;
        assertNotNull(messages2);
        assertEquals(messages, messages2);
    }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        // Test case 1
        MessageHandler handler = new MessageHandler("example");
        String key = "key1";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedMessage = "Expected message for key1 with params: param1, param2";
        assertEquals(expectedMessage, handler.getMessage(key, params));
        
        // Test case 2
        key = "key2";
        params = new ArrayList<>();
        expectedMessage = "Expected message for key2";
        assertEquals(expectedMessage, handler.getMessage(key, params));
        
        // Test case 3
        key = "key3";
        params = null;
        expectedMessage = "Key: key3 not found in message file";
        assertEquals(expectedMessage, handler.getMessage(key, params));
    }
    
    @Test
    public void testGetMessageOptional() {
        // Test case 1
        MessageHandler handler = new MessageHandler("example");
        String key = "key1";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedMessage = "Expected message for key1 with params: param1, param2";
        assertEquals(expectedMessage, handler.getMessageOptional(key, params));
        
        // Test case 2
        key = "key2";
        params = new ArrayList<>();
        expectedMessage = "Expected message for key2";
        assertEquals(expectedMessage, handler.getMessageOptional(key, params));
        
        // Test case 3
        key = "key3";
        params = null;
        assertNull(handler.getMessageOptional(key, params));
    }
    
    @Test
    public void testGetInstance() {
        // Test case 1
        String basename = "example";
        MessageHandler handler = MessageHandler.getInstance(basename);
        assertNotNull(handler);
        
        // Test case 2
        basename = "example2";
        handler = MessageHandler.getInstance(basename);
        assertNotNull(handler);
        
        // Test case 3
        basename = "example3";
        handler = MessageHandler.getInstance(basename);
        assertNotNull(handler);
    }
    
    @Before
    public void setUp() {
        // Set up test data or objects
    }
    
    // Additional test methods for addMessages and MessagesFilter if needed
    
}
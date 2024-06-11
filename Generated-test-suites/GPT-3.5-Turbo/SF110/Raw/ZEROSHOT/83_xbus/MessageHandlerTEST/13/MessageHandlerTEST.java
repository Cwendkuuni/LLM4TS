import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // Initialize test data
        String key = "testKey";
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add(123);
        
        // Create MessageHandler instance
        MessageHandler messageHandler = MessageHandler.getInstance("testBasename");
        
        // Test getMessage method
        String expectedResult = "Key: " + key + " not found in message file";
        String actualResult = messageHandler.getMessage(key, params);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetMessageOptional() {
        // Initialize test data
        String key = "testKey";
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add(123);
        
        // Create MessageHandler instance
        MessageHandler messageHandler = MessageHandler.getInstance("testBasename");
        
        // Test getMessageOptional method
        String expectedResult = null;
        String actualResult = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetInstance() {
        // Initialize test data
        String basename = "testBasename";
        
        // Test getInstance method
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        assertSame(instance1, instance2);
    }
    
    @Test
    public void testAddMessages() {
        // Initialize test data
        String basename = "testBasename";
        String dir = "testDir";
        
        // Create MessageHandler instance
        MessageHandler messageHandler = new MessageHandler(basename);
        
        // Test addMessages method
        // TODO: Implement test case for addMessages method
    }
}
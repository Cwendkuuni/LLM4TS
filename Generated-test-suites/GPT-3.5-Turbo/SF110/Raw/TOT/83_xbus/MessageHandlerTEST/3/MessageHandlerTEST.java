import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    private static MessageHandler messageHandler;
    
    @BeforeClass
    public void setUp() {
        messageHandler = new MessageHandler();
    }
    
    @Test
    public void testGetMessage_ValidKeyAndParams_ReturnsFormattedMessage() {
        String key = "testKey";
        List<String> params = Arrays.asList("Param1", "Param2");
        String expectedMessage = "This is a test message with Param1 and Param2";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        Assert.assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessage_NullKey_ReturnsErrorMessage() {
        String key = null;
        List<String> params = Arrays.asList("Param1", "Param2");
        String expectedErrorMessage = "Key: null not found in message file";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        Assert.assertEquals(expectedErrorMessage, actualMessage);
        // Additional assertions for verifying error logging or exception handling
    }
    
    @Test
    public void testGetMessage_ValidKeyAndEmptyParams_ReturnsFormattedMessageWithoutParams() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        String expectedMessage = "This is a test message";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        Assert.assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_ValidKeyAndParams_ReturnsFormattedMessage() {
        String key = "testKey";
        List<String> params = Arrays.asList("Param1", "Param2");
        String expectedMessage = "This is a test message with Param1 and Param2";
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        Assert.assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_NullKey_ReturnsNull() {
        String key = null;
        List<String> params = Arrays.asList("Param1", "Param2");
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        Assert.assertNull(actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_ValidKeyAndEmptyParams_ReturnsFormattedMessageWithoutParams() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        String expectedMessage = "This is a test message";
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        Assert.assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetInstance_ValidBasename_ReturnsValidInstance() {
        String basename = "testBasename";
        
        MessageHandler actualInstance = MessageHandler.getInstance(basename);
        
        Assert.assertNotNull(actualInstance);
        // Additional assertions for verifying the uniqueness of instances
    }
    
    @Test
    public void testGetInstance_NullBasename_ReturnsNullInstance() {
        String basename = null;
        
        MessageHandler actualInstance = MessageHandler.getInstance(basename);
        
        Assert.assertNull(actualInstance);
        // Additional assertions for verifying error handling or exception throwing
    }

    // Additional test cases for edge cases and error scenarios
    
}
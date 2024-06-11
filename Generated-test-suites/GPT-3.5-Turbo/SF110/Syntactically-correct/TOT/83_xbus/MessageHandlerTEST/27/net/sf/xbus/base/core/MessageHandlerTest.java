import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        messageHandler = new MessageHandler("test");
    }
    
    @Test
    public void testGetMessage_WithValidKeyAndParams_ReturnsMessageText() {
        String key = "validKey";
        List<String> params = Arrays.asList("param1", "param2");
        String expectedMessageText = "This is a test message with param1 and param2";
        
        String actualMessageText = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessageText, actualMessageText);
    }
    
    @Test
    public void testGetMessage_WithInvalidKey_ReturnsErrorMessage() {
        String key = "invalidKey";
        List<String> params = null;
        String expectedErrorMessage = "Key: " + key + " not found in message file";
        
        String actualErrorMessage = messageHandler.getMessage(key, params);
        
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
    
    @Test
    public void testGetMessage_WithNullParams_ReturnsMessageTextWithNullValues() {
        String key = "validKey";
        List<String> params = null;
        String expectedMessageText = "This is a test message with <$1$> and <$2$>";
        
        String actualMessageText = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessageText, actualMessageText);
    }
    
    @Test
    public void testGetMessageOptional_WithValidKeyAndParams_ReturnsMessageText() {
        String key = "validKey";
        List<String> params = Arrays.asList("param1", "param2");
        String expectedMessageText = "This is a test message with param1 and param2";
        
        String actualMessageText = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessageText, actualMessageText);
    }
    
    @Test
    public void testGetMessageOptional_WithInvalidKey_ReturnsNull() {
        String key = "invalidKey";
        List<String> params = null;
        
        String actualMessageText = messageHandler.getMessageOptional(key, params);
        
        assertNull(actualMessageText);
    }
    
    @Test
    public void testGetMessageOptional_WithNullParams_ReturnsMessageTextWithNullValues() {
        String key = "validKey";
        List<String> params = null;
        String expectedMessageText = "This is a test message with <$1$> and <$2$>";
        
        String actualMessageText = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessageText, actualMessageText);
    }
    
    @Test
    public void testGetInstance_WithExistingBasename_ReturnsExistingInstance() {
        String basename = "test";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
        assertSame(messageHandler, instance);
    }
    
    @Test
    public void testGetInstance_WithNonExistingBasename_CreatesNewInstance() {
        String basename = "nonExisting";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
        assertNotSame(messageHandler, instance);
    }
    
    @Test
    public void testAddMessages_WithValidBasenameAndDir_AddsMessagesToMessaageHandler() {
        String basename = "test";
        String dir = "messages";
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals(2, messageHandler.mMessages.size());
        assertTrue(messageHandler.mMessages.containsKey("validKey1"));
        assertTrue(messageHandler.mMessages.containsKey("validKey2"));
    }
    
    @Test
    public void testAddMessages_WithInvalidBasenameAndDir_DoesNotAddMessagesToMessaageHandler() {
        String basename = "invalid";
        String dir = "messages";
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals(0, messageHandler.mMessages.size());
    }
}
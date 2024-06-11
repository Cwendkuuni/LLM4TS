import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessage_validKey_returnsMessageText() {
        String key = "key1";
        List<String> params = Arrays.asList("param1", "param2");
        String expectedMessage = "This is message key1 with param1 and param2";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_invalidKey_returnsErrorMessage() {
        String key = "invalidKey";
        List<String> params = Arrays.asList("param1", "param2");
        String expectedMessage = "Key: invalidKey not found in message file";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
  
    @Test
    public void testGetMessageOptional_validKey_returnsMessageText() {
        String key = "key2";
        List<String> params = Arrays.asList("param1", "param2");
        String expectedMessage = "This is optional message key2 with param1 and param2";
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
  
    @Test
    public void testGetMessageOptional_invalidKey_returnsNull() {
        String key = "invalidKey";
        List<String> params = Arrays.asList("param1", "param2");
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertNull(actualMessage);
    }

    @Test
    public void testGetInstance_newInstanceCreated() {
        String basename = "test";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages_messageFilesAddedToMessages() {
        String basename = "test";
        String dir = "etc/";
        int expectedSize = 2;
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals(expectedSize, messageHandler.mMessages.size());
    }
}
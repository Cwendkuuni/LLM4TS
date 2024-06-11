import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        List<String> params = new ArrayList<>();
        String key = "testKey";
        assertEquals("Key: testKey not found in message file", messageHandler.getMessage(key, params));
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        List<String> params = new ArrayList<>();
        String key = "testKey";
        assertNull(messageHandler.getMessageOptional(key, params));
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = new MessageHandler();
        assertNotNull(MessageHandler.getInstance("testInstance"));
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_FileNotFoundException() {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.addMessages("testBasename", "nonExistentDir/");
    }
    
    @Test
    public void testAddMessages_IOException() {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.addMessages("testBasename", "invalidDir/");
        assertEquals(0, messageHandler.mMessages.size());
    }
}
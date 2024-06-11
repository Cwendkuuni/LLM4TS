import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "testKey";
        
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessage(key, params);
        
        assertEquals("Key: testKey not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "testKey";
        
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "testBasename";
        
        MessageHandler messageHandler = MessageHandler.getInstance(basename);
        
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "testBasename";
        String dir = "/path/to/messages/dir/";
        
        MessageHandler messageHandler = new MessageHandler(basename);
        messageHandler.addMessages(basename, dir);
        
        assertNotNull(messageHandler.mMessages);
    }
}
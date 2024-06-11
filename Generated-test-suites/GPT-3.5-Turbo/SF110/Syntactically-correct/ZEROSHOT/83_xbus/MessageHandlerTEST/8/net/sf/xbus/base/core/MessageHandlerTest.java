import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = messageHandler.getMessage(key, params);
        
        assertNotNull(result);
        assertEquals("Key: key not found in message file", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = messageHandler.getMessageOptional(key, params);
        
        assertNull(result);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "basename";
        
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String basename = "basename";
        String dir = "dir";
        
        messageHandler.addMessages(basename, dir);
        
        // Add assert statements here
    }
}
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List params = new ArrayList();
        params.add("param1");
        params.add(2);
        
        String result = handler.getMessage(key, params);
        
        assertEquals("Key: testKey not found in message file", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List params = new ArrayList();
        params.add("param1");
        params.add(2);
        
        String result = handler.getMessageOptional(key, params);
        
        assertNull(result);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "testInstance";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }
}
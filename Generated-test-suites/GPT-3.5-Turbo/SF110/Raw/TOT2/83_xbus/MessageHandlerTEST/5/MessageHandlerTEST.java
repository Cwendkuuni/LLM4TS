import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("arg1");
        String key = "test";
        
        MessageHandler handler = new MessageHandler();
        String message = handler.getMessage(key, params);
        
        assertNotNull(message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("arg1");
        String key = "test";
        
        MessageHandler handler = new MessageHandler();
        String message = handler.getMessageOptional(key, params);
        
        assertNotNull(message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "test";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "test";
        String dir = "resources/";
        
        MessageHandler handler = new MessageHandler();
        handler.addMessages(basename, dir);
        
        assertNotNull(handler.mMessages);
    }
}
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
        params.add("param2");
        String message = handler.getMessage(key, params);
        
        assertNotNull(message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List params = new ArrayList();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessageOptional(key, params);
        
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler handler = MessageHandler.getInstance("testBasename");
        
        assertNotNull(handler);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler("testBasename");
        
        assertNotNull(handler);
        assertNotNull(handler.getMessage("testKey", null));
    }
  
    // Edge cases and exception handling scenarios can be added here
}
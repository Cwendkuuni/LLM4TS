import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessage("key", params);
        assertEquals("Key: key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessageOptional("key", params);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test1");
        MessageHandler handler2 = MessageHandler.getInstance("test2");
        assertNotNull(handler1);
        assertNotNull(handler2);
        assertNotSame(handler1, handler2);
    }
    
    // Add additional test cases for identified edge cases and exception handling scenarios
    
    
    
    // TODO: Add additional test cases here
    
    
}
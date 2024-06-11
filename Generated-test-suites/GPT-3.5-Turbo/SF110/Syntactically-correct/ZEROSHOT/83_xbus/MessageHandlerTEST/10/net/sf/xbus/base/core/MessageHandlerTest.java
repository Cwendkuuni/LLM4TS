import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance("testfile");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String result = handler.getMessage("key1", params);
        assertEquals("Message mismatch", "Expected message", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("testfile");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String result = handler.getMessageOptional("key1", params);
        assertEquals("Optional message mismatch", "Expected optional message", result);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        MessageHandler handler = MessageHandler.getInstance("testfile");
        String result = handler.getMessageOptional("key1", null);
        assertNull("Optional message should be null", result);
    }
    
    @Test
    public void testGetMessageOptional_NullKey() {
        MessageHandler handler = MessageHandler.getInstance("testfile");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String result = handler.getMessageOptional(null, params);
        assertNull("Optional message should be null", result);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("testfile");
        MessageHandler handler2 = MessageHandler.getInstance("testfile");
        assertSame("Instances should be the same", handler1, handler2);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler("testfile");
        assertEquals("Number of messages mismatch", 10, handler.mMessages.size());
    }
    
    // Add more test methods as needed
    
}
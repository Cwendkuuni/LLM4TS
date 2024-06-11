import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage_validKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "validKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expected = "This is a valid message with param1 and param2";
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessage_invalidKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expected = "Key: invalidKey not found in message file";
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional_validKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "validKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expected = "This is a valid message with param1 and param2";
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional_invalidKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expected = null;
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expected, actual);
    }

    // Other test methods for remaining methods in MessageHandler class
    // ...

}
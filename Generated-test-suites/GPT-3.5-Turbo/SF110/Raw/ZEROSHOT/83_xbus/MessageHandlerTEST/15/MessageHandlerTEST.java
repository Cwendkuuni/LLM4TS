import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        MessageHandler messageHandler = new MessageHandler();
        String result = messageHandler.getMessage("key", params);
        assertEquals("Key: key not found in message file", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        MessageHandler messageHandler = new MessageHandler();
        String result = messageHandler.getMessageOptional("key", params);
        assertNull(result);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("basename");
        assertNotNull(messageHandler);
    }
}
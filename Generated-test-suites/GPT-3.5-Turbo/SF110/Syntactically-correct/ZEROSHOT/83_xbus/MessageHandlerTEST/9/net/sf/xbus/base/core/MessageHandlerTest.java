import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler("basename");
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessage("key", params);
        assertEquals("Key: key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler("basename");
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessageOptional("key", params);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testConstructor() {
        MessageHandler messageHandler = new MessageHandler("basename");
        assertNotNull(messageHandler);
    }
}
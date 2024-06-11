import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        
        String message = handler.getMessage("TestKey", params);
        
        assertEquals("Key: TestKey not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        
        String message = handler.getMessageOptional("TestKey", params);
        
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler handler = MessageHandler.getInstance("TestBaseName");
        assertNotNull(handler);
    }

    // Add more test cases here
    
}
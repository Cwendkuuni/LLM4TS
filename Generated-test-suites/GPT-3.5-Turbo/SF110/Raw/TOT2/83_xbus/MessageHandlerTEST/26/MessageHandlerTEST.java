import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessage("testKey", params);
        assertNotNull(message);
        assertEquals("Key: testKey not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("testKey", params);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("testInstance");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.addMessages("testBase", "testDir");
        assertNotNull(messageHandler);
    }
}
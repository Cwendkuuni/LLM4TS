import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
  
    private MessageHandler messageHandler;
  
    @Before
    public void setUp() {
        messageHandler = new MessageHandler();
    }
  
    @Test
    public void testGetMessage() {
        String key = "test.key";
        List<Object> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessage(key, params);
        assertEquals("Key: test.key not found in message file", message);
    }
  
    @Test
    public void testGetMessageOptional() {
        String key = "test.key";
        List<Object> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional(key, params);
        assertNull(message);
    }
  
    @Test
    public void testGetInstance() {
        String basename = "test";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }
  
}
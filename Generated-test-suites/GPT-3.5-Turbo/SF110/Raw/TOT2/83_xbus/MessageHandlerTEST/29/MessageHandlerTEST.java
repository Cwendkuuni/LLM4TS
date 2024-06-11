import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = Arrays.asList("param1", "param2");
        String result = messageHandler.getMessage(key, params);
        assertNotNull(result);
        assertEquals("Key: testKey not found in message file", result);
    }
}
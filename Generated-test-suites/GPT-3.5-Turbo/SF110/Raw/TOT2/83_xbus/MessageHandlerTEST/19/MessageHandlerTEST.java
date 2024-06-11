import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessage("test_key", null);
        assertEquals("Key: test_key not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("test_key", params);
        assertEquals("messageText", message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test_basename");
        assertNotNull("Instance is null", messageHandler);
    }

}
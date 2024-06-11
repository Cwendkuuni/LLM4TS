
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = new MessageHandler("basename");
    }

    @Test
    public void testGetMessage() {
        String message = messageHandler.getMessage("key", null);
        assertEquals("Key: key not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        String message = messageHandler.getMessageOptional("key", null);
        assertNull(message);
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessageOptional("key", params);
        assertEquals("message with parameters: param1, param2", message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("basename");
        MessageHandler instance2 = MessageHandler.getInstance("basename");
        assertSame(instance1, instance2);
    }
}

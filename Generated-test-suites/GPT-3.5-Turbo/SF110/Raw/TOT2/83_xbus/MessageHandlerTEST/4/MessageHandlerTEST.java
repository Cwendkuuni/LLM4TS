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
        MessageHandler instance1 = MessageHandler.getInstance("instance1");
        MessageHandler instance2 = MessageHandler.getInstance("instance1");
        assertSame(instance1, instance2);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.addMessages("test", "test_directory/");
        assertNotNull(messageHandler.mMessages);
    }

}
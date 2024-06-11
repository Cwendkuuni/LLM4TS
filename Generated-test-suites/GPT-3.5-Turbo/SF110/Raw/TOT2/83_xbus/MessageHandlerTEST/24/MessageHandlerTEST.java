import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessage(key, params);
        assertNotNull(message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String message = messageHandler.getMessageOptional(key, params);
        assertNotNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("test");
        messageHandler.addMessages("test", "testDir");
        assertNotNull(messageHandler);
    }
}
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        String result = messageHandler.getMessage(key, params);
        assertNotNull(result);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        String result = messageHandler.getMessageOptional(key, params);
        assertNotNull(result);
    }

    @Test
    public void testGetInstance() {
        String basename = "test";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler();
        String basename = "test";
        String dir = "testDir";
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.mMessages);
    }
}
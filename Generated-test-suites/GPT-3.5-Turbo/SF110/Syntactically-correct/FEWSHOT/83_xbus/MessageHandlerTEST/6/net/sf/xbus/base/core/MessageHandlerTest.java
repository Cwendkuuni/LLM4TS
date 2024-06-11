import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler instance = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedMessage = "Expected message";
        instance.mMessages.put(key, expectedMessage);

        assertEquals(expectedMessage, instance.getMessage(key, params));
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler instance = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedMessage = "Expected message";
        instance.mMessages.put(key, expectedMessage);

        assertEquals(expectedMessage, instance.getMessageOptional(key, params));
    }

    @Test
    public void testGetMessageOptional_NullParams() {
        MessageHandler instance = new MessageHandler("basename");
        String key = "key";
        String expectedMessage = "Expected message";
        instance.mMessages.put(key, expectedMessage);

        assertEquals(expectedMessage, instance.getMessageOptional(key, null));
    }

    @Test
    public void testGetMessageOptional_NullMessages() {
        MessageHandler instance = new MessageHandler("basename");
        String key = "key";

        assertNull(instance.getMessageOptional(key, null));
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("basename");

        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        MessageHandler instance = new MessageHandler("basename");
        String basename = "basename";
        String dir = "dir";
        String key = "key";
        String message = "message";
        instance.mMessages.put(key, message);

        instance.addMessages(basename, dir);

        assertEquals(message, instance.mMessages.get(key));
    }

}
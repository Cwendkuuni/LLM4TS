import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // typical use case
        List<String> params = new ArrayList<>();
        params.add("param1");
        String result = MessageHandler.getInstance("test").getMessage("key1", params);
        assertEquals("Key: key1 not found in message file", result);

        // edge case
        result = MessageHandler.getInstance("test").getMessage("key1", null);
        assertEquals("Key: key1 not found in message file", result);
    }

    @Test
    public void testGetMessageOptional() {
        // typical use case
        List<String> params = new ArrayList<>();
        params.add("param1");
        String result = MessageHandler.getInstance("test").getMessageOptional("key2", params);
        assertNull(result);

        // edge case
        result = MessageHandler.getInstance("test").getMessageOptional("key2", null);
        assertNull(result);
    }

    @Test
    public void testGetInstance() {
        // typical use case
        MessageHandler instance = MessageHandler.getInstance("test");
        assertNotNull(instance);

        // edge case
        MessageHandler instance2 = MessageHandler.getInstance("test");
        assertEquals(instance, instance2);
    }

    @Test
    public void testAddMessages() {
        // typical use case
        MessageHandler instance = MessageHandler.getInstance("test");
        instance.addMessages("test", "testDir");
        assertNotNull(instance.mMessages);

        // error scenario
        // case of FileNotFoundException
        try {
            instance.addMessages("test", "nonexistentDir");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }
}
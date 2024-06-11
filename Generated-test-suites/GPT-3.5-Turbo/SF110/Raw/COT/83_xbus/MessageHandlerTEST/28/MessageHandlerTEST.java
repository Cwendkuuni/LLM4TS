import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;

    @BeforeClass
    public static void setup() {
        // Initialize MessageHandler instance
        messageHandler = MessageHandler.getInstance("basename");
    }

    @Test
    public void testGetMessage() {
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedMessage = "Expected Message";

        // Add test data to mMessages hashtable
        messageHandler.mMessages.put(key, expectedMessage);

        // Test getMessage with valid key and params
        String actualMessage = messageHandler.getMessage(key, params);
        Assert.assertEquals(expectedMessage, actualMessage);

        // Test getMessage with invalid key
        actualMessage = messageHandler.getMessage("invalidKey", params);
        Assert.assertEquals("Key: invalidKey not found in message file", actualMessage);
    }

    @Test
    public void testGetMessageOptional() {
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedMessage = "Expected Message";

        // Add test data to mMessages hashtable
        messageHandler.mMessages.put(key, expectedMessage);

        // Test getMessageOptional with valid key and params
        String actualMessage = messageHandler.getMessageOptional(key, params);
        Assert.assertEquals(expectedMessage, actualMessage);

        // Test getMessageOptional with invalid key
        actualMessage = messageHandler.getMessageOptional("invalidKey", params);
        Assert.assertNull(actualMessage);
    }

    @Test
    public void testGetInstance() {
        String basename = "basename";
        MessageHandler instance = MessageHandler.getInstance(basename);

        // Verify that the returned instance is not null
        Assert.assertNotNull(instance);

        // Verify that the same instance is returned for the same basename
        Assert.assertSame(instance, MessageHandler.getInstance(basename));
    }

    @Test
    public void testAddMessages() {
        String basename = "basename";
        String dir = "directory";

        // Test addMessages with valid basename and dir
        messageHandler.addMessages(basename, dir);

        // Add assert statements to verify the correctness of addMessages
    }
}
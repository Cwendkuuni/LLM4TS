
import org.junit.*;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessage_ExistingKey_ReturnsMessageText() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expectedMessage = "Test message with parameters: param1 param2";
        String actualMessage = messageHandler.getMessage(key, params);

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_NonExistingKey_ReturnsErrorMessage() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();

        String expectedMessage = "Key: nonExistingKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_NullParams_ReturnsMessageTextWithoutParameters() {
        String key = "existingKey";
        List<String> params = null;

        String expectedMessage = "Test message without parameters";
        String actualMessage = messageHandler.getMessage(key, params);

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_ExistingKey_ReturnsMessageText() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expectedMessage = "Test message with parameters: param1 param2";
        String actualMessage = messageHandler.getMessageOptional(key, params);

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_NonExistingKey_ReturnsNull() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();

        String actualMessage = messageHandler.getMessageOptional(key, params);

        Assert.assertNull(actualMessage);
    }

    @Test
    public void testGetMessageOptional_NullParams_ReturnsMessageTextWithoutParameters() {
        String key = "existingKey";
        List<String> params = null;

        String expectedMessage = "Test message without parameters";
        String actualMessage = messageHandler.getMessageOptional(key, params);

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetInstance_ExistingBasename_ReturnsMessageHandlerInstance() {
        String basename = "existingBasename";

        MessageHandler instance = MessageHandler.getInstance(basename);

        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstance_NonExistingBasename_ReturnsNewMessageHandlerInstance() {
        String basename = "nonExistingBasename";

        MessageHandler instance = MessageHandler.getInstance(basename);

        Assert.assertNotNull(instance);
    }

    @Test
    public void testAddMessages_ValidBasenameAndDir_AddsMessagesToMessageHandler() {
        String basename = "test";
        String dir = "validDir";

        messageHandler.addMessages(basename, dir);

        // Verify that the messages are added to the mMessages Hashtable
        Map<String, String> mMessages = getMessageField(messageHandler);
        Assert.assertEquals(3, mMessages.size());
    }

    @Test(expected = Exception.class)
    public void testAddMessages_InvalidBasenameAndDir_ThrowsException() {
        String basename = "invalidBasename";
        String dir = "invalidDir";

        messageHandler.addMessages(basename, dir);

        // Expecting an exception to be thrown
    }

    private Map<String, String> getMessageField(MessageHandler messageHandler) {
        try {
            Field field = MessageHandler.class.getDeclaredField("mMessages");
            field.setAccessible(true);
            return (Map<String, String>) field.get(messageHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}

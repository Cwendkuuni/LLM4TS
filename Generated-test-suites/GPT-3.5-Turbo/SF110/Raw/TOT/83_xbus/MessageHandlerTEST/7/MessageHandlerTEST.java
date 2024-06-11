import static org.junit.Assert.*;
import org.junit.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("test");
    }

    @Test
    public void testGetMessage_ExistingKey_ReturnsMessageText() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        String messageText = messageHandler.getMessage(key, params);

        assertNotNull(messageText);
        assertTrue(messageText.contains(key));
    }

    @Test
    public void testGetMessage_NonExistingKey_ReturnsErrorMessage() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();
        String messageText = messageHandler.getMessage(key, params);

        assertNotNull(messageText);
        assertTrue(messageText.contains(key));
    }

    @Test
    public void testGetMessageOptional_ExistingKey_ReturnsMessageTextWithReplacedParams() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String messageText = messageHandler.getMessageOptional(key, params);

        assertNotNull(messageText);
        assertTrue(messageText.contains("param1"));
        assertTrue(messageText.contains("param2"));
    }

    @Test
    public void testGetMessageOptional_NonExistingKey_ReturnsNull() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();
        String messageText = messageHandler.getMessageOptional(key, params);

        assertNull(messageText);
    }

    @Test
    public void testGetInstance_FirstInstance_ReturnsSameInstance() {
        String basename = "test";
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetInstance_MultipleInstances_ReturnsDifferentInstances() {
        String basename1 = "test1";
        String basename2 = "test2";
        MessageHandler instance1 = MessageHandler.getInstance(basename1);
        MessageHandler instance2 = MessageHandler.getInstance(basename2);

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }

    @Test
    public void testAddMessages_ValidFiles_AddsMessagesToRepository() {
        String basename = "test";
        String dir = "directory/";
        messageHandler.addMessages(basename, dir);

        // Test if messages are added correctly to the mMessages hashtable
        // ...
    }

    @Test
    public void testAddMessages_InvalidFiles_ThrowsException() {
        String basename = "test";
        String dir = "directory/";
        
        assertThrows(FileNotFoundException.class, () -> {
            messageHandler.addMessages(basename, dir);
        });
    }

    // Additional test cases for edge and error scenarios can be added here

}
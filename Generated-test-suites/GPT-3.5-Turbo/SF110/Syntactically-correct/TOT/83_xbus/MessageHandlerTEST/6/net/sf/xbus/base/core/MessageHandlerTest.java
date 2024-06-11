import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void getMessage_validKeyAndParams_returnsMessageText() {
        // Arrange
        String key = "key1";
        List<String> params = Arrays.asList("param1", "param2");
        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        String messageText = messageHandler.getMessage(key, params);

        // Assert
        assertEquals("MessageText", messageText);
    }

    @Test
    public void getMessage_keyNotFound_returnsErrorMessage() {
        // Arrange
        String key = "unknownKey";
        List<String> params = Arrays.asList("param1", "param2");
        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        String messageText = messageHandler.getMessage(key, params);

        // Assert
        assertTrue(messageText.contains("not found in message file"));
    }

    @Test
    public void getMessageOptional_validKeyAndParams_returnsMessageText() {
        // Arrange
        String key = "key1";
        List<String> params = Arrays.asList("param1", "param2");
        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        String messageText = messageHandler.getMessageOptional(key, params);

        // Assert
        assertEquals("MessageText", messageText);
    }

    @Test
    public void getMessageOptional_keyNotFound_returnsNull() {
        // Arrange
        String key = "unknownKey";
        List<String> params = Arrays.asList("param1", "param2");
        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        String messageText = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(messageText);
    }

    @Test
    public void getInstance_existingBasename_returnsExistingInstance() {
        // Arrange
        String basename = "basename";
        MessageHandler messageHandler1 = MessageHandler.getInstance(basename);
        MessageHandler messageHandler2 = MessageHandler.getInstance(basename);

        // Assert
        assertSame("Instance", messageHandler1, messageHandler2);
    }

    @Test
    public void getInstance_nonExistingBasename_returnsNewInstance() {
        // Arrange
        String basename1 = "basename1";
        String basename2 = "basename2";
        MessageHandler messageHandler1 = MessageHandler.getInstance(basename1);
        MessageHandler messageHandler2 = MessageHandler.getInstance(basename2);

        // Assert
        assertNotSame("Instance", messageHandler1, messageHandler2);
    }

    @Test
    public void addMessages_existingBasenameAndDir_loadsMessages() {
        // Arrange
        String basename = "basename";
        String dir = "directory";
        MessageHandler messageHandler = new MessageHandler(basename);

        // Act
        messageHandler.addMessages(basename, dir);

        // Assert
        assertNotNull("Messages", messageHandler.mMessages);
        assertTrue("Messages size > 0", messageHandler.mMessages.size() > 0);
    }

    @Test
    public void addMessages_nonExistingBasenameAndDir_doesNotLoadMessages() {
        // Arrange
        String basename = "unknownBasename";
        String dir = "unknownDirectory";
        MessageHandler messageHandler = new MessageHandler(basename);

        // Act
        messageHandler.addMessages(basename, dir);

        // Assert
        assertNotNull("Messages", messageHandler.mMessages);
        assertEquals("Messages size", 0, messageHandler.mMessages.size());
    }
}
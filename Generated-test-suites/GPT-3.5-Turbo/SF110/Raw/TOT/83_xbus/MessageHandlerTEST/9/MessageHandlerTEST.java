import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage_validKeyAndParams_shouldReturnValidMessage() {
        // Arrange
        String key = "validKey";
        List params = new ArrayList<>();
        params.add("param1");
        
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.mMessages.put(key, "Valid message with param: $1$");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertEquals("Valid message with param: param1", result);
    }

    @Test
    public void testGetMessage_nullKey_shouldReturnErrorMessage() {
        // Arrange
        String key = null;
        List params = new ArrayList<>();
        params.add("param1");
        
        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertEquals("Key: null not found in message file", result);
    }

    @Test
    public void testGetMessage_emptyParams_shouldReturnValidMessage() {
        // Arrange
        String key = "validKey";
        List params = new ArrayList<>();
        
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.mMessages.put(key, "Valid message without parameters");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertEquals("Valid message without parameters", result);
    }

    @Test
    public void testGetMessageOptional_validKeyAndParams_shouldReturnValidMessage() {
        // Arrange
        String key = "validKey";
        List params = new ArrayList<>();
        params.add("param1");
        
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.mMessages.put(key, "Valid message with param: $1$");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertEquals("Valid message with param: param1", result);
    }

    @Test
    public void testGetMessageOptional_nullKey_shouldReturnNull() {
        // Arrange
        String key = null;
        List params = new ArrayList<>();
        params.add("param1");
        
        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_emptyParams_shouldReturnValidMessage() {
        // Arrange
        String key = "validKey";
        List params = new ArrayList<>();
        
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.mMessages.put(key, "Valid message without parameters");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertEquals("Valid message without parameters", result);
    }

    @Test
    public void testGetInstance_newBasename_shouldReturnNewMessageHandlerInstance() {
        // Arrange
        String basename = "newBasename";

        // Act
        MessageHandler result = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetInstance_existingBasename_shouldReturnExistingMessageHandlerInstance() {
        // Arrange
        String basename = "existingBasename";
        
        MessageHandler.mInstances.put(basename, new MessageHandler(basename));

        // Act
        MessageHandler result = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testAddMessages_validBasenameAndDir_shouldAddMessagesToMessageHandler() {
        // Arrange
        String basename = "validBasename";
        String dir = "validDir";
        File dirFile = new File(dir);
        dirFile.mkdir();

        File messagesFile = new File(dir + "/validBasename_en.properties");
        try {
            messagesFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageHandler messageHandler = new MessageHandler("basename");

        // Act
        messageHandler.addMessages(basename, dir);

        // Assert
        assertTrue(messageHandler.mMessages.contains("validKey"));
    }
}
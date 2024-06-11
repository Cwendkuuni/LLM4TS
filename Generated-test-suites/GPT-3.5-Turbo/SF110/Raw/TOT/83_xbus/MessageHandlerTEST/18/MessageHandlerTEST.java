import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage_keyExists() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Key: key not found in message file", result);
    }

    @Test
    public void testGetMessage_keyDoesNotExist() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "nonexistentKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Key: nonexistentKey not found in message file", result);
    }

    @Test
    public void testGetMessageOptional_keyExists() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("param1"));
        assertTrue(result.contains("param2"));
    }

    @Test
    public void testGetMessageOptional_keyDoesNotExist() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "nonexistentKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetInstance_firstInstance() {
        // Arrange
        String basename = "basename";

        // Act
        MessageHandler instance = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(instance);
        assertEquals(basename, instance.getBasename());
    }

    @Test
    public void testGetInstance_secondInstance() {
        // Arrange
        String basename = "basename";

        // Act
        MessageHandler firstInstance = MessageHandler.getInstance(basename);
        MessageHandler secondInstance = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(firstInstance);
        assertNotNull(secondInstance);
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testAddMessages_validFiles() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String dirname = "directory";

        // Act
        messageHandler.addMessages("basename", dirname);

        // Assert
        assertNotNull(messageHandler.getMessages());
        assertFalse(messageHandler.getMessages().isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_missingFiles() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String dirname = "nonexistentDirectory";

        // Act
        messageHandler.addMessages("basename", dirname);
    }

}
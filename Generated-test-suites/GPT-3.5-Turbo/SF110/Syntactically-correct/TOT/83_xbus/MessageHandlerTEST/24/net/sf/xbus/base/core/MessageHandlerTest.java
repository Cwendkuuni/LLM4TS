import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    // Test cases for getMessage method
    
    @Test
    public void testGetMessage_ExistingKey_ReturnsCorrectMessage() {
        // Arrange
        MessageHandler handler = new MessageHandler("test");
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        
        // Act
        String message = handler.getMessage(key, params);
        
        // Assert
        assertEquals("Returned message should be the correct one", "Expected message", message);
    }
    
    @Test
    public void testGetMessage_NonExistingKey_ReturnsCustomErrorMessage() {
        // Arrange
        MessageHandler handler = new MessageHandler("test");
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();
        
        // Act
        String message = handler.getMessage(key, params);
        
        // Assert
        assertEquals("Returned message should be a custom error message", "Key: nonExistingKey not found in message file", message);
    }
    
    // Test cases for getMessageOptional method
    
    @Test
    public void testGetMessageOptional_ExistingKey_ReturnsMessageWithParamsReplaced() {
        // Arrange
        MessageHandler handler = new MessageHandler("test");
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        // Act
        String message = handler.getMessageOptional(key, params);
        
        // Assert
        assertEquals("Returned message should have params replaced", "Expected message with param1 and param2", message);
    }
    
    @Test
    public void testGetMessageOptional_ParamIsNull_ReturnsMessageWithNullParam() {
        // Arrange
        MessageHandler handler = new MessageHandler("test");
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add(null);
        
        // Act
        String message = handler.getMessageOptional(key, params);
        
        // Assert
        assertEquals("Returned message should contain <null> for null param", "Expected message with <null>", message);
    }
    
    // Test cases for getInstance method
    
    @Test
    public void testGetInstance_ExistingInstance_ReturnsSameInstance() {
        // Arrange
        String basename = "test";
        
        // Act
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        
        // Assert
        assertSame("Both instances should be the same", instance1, instance2);
    }
    
    @Test
    public void testGetInstance_NonExistingInstance_ReturnsNewInstance() {
        // Arrange
        String basename1 = "test1";
        String basename2 = "test2";
        
        // Act
        MessageHandler instance1 = MessageHandler.getInstance(basename1);
        MessageHandler instance2 = MessageHandler.getInstance(basename2);
        
        // Assert
        assertNotSame("Instances should be different for different basenames", instance1, instance2);
    }
    
    // Additional test cases for addMessages method
    
    @Test
    public void testAddMessages_ValidBasenameAndDirectory_LoadsMessages() {
        // Arrange
        MessageHandler handler = new MessageHandler("test");
        String basename = "test";
        String dir = "testDir";
        
        // Act
        handler.addMessages(basename, dir);
        
        // Assert
        assertNotNull("Messages should not be null after loading", handler.mMessages);
        assertTrue("Messages should contain at least one message", handler.mMessages.size() > 0);
    }
    
    @Test
    public void testAddMessages_InvalidBasenameAndDirectory_DoesNotLoadMessages() {
        // Arrange
        MessageHandler handler = new MessageHandler("test");
        String basename = "nonExistingBasename";
        String dir = "nonExistingDir";
        
        // Act
        handler.addMessages(basename, dir);
        
        // Assert
        assertNull("Messages should be null if basename or directory is invalid", handler.mMessages);
    }

}
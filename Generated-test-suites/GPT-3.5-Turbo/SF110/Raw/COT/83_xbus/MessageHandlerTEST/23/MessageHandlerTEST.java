import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessage("key", null);
        assertEquals("Key: key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", null);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testConstructor() {
        MessageHandler messageHandler = new MessageHandler("basename");
        assertNotNull(messageHandler);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        messageHandler.getMessage(null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetMessageOptionalWithNullKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        messageHandler.getMessageOptional(null, null);
    }
    
    @Test
    public void testGetMessageOptionalWithNullParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", null);
        assertNotNull(message);
    }
    
    @Test
    public void testAddMessagesWithNonExistingDir() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.addMessages("basename", "nonexistingdir");
        // Test that adding messages from a non-existing directory does not throw an exception
    }
    
    @Test
    public void testAddMessagesWithInvalidPropertiesFile() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.addMessages("basename", "invalidfile.properties");
        // Test that adding messages from an invalid properties file does not throw an exception
    }
    
    // Complete test file starts here
    
    
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessage("key", null);
        assertEquals("Key: key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", null);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testConstructor() {
        MessageHandler messageHandler = new MessageHandler("basename");
        assertNotNull(messageHandler);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        messageHandler.getMessage(null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetMessageOptionalWithNullKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        messageHandler.getMessageOptional(null, null);
    }
    
    @Test
    public void testGetMessageOptionalWithNullParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", null);
        assertNotNull(message);
    }
    
    @Test
    public void testAddMessagesWithNonExistingDir() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.addMessages("basename", "nonexistingdir");
        // Test that adding messages from a non-existing directory does not throw an exception
    }
    
    @Test
    public void testAddMessagesWithInvalidPropertiesFile() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.addMessages("basename", "invalidfile.properties");
        // Test that adding messages from an invalid properties file does not throw an exception
    }
    
    

}

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    private List<String> params;
    
    @Before
    public void setUp() {
        String basename = "test";
        messageHandler = MessageHandler.getInstance(basename);
        params = new ArrayList<>();
    }
    
    // getMessage() Test Cases
    
    @Test
    public void testGetMessage_WhenKeyExists_ShouldReturnMessageText() {
        String key = "existing_key";
        params.add("param1");
        params.add("param2");
        String expectedMessage = "This is an existing key: param1 param2";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessage_WhenKeyDoesNotExist_ShouldReturnErrorMessage() {
        String key = "non_existing_key";
        String expectedMessage = "Key: non_existing_key not found in message file";
        
        String actualMessage = messageHandler.getMessage(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    // getMessageOptional() Test Cases

    @Test
    public void testGetMessageOptional_WhenKeyExistsAndParamsNotEmpty_ShouldReturnMessageTextWithParamsReplaced() {
        String key = "existing_key";
        params.add("param1");
        params.add("param2");
        String expectedMessage = "This is an existing key: param1 param2";
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_WhenKeyDoesNotExist_ShouldReturnNull() {
        String key = "non_existing_key";
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertNull(actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_WhenParamsIsEmpty_ShouldReturnMessageTextWithoutParamReplacements() {
        String key = "existing_key";
        String expectedMessage = "This is an existing key: $1$ $2$";
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    // getInstance() Test Cases
    
    @Test
    public void testGetInstance_WhenBasenameExists_ShouldReturnExistingInstance() {
        String basename = "existing_basename";
        
        MessageHandler existingInstance = MessageHandler.getInstance(basename);
        
        assertNotNull(existingInstance);
    }
    
    @Test
    public void testGetInstance_WhenBasenameDoesNotExist_ShouldCreateNewInstance() {
        String basename = "non_existing_basename";
        
        MessageHandler newInstance = MessageHandler.getInstance(basename);
        
        assertNotNull(newInstance);
    }
    
    // addMessages() Test Cases
    
    @Test
    public void testAddMessages_WhenDirIsEmpty_ShouldNotAddMessages() {
        String basename = "test";
        String dir = "";
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals(0, messageHandler.mMessages.size());
    }
    
    @Test
    public void testAddMessages_WhenMessagesFilesExist_ShouldAddMessagesToHashtable() {
        String basename = "test";
        String dir = "messages/";
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals(2, messageHandler.mMessages.size());
        assertNotNull(messageHandler.mMessages.get("key1"));
        assertNotNull(messageHandler.mMessages.get("key2"));
    }
    
    @Test
    public void testAddMessages_WhenMessagesFilesDoNotExist_ShouldNotAddMessages() {
        String basename = "test";
        String dir = "invalid_messages/";
        
        messageHandler.addMessages(basename, dir);
        
        assertEquals(0, messageHandler.mMessages.size());
    }
}


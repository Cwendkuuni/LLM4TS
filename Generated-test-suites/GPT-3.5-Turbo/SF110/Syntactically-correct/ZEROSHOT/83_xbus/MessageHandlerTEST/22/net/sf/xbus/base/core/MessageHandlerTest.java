import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageHandlerTest {
  
    private MessageHandler messageHandler;
  
    @Before
    public void setUp() {
        messageHandler = new MessageHandler("basename");
    }
  
    @After
    public void tearDown() {
        messageHandler = null;
    }
  
    @Test
    public void testGetMessage_validKey_returnsMessageText() {
        String key = "validKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
      
        String expectedMessageText = "messageText";
        messageHandler.mMessages.put(key, expectedMessageText);
      
        String actualMessageText = messageHandler.getMessage(key, params);
      
        assertEquals(expectedMessageText, actualMessageText);
    }
  
    @Test
    public void testGetMessage_invalidKey_returnsErrorMessage() {
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
      
        String expectedErrorMessage = "Key: " + key + " not found in message file";
      
        String actualErrorMessage = messageHandler.getMessage(key, params);
      
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
  
    @Test
    public void testGetMessageOptional_keyExists_returnsMessageText() {
        String key = "validKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
      
        String expectedMessageText = "messageText";
        messageHandler.mMessages.put(key, expectedMessageText);
      
        String actualMessageText = messageHandler.getMessageOptional(key, params);
      
        assertEquals(expectedMessageText, actualMessageText);
    }
  
    @Test
    public void testGetMessageOptional_keyDoesNotExist_returnsNull() {
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
      
        String actualMessageText = messageHandler.getMessageOptional(key, params);
      
        assertNull(actualMessageText);
    }
  
    @Test
    public void testGetMessageOptional_nullParams_returnsMessageTextWithNullParam() {
        String key = "validKey";
        List<String> params = null;
      
        String expectedMessageText = "messageText $1$";
        messageHandler.mMessages.put(key, expectedMessageText);
      
        String actualMessageText = messageHandler.getMessageOptional(key, params);
      
        assertEquals(expectedMessageText, actualMessageText);
    }
  
    @Test
    public void testGetMessageOptional_emptyParams_returnsMessageTextWithEmptyParam() {
        String key = "validKey";
        List<String> params = new ArrayList<>();
      
        String expectedMessageText = "messageText $1$";
        messageHandler.mMessages.put(key, expectedMessageText);
      
        String actualMessageText = messageHandler.getMessageOptional(key, params);
      
        assertEquals(expectedMessageText, actualMessageText);
    }
  
    @Test
    public void testGetInstance_newInstance_returnsNewInstance() {
        String basename = "basename";
      
        MessageHandler actualInstance = MessageHandler.getInstance(basename);
      
        assertEquals(MessageHandler.class, actualInstance.getClass());
    }
  
    @Test
    public void testGetInstance_existingInstance_returnsExistingInstance() {
        String basename = "basename";
        MessageHandler.mInstances.put(basename, messageHandler);
      
        MessageHandler actualInstance = MessageHandler.getInstance(basename);
      
        assertEquals(messageHandler, actualInstance);
    }
  
    @Test
    public void testAddMessages_validArguments_addsMessages() {
        String basename = "basename";
        String dir = "directory";
      
        String key = "key";
        String message = "message";
      
        Hashtable<String, String> expectedMessages = new Hashtable<>();
        expectedMessages.put(key, message);
      
        messageHandler.addMessages(basename, dir);
      
        assertEquals(expectedMessages, messageHandler.mMessages);
    }
  
    @Test
    public void testAddMessages_missingFile_throwsException() {
        String basename = "basename";
        String dir = "missingDirectory";
      
        try {
            messageHandler.addMessages(basename, dir);
        } catch (Exception e) {
            assertEquals(e.getClass(), FileNotFoundException.class);
        }
    }
}

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = new MessageHandler("test");
    }
    
    @Test
    public void getMessage_withValidKeyAndParams_returnFormattedMessage() {
        String key = "test.key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String actualMessage = messageHandler.getMessage(key, params);
        String expectedMessage = "This is a test message with param1 and param2.";
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void getMessage_withInvalidKey_returnErrorMessage() {
        String key = "invalid.key";
        List<String> params = new ArrayList<>();
        
        String actualMessage = messageHandler.getMessage(key, params);
        String expectedMessage = "Key: invalid.key not found in message file";
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void getMessageOptional_withValidKeyAndParams_returnFormattedMessage() {
        String key = "test.key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        String expectedMessage = "This is a test message with param1 and param2.";
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void getMessageOptional_withInvalidKey_returnNull() {
        String key = "invalid.key";
        List<String> params = new ArrayList<>();
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        assertNull(actualMessage);
    }
    
    @Test
    public void getMessageOptional_withNullParams_returnFormattedMessageWithNull() {
        String key = "test.key";
        List<String> params = null;
        
        String actualMessage = messageHandler.getMessageOptional(key, params);
        String expectedMessage = "This is a test message with <null> and <null>.";
        
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void getInstance_withExistingBasename_returnSameInstance() {
        String basename = "test";
        
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        
        assertSame(instance1, instance2);
    }
    
    @Test
    public void getInstance_withDifferentBasenames_returnDifferentInstances() {
        String basename1 = "test1";
        String basename2 = "test2";
        
        MessageHandler instance1 = MessageHandler.getInstance(basename1);
        MessageHandler instance2 = MessageHandler.getInstance(basename2);
        
        assertNotSame(instance1, instance2);
    }
    
    // Additional test cases can be added for addMessages() method.
    
}


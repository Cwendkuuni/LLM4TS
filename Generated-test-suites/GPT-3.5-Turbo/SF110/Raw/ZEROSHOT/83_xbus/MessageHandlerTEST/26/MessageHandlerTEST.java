
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessage() {
        String key = "test-key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expected = "Key: " + key + " not found in message file";
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional() {
        String key = "test-key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String expected = null;
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "test";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "test";
        String dir = "test-dir";
        
        messageHandler.addMessages(basename, dir);
        
        Hashtable<String, Object> messages = messageHandler.mMessages;
        
        assertNotNull(messages);
    }

}

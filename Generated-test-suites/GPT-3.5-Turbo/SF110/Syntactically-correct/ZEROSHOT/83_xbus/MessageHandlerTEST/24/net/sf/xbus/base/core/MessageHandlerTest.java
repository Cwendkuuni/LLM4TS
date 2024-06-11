import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("testBasename");
    }

    @Test
    public void testGetMessage() {
        String key = "testKey";
        List<String> params = Arrays.asList("param1", "param2");
        
        String message = messageHandler.getMessage(key, params);
        
        assertNotNull(message);
        assertEquals("Key: testKey not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNull(message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        String key = "testKey";
        
        String message = messageHandler.getMessageOptional(key, null);
        
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "testBasename";
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "testBasename";
        String dir = "testDir";
        
        try {
            messageHandler.addMessages(basename, dir);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        // Add assertions here to test if messages were added successfully
    }
}
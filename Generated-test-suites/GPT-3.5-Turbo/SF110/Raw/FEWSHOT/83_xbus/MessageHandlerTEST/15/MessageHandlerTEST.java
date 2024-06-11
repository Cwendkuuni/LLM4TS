package net.sf.xbus.base.core;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }
    
    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String key = "key";
        String expectedMessage = "Key: " + key + " not found in message file";
        
        String message = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String key = "key";
        String expectedMessage = "message";
        
        String message = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedMessage, message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        String key = "key";
        String expectedMessage = "message";
        
        String message = messageHandler.getMessageOptional(key, null);
        assertEquals(expectedMessage, message);
    }
    
    @Test
    public void testGetMessageOptional_NullMessages() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String key = "key";
        
        String message = messageHandler.getMessageOptional(key, params);
        assertNull(message);
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
        String dir = "dir";
        
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.mMessages);
    }
}
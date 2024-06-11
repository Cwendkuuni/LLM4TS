package net.sf.xbus.base.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "message_key";
        
        MessageHandler messageHandler = new MessageHandler("basename");
        String message = messageHandler.getMessage(key, params);
        
        assertNotNull(message);
        assertEquals("Key: " + key + " not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "message_key";
        
        MessageHandler messageHandler = new MessageHandler("basename");
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNotNull(message);
        assertEquals("Key: " + key + " not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        List<String> params = null;
        String key = "message_key";
        
        MessageHandler messageHandler = new MessageHandler("basename");
        String message = messageHandler.getMessageOptional(key, params);
        
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "basename";
        
        MessageHandler messageHandler = MessageHandler.getInstance(basename);
        
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        String basename = "basename";
        String dir = "directory";
        
        MessageHandler messageHandler = new MessageHandler(basename);
        messageHandler.addMessages(basename, dir);
        
        // Assert statements
    }
}
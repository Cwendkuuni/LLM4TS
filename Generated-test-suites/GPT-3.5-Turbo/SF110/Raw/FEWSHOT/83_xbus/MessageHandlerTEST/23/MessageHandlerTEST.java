package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance("test");
    }
    
    @Test
    public void testGetMessage_keyNotFound() {
        String key = "nonExistentKey";
        List<String> params = new ArrayList<>();
        
        String result = messageHandler.getMessage(key, params);
        
        assertEquals("Key: " + key + " not found in message file", result);
    }
    
    @Test
    public void testGetMessage_optionalKeyNotFound() {
        String key = "nonExistentKey";
        List<String> params = new ArrayList<>();
        
        String result = messageHandler.getMessageOptional(key, params);
        
        assertNull(result);
    }
    
    @Test
    public void testGetMessage_optionalKeyFoundWithNoParams() {
        String key = "keyWithNoParams";
        List<String> params = new ArrayList<>();
        
        String result = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Message with no params", result);
    }
    
    @Test
    public void testGetMessage_optionalKeyFoundWithParams() {
        String key = "keyWithParams";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Message with params param1 param2", result);
    }
}
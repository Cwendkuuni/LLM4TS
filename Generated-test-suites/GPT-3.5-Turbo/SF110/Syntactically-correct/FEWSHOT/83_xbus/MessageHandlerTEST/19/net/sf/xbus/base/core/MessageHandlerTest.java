package net.sf.xbus.base.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler("basename");
        
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = messageHandler.getMessage("key1", params);
        assertEquals("Expected result not received", "message1", result);
        
        result = messageHandler.getMessage("key2", params);
        assertEquals("Expected result not received", "Key: key2 not found in message file", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler("basename");
        
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = messageHandler.getMessageOptional("key1", params);
        assertEquals("Expected result not received", "message1", result);
        
        result = messageHandler.getMessageOptional("key2", params);
        assertNull("Expected result not received", result);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        
        assertEquals("Expected instance not received", messageHandler, MessageHandler.mInstances.get("basename"));
    }
}
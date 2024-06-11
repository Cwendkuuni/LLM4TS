package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("test");
        MessageHandler instance2 = MessageHandler.getInstance("test");
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertTrue(instance1 == instance2);
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessage("key1", params);
        
        assertEquals("Value1: param1, param2", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String message = messageHandler.getMessageOptional("key1", params);
        
        assertEquals("Value1: param1, param2", message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        String message = messageHandler.getMessageOptional("key1", null);
        
        assertEquals("Value1: $1$, $2$", message);
    }
    
    @Test
    public void testGetMessageOptional_NullMessageText() {
        String message = messageHandler.getMessageOptional("invalidKey", null);
        
        assertNull(message);
    }

}
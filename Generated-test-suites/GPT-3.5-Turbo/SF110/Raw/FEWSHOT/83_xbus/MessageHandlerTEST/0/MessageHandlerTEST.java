package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        String key = "key1";
        String expected = "message1";
        assertEquals(expected, messageHandler.getMessage(key, null));
        
        key = "key2";
        expected = "Key: key2 not found in message file";
        assertEquals(expected, messageHandler.getMessage(key, null));
        
        key = null;
        expected = "Key: null not found in message file";
        assertEquals(expected, messageHandler.getMessage(key, null));
    }
    
    @Test
    public void testGetMessageOptional() {
        String key = "key1";
        String expected = "message1";
        assertEquals(expected, messageHandler.getMessageOptional(key, null));
        
        key = "key2";
        assertNull(messageHandler.getMessageOptional(key, null));
        
        key = null;
        assertNull(messageHandler.getMessageOptional(key, null));
    }
    
    @Test
    public void testGetInstance() {
        String basename = "test";
        assertNotNull(MessageHandler.getInstance(basename));
        
        basename = null;
        assertNotNull(MessageHandler.getInstance(basename));
    }
}
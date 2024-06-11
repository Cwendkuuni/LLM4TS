package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = new MessageHandler("");
    }
    
    @Test
    public void testGetMessage() {
        String key = "testKey";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        String expected = "test message";
        
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional() {
        String key = "testKey";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        String expected = "test message";
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessage_InvalidKey() {
        String key = "invalidKey";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        String expected = "Key: invalidKey not found in message file";
        
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        String key = "testKey";
        List<String> params = null;
        String expected = "test message";
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional_NullMessages() {
        messageHandler.mMessages = null;
        String key = "testKey";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertNull(actual);
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
        
        messageHandler.addMessages(basename, dir);
        
        assertNotNull(messageHandler.mMessages);
    }
    
    @Test
    public void testMessagesFilter_Accept_ValidFile() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "testPostfix");
        File dir = new File("testDir");
        String filename = "testPrefix_testPostfix";
        
        boolean actual = filter.accept(dir, filename);
        
        assertTrue(actual);
    }
    
    @Test
    public void testMessagesFilter_Accept_InvalidPrefix() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "testPostfix");
        File dir = new File("testDir");
        String filename = "invalidPrefix_testPostfix";
        
        boolean actual = filter.accept(dir, filename);
        
        assertFalse(actual);
    }
    
    @Test
    public void testMessagesFilter_Accept_InvalidPostfix() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "testPostfix");
        File dir = new File("testDir");
        String filename = "testPrefix_invalidPostfix";
        
        boolean actual = filter.accept(dir, filename);
        
        assertFalse(actual);
    }
}
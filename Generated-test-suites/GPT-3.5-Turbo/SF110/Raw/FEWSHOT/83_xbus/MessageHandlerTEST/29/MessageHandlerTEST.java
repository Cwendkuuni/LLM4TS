package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        
        MessageHandler handler = new MessageHandler("basename");
        String message = handler.getMessage("key", params);
        
        assertEquals("Key: key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        
        MessageHandler handler = new MessageHandler("basename");
        Hashtable<String, String> messages = new Hashtable<String, String>();
        messages.put("key", "message");
        handler.mMessages = messages;
        
        String message = handler.getMessageOptional("key", params);
        
        assertEquals("message", message);
    }
    
    @Test
    public void testGetMessageOptionalNullParams() {        
        MessageHandler handler = new MessageHandler("basename");
        Hashtable<String, String> messages = new Hashtable<String, String>();
        messages.put("key", "message");
        handler.mMessages = messages;
        
        String message = handler.getMessageOptional("key", null);
        
        assertEquals("message", message);
    }
    
    @Test
    public void testGetMessageOptionalNullMessages() {
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        
        MessageHandler handler = new MessageHandler("basename");
        
        String message = handler.getMessageOptional("key", params);
        
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("basename");
        MessageHandler handler2 = MessageHandler.getInstance("basename");
        
        assertSame(handler1, handler2);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler("basename");
        
        Hashtable<String, String> messages = new Hashtable<String, String>();
        messages.put("key1", "message1");
        messages.put("key2", "message2");
        
        handler.mMessages = messages;
        
        assertEquals("message1", handler.mMessages.get("key1"));
        assertEquals("message2", handler.mMessages.get("key2"));
    }
}
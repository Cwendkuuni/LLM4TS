package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        assertEquals("Key: key not found in message file", messageHandler.getMessage("key", params));
        
        params.add("param1");
        params.add("param2");
        assertEquals("Key: key not found in message file", messageHandler.getMessage("key", params));
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNull(messageHandler.getMessageOptional("key", null));

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        assertNull(messageHandler.getMessageOptional("key", params));
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertEquals("basename", messageHandler.getInstance("basename"));
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        messageHandler.addMessages("basename", "directory");
        // Assert expected messages are added
    }
}
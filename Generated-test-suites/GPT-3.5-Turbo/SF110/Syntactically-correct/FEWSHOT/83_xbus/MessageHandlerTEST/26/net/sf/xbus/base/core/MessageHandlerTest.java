
package net.sf.xbus.base.core;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance("test");
    }
    
    @Test
    public void testGetMessage() {
        // Test when key is present in messages
        List<String> params = new ArrayList<>();
        params.add("param1");
        assertEquals("Test message for key1 with param1", messageHandler.getMessage("key1", params));
        
        // Test when key is not present in messages
        assertNull(messageHandler.getMessage("nonexistentkey", params));
    }
    
    @Test
    public void testGetMessageOptional() {
        // Test when key is present in messages
        List<String> params = new ArrayList<>();
        params.add("param1");
        assertEquals("Test message for key1 with param1", messageHandler.getMessageOptional("key1", params));
        
        // Test when key is not present in messages
        assertNull(messageHandler.getMessageOptional("nonexistentkey", params));
        
        // Test when params list is null
        assertNull(messageHandler.getMessageOptional("key1", null));
    }
    
    @Test
    public void testGetInstance() {
        // Test when instance is already created
        MessageHandler instance1 = MessageHandler.getInstance("test");
        MessageHandler instance2 = MessageHandler.getInstance("test");
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
        
        // Test when instance is not created yet
        MessageHandler instance3 = MessageHandler.getInstance("newtest");
        assertNotNull(instance3);
        assertNotSame(instance1, instance3);
    }
    
    @Test
    public void testAddMessages() {
        // Test adding messages from existing directory
        messageHandler.addMessages("test", "messages");
        assertNotNull(messageHandler.mMessages);
        
        // Test adding messages from non-existent directory
        messageHandler.addMessages("test", "nonexistentdir");
        assertNotNull(messageHandler.mMessages);
    }
}

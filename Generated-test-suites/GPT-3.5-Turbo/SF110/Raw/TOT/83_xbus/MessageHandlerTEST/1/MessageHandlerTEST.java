
package net.sf.xbus.base.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage_ExistingKey_ReturnsCorrectMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessage("key1", null);
        assertEquals("This is message 1", message);
    }
    
    @Test
    public void testGetMessage_NonExistingKey_ReturnsErrorMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessage("non-existing-key", null);
        assertEquals("Key: non-existing-key not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional_ExistingKey_ReturnsCorrectMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = Arrays.asList("parameter1", "parameter2");
        String message = messageHandler.getMessageOptional("key1", params);
        assertEquals("This is message 1 with parameter1 and parameter2", message);
    }
    
    @Test
    public void testGetMessageOptional_NonExistingKey_ReturnsNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = Arrays.asList("parameter1", "parameter2");
        String message = messageHandler.getMessageOptional("non-existing-key", params);
        assertNull(message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams_ReturnsMessageWithoutParameters() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessageOptional("key1", null);
        assertEquals("This is message 1", message);
    }
    
    @Test
    public void testGetMessageOptional_ParameterIsNull_ReturnsMessageWithoutNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = Arrays.asList(null, "parameter2");
        String message = messageHandler.getMessageOptional("key1", params);
        assertEquals("This is message 1 with <null> and parameter2", message);
    }
    
    @Test
    public void testGetInstance_NewInstance_ReturnsSameInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test");
        MessageHandler messageHandler2 = MessageHandler.getInstance("test");
        assertSame(messageHandler1, messageHandler2);
    }
    
    @Test
    public void testGetInstance_DifferentBasenames_ReturnsDifferentInstances() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test1");
        MessageHandler messageHandler2 = MessageHandler.getInstance("test2");
        assertNotSame(messageHandler1, messageHandler2);
    }
    
    // Add more test cases for edge cases and error scenarios if necessary
    
}


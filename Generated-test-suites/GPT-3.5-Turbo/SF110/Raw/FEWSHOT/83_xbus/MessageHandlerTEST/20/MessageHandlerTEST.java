package net.sf.xbus.base.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import net.sf.xbus.base.core.MessageHandler;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        assertEquals("Key: testKey not found in message file", messageHandler.getMessage("testKey", params));
        assertNull(messageHandler.getMessage("nonExistingKey", params));
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        assertNull(messageHandler.getMessageOptional(null, params));
        assertNull(messageHandler.getMessageOptional("nonExistingKey", params));
        assertNull(messageHandler.getMessageOptional("testKey", null));
        assertEquals("Value1 is: param1, Value2 is: param2", messageHandler.getMessageOptional("testKey", params));
    }

    // Add more test methods here for all methods in the MessageHandler class

}
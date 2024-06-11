package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("basename");
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        assertEquals("Key: key1 not found in message file", messageHandler.getMessage("key1", params));
        assertNull(messageHandler.getMessage("key2", params));
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        assertNull(messageHandler.getMessageOptional("key1", params));
        assertNull(messageHandler.getMessageOptional("key2", params));
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("basename");
        MessageHandler instance2 = MessageHandler.getInstance("basename");

        assertEquals(instance1, instance2);
    }
}
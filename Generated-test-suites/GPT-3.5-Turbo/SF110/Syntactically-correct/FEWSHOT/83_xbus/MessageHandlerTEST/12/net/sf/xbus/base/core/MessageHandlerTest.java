package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = new MessageHandler();
    }

    @Test
    public void testGetMessage() {
        // Prepare
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Mock
        MessageHandler mockedMessageHandler = mock(MessageHandler.class);
        when(mockedMessageHandler.getMessage(key, params)).thenReturn("Sample Message");
        
        // Verify
        assertEquals("Sample Message", mockedMessageHandler.getMessage(key, params));
    }

    @Test
    public void testGetMessageOptional() {
        // Prepare
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        // Mock
        MessageHandler mockedMessageHandler = mock(MessageHandler.class);
        when(mockedMessageHandler.getMessageOptional(key, params)).thenReturn("Sample Message Optional");

        // Verify
        assertEquals("Sample Message Optional", mockedMessageHandler.getMessageOptional(key, params));
    }

    @Test
    public void testGetInstance() {
        // Prepare
        String basename = "basename";

        // Mock
        MessageHandler mockedMessageHandler = mock(MessageHandler.class);
        when(mockedMessageHandler.getInstance(basename)).thenReturn(messageHandler);

        // Verify
        assertEquals(messageHandler, mockedMessageHandler.getInstance(basename));
    }

    @Test
    public void testAddMessages() {
        // Prepare
        String basename = "basename";
        String dir = "dir";

        // Mock
        MessageHandler mockedMessageHandler = mock(MessageHandler.class);
        doCallRealMethod().when(mockedMessageHandler).addMessages(basename, dir);

        // Verify
        assertEquals("Sample Message", mockedMessageHandler.addMessages(basename, dir));
    }
}

package net.sf.xbus.base.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("basename");
    }

    @After
    public void tearDown() {
        messageHandler = null;
    }

    @Test
    public void testGetMessageForKeyExists() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String messageText = messageHandler.getMessage("key1", params);

        assertEquals("Key: key1 not found in message file", messageText);
        verify(Trace.class, never()).error(messageText);
    }

    @Test
    public void testGetMessageForKeyNotExists() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String messageText = messageHandler.getMessage("nonexistentKey", params);

        assertEquals(null, messageText);
        verify(Trace.class, times(1)).error("Key: nonexistentKey not found in message file");
    }

    @Test
    public void testGetMessageOptionalForKeyExists() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String messageText = messageHandler.getMessageOptional("key1", params);

        assertEquals("MessageText for key1", messageText);
    }

    @Test
    public void testGetMessageOptionalForKeyNotExists() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String messageText = messageHandler.getMessageOptional("nonexistentKey", params);

        assertEquals(null, messageText);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("basename");
        assertSame(messageHandler, instance);
    }

    @Test
    public void testAddMessages() {
        messageHandler.addMessages("basename", "dir");

        // TODO: add assertions for the messages added to the mMessages hashtable
    }

    @Test
    public void testMessagesFilterAccept() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("prefix", "postfix");

        boolean accept = filter.accept(new File("dir"), "prefixfilename_postfix");

        assertTrue(accept);
    }
}

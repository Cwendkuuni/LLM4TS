package ioproject.server.network;

import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private Client client;
    private Object message;
    private Throwable cause;
    private ClientHandler handler;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptyGroup = new ClientGroup(null);
        client = new Client();
        message = new Object();
        cause = new Throwable();
        handler = new ClientHandler();
    }


    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
        assertSame(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client, message));

        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, message));
        assertTrue(handler.isMessageSentCalled());
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client, message));

        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        assertTrue(handler.isMessageReceivedCalled());
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertTrue(handler.isExceptionCaughtCalled());
    }

    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());

        parentGroup.add(client);
        assertTrue(parentGroup.iterator().hasNext());
    }

    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client));

        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());

        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
        assertTrue(handler.isClientAddedCalled());

        assertFalse(parentGroup.add(client));
    }

    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client));

        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        assertTrue(handler.isClientRemovedCalled());

        assertFalse(parentGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        assertTrue(parentGroup.createSubGroup() instanceof ClientGroup);
        assertFalse(subGroup.createSubGroup() instanceof ClientGroup);
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertSame(handler, parentGroup.handler);

        subGroup.addClientHandler(handler);
        assertSame(handler, subGroup.handler);
    }

}
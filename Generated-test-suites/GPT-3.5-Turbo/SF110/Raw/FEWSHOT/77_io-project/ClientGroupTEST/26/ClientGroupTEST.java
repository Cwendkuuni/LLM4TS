package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;

public class ClientGroupTest {
    private ClientGroup clientGroup;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();

        assertFalse(clientGroup.notifyMessageSent(client, message));

        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageSent(client, message));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();

        assertFalse(clientGroup.notifyMessageReceived(client, message));

        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageReceived(client, message));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();

        assertFalse(clientGroup.notifyExceptionCaught(client, cause));

        clientGroup.add(client);
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();

        assertEquals(0, clientGroup.size());

        clientGroup.add(client1);
        clientGroup.add(client2);

        assertTrue(clientGroup.iterator().next() == client1);
        assertTrue(clientGroup.iterator().next() == client2);
    }

    @Test
    public void testContains() {
        Client client = new Client();

        assertFalse(clientGroup.contains(client));

        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        Client client = new Client();

        assertEquals(0, clientGroup.size());

        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();

        assertFalse(clientGroup.add(client));

        clientGroup.createSubGroup().add(client);
        assertTrue(clientGroup.add(client));
    }

    @Test
    public void testRemove() {
        Client client = new Client();

        assertFalse(clientGroup.remove(client));

        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();

        assertEquals(clientGroup, subGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();

        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}
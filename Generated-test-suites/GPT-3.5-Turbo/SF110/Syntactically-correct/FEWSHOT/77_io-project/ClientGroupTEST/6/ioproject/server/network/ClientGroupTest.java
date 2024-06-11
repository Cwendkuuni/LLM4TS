package ioproject.server.network;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);

        assertEquals(parentGroup, clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();
        ClientHandler handler = new ClientHandler();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.notifyMessageSent(client, message));

        handler.verifyMessageSent(client, message);
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        ClientHandler handler = new ClientHandler();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.notifyMessageReceived(client, message));

        handler.verifyMessageReceived(client, message);
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        ClientHandler handler = new ClientHandler();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.notifyExceptionCaught(client, cause));

        handler.verifyExceptionCaught(client, cause);
    }

    @Test
    public void testIterator() {
        Client client = new Client();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);

        Iterator<Client> iterator = clientGroup.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        Client client = new Client();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);

        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        Client client = new Client();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);

        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
        ClientHandler handler = new ClientHandler();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
        handler.verifyClientAdded(client);
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        ClientHandler handler = new ClientHandler();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
        handler.verifyClientRemoved(client);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);

        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(clientGroup.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();

        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);

        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.getHandler());
    }
}

class Client {
}

class ClientHandler {
    public void messageSent(Client client, Object message) {}
    public void messageReceived(Client client, Object message) {}
    public void exceptionCaught(Client client, Throwable cause) {}
    public void clientAdded(Client client) {}
    public void clientRemoved(Client client) {}

    public void verifyMessageSent(Client client, Object message) {}
    public void verifyMessageReceived(Client client, Object message) {}
    public void verifyExceptionCaught(Client client, Throwable cause) {}
    public void verifyClientAdded(Client client) {}
    public void verifyClientRemoved(Client client) {}
}
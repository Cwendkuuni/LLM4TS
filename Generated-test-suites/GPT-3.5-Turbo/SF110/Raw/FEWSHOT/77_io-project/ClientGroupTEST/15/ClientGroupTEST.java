package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;
import ioproject.common.network.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        assertEquals(parentGroup, clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client1 = new Client();
        Client client2 = new Client();
        Object message = new Object();
        ClientGroup clientGroup = new ClientGroup(null);
        assertFalse(clientGroup.notifyMessageSent(client1, message));
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyMessageSent(client1, message));
        clientGroup.add(client2);
        ClientGroup subGroup = clientGroup.createSubGroup();
        clientGroup.add(subGroup);
        assertTrue(clientGroup.notifyMessageSent(client1, message));
        assertTrue(clientGroup.notifyMessageSent(client2, message));
        assertFalse(clientGroup.notifyMessageSent(new Client(), message));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client1 = new Client();
        Client client2 = new Client();
        Object message = new Object();
        ClientGroup clientGroup = new ClientGroup(null);
        assertFalse(clientGroup.notifyMessageReceived(client1, message));
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyMessageReceived(client1, message));
        clientGroup.add(client2);
        ClientGroup subGroup = clientGroup.createSubGroup();
        clientGroup.add(subGroup);
        assertTrue(clientGroup.notifyMessageReceived(client1, message));
        assertTrue(clientGroup.notifyMessageReceived(client2, message));
        assertFalse(clientGroup.notifyMessageReceived(new Client(), message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client1 = new Client();
        Client client2 = new Client();
        Throwable cause = new Throwable();
        ClientGroup clientGroup = new ClientGroup(null);
        assertFalse(clientGroup.notifyExceptionCaught(client1, cause));
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyExceptionCaught(client1, cause));
        clientGroup.add(client2);
        ClientGroup subGroup = clientGroup.createSubGroup();
        clientGroup.add(subGroup);
        assertTrue(clientGroup.notifyExceptionCaught(client1, cause));
        assertTrue(clientGroup.notifyExceptionCaught(client2, cause));
        assertFalse(clientGroup.notifyExceptionCaught(new Client(), cause));
    }

    @Test
    public void testIterator() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        clientGroup.add(client1);
        assertTrue(clientGroup.iterator().hasNext());
        assertEquals(client1, clientGroup.iterator().next());
    }

    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.contains(client));
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup clientGroup = new ClientGroup(null);
        assertEquals(0, clientGroup.size());
        Client client = new Client();
        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
        ClientGroup clientGroup = new ClientGroup(null);
        assertTrue(clientGroup.add(client));
        clientGroup.addClientHandler(new ClientHandler() {
            public void clientAdded(Client client) {
                // do nothing
            }
        });
        assertFalse(clientGroup.add(new Client()));
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        ClientGroup clientGroup = new ClientGroup(null);
        assertFalse(clientGroup.remove(client));
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertFalse(clientGroup.iterator().hasNext());
        assertTrue(subGroup.iterator().hasNext());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler clientHandler = new ClientHandler() {
            public void clientAdded(Client client) {
                // do nothing
            }
        };
        clientGroup.addClientHandler(clientHandler);
        assertEquals(clientHandler, clientGroup.handler);
    }
}
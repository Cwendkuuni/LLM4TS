package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private ClientHandler clientHandler;
    private Client client;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        clientHandler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}
        };
        client = new Client();
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(clientGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(clientGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());
    }

    @Test
    public void testContains() {
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client));
    }

    @Test
    public void testRemove() {
        assertFalse(clientGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertNotNull(subGroup);
    }

    @Test
    public void testAddClientHandler() {
        clientGroup.addClientHandler(clientHandler);
        assertEquals(clientHandler, clientGroup.handler);
    }
}
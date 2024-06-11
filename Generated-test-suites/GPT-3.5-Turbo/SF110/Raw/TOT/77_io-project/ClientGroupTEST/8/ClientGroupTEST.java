import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.Iterator;

class ClientGroupTest {
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyParentGroup;
    private ClientGroup emptySubGroup;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler handler;

    @BeforeEach
    void setUp() {
        parentGroup = new ClientGroup(null);
        emptyParentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptySubGroup = emptyParentGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
        handler = new ClientHandler();
    }

    @Test
    void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    void testNotifyMessageSent() {
        assertTrue(parentGroup.add(client1));

        assertFalse(parentGroup.notifyMessageSent(new Client(), "Test message"));

        assertTrue(parentGroup.notifyMessageSent(client1, "Test message"));
        assertEquals("Test message", handler.getMessageSent());

        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageSent(client1, "Test message 2"));
        assertEquals("Test message 2", handler.getMessageSent());

        assertTrue(subGroup.add(client2));
        assertTrue(parentGroup.notifyMessageSent(client2, "Test message 3"));
        assertEquals("Test message 3", handler.getMessageSent());

        assertTrue(subGroup.notifyMessageSent(client2, "Test message 4"));
        assertEquals("Test message 4", handler.getMessageSent());
    }

    @Test
    void testNotifyMessageReceived() {
        assertTrue(parentGroup.add(client1));

        assertFalse(parentGroup.notifyMessageReceived(new Client(), "Test message"));

        assertTrue(parentGroup.notifyMessageReceived(client1, "Test message"));
        assertEquals("Test message", handler.getMessageReceived());

        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageReceived(client1, "Test message 2"));
        assertEquals("Test message 2", handler.getMessageReceived());

        assertTrue(subGroup.add(client2));
        assertTrue(parentGroup.notifyMessageReceived(client2, "Test message 3"));
        assertEquals("Test message 3", handler.getMessageReceived());

        assertTrue(subGroup.notifyMessageReceived(client2, "Test message 4"));
        assertEquals("Test message 4", handler.getMessageReceived());
    }

    @Test
    void testNotifyExceptionCaught() {
        assertTrue(parentGroup.add(client1));

        assertFalse(parentGroup.notifyExceptionCaught(new Client(), new Throwable()));

        assertTrue(parentGroup.notifyExceptionCaught(client1, new Throwable()));
        assertEquals(new Throwable(), handler.getCause());

        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyExceptionCaught(client1, new Exception()));
        assertEquals(new Exception(), handler.getCause());

        assertTrue(subGroup.add(client2));
        assertTrue(parentGroup.notifyExceptionCaught(client2, new RuntimeException()));
        assertEquals(new RuntimeException(), handler.getCause());

        assertTrue(subGroup.notifyExceptionCaught(client2, new RuntimeException()));
        assertEquals(new RuntimeException(), handler.getCause());
    }

    @Test
    void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());

        parentGroup.add(client1);
        Iterator<Client> iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());

        assertTrue(subGroup.add(client2));
        iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
    }

    @Test
    void testContains() {
        parentGroup.add(client1);
        assertTrue(parentGroup.contains(client1));
        assertFalse(parentGroup.contains(client2));
    }

    @Test
    void testSize() {
        assertEquals(0, parentGroup.size());
        parentGroup.add(client1);
        assertEquals(1, parentGroup.size());
        parentGroup.add(client2);
        assertEquals(2, parentGroup.size());
        parentGroup.remove(client1);
        assertEquals(1, parentGroup.size());
    }

    @Test
    void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.contains(client1));
        assertFalse(parentGroup.add(client1));
        parentGroup.add(client2);
        assertTrue(subGroup.add(client2));
        assertTrue(subGroup.contains(client2));
        assertFalse(parentGroup.add(client2));
        assertFalse(subGroup.add(client1));
        assertFalse(parentGroup.contains(client3));
        subGroup.addClientHandler(handler);
        assertFalse(subGroup.add(client2));
        assertEquals(client2, handler.getAddedClient());
    }

    @Test
    void testRemove() {
        assertFalse(parentGroup.remove(client1));
        parentGroup.add(client1);
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.contains(client1));
        subGroup.add(client1);
        assertTrue(subGroup.remove(client1));
        assertFalse(subGroup.contains(client1));
        assertFalse(parentGroup.remove(client2));
        assertTrue(parentGroup.add(client2));
        assertTrue(parentGroup.remove(client2));
        assertFalse(parentGroup.contains(client2));
    }

    @Test
    void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
        Iterator<ClientGroup> iterator = parentGroup.subGroups.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(subGroup, iterator.next().get());
        assertNotNull(parentGroup.createSubGroup());
        iterator = parentGroup.subGroups.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(subGroup, iterator.next().get());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next().get());
    }

    @Test
    void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
        subGroup.addClientHandler(handler);
        assertEquals(handler, subGroup.handler);
        emptyParentGroup.addClientHandler(handler);
        assertEquals(handler, emptyParentGroup.handler);
        emptySubGroup.addClientHandler(handler);
        assertEquals(handler, emptySubGroup.handler);
    }

    private class Client {
    }

    private class ClientHandler implements ioproject.server.network.ClientHandler {
        private String messageSent;
        private String messageReceived;
        private Throwable cause;
        private Client addedClient;
        private Client removedClient;

        @Override
        public void messageSent(Client client, Object message) {
            this.messageSent = (String) message;
        }

        @Override
        public void messageReceived(Client client, Object message) {
            this.messageReceived = (String) message;
        }

        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            this.cause = cause;
        }

        @Override
        public void clientAdded(Client client) {
            this.addedClient = client;
        }

        @Override
        public void clientRemoved(Client client) {
            this.removedClient = client;
        }

        String getMessageSent() {
            return messageSent;
        }

        String getMessageReceived() {
            return messageReceived;
        }

        Throwable getCause() {
            return cause;
        }

        Client getAddedClient() {
            return addedClient;
        }

    }

}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private ClientGroup fullGroup;
    private TestClientHandler handler;
    private Client client;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptyGroup = new ClientGroup(null);
        fullGroup = new ClientGroup(null);
        handler = new TestClientHandler();
        client = new Client();
        fullGroup.add(client);
        fullGroup.addClientHandler(handler);
    }

    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
        assertEquals(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client, "message"));
        assertFalse(emptyGroup.notifyMessageSent(client, "message"));
        assertTrue(fullGroup.notifyMessageSent(client, "message"));
        assertTrue(subGroup.notifyMessageSent(client, "message"));
        assertEquals(client, handler.getSentClient());
        assertEquals("message", handler.getSentMessage());
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client, "message"));
        assertFalse(emptyGroup.notifyMessageReceived(client, "message"));
        assertTrue(fullGroup.notifyMessageReceived(client, "message"));
        assertTrue(subGroup.notifyMessageReceived(client, "message"));
        assertEquals(client, handler.getReceivedClient());
        assertEquals("message", handler.getReceivedMessage());
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client, new Throwable()));
        assertFalse(emptyGroup.notifyExceptionCaught(client, new Throwable()));
        assertTrue(fullGroup.notifyExceptionCaught(client, new Throwable()));
        assertTrue(subGroup.notifyExceptionCaught(client, new Throwable()));
        assertEquals(client, handler.getCaughtClient());
        assertNotNull(handler.getCaughtThrowable());
    }

    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());
        assertFalse(emptyGroup.iterator().hasNext());
        assertTrue(fullGroup.iterator().hasNext());
        assertTrue(subGroup.iterator().hasNext());
    }

    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client));
        assertFalse(emptyGroup.contains(client));
        assertTrue(fullGroup.contains(client));
        assertTrue(subGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        assertEquals(0, emptyGroup.size());
        assertEquals(1, fullGroup.size());
        assertEquals(1, subGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client));
        assertTrue(emptyGroup.add(client));
        assertFalse(fullGroup.add(client));
        assertFalse(subGroup.add(client));
        assertEquals(client, handler.getAddedClient());
    }

    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client));
        assertFalse(emptyGroup.remove(client));
        assertTrue(fullGroup.remove(client));
        assertTrue(subGroup.remove(client));
        assertEquals(client, handler.getRemovedClient());
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup sub = parentGroup.createSubGroup();
        assertNotNull(sub);
        assertEquals(parentGroup, sub.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertNotNull(parentGroup);
        assertEquals(handler, parentGroup.handler);
    }

    private static class TestClientHandler implements ClientHandler {

        private Client sentClient;
        private Object sentMessage;
        private Client receivedClient;
        private Object receivedMessage;
        private Client caughtClient;
        private Throwable caughtThrowable;
        private Client addedClient;
        private Client removedClient;

        @Override
        public void messageSent(Client client, Object message) {
            sentClient = client;
            sentMessage = message;
        }

        @Override
        public void messageReceived(Client client, Object message) {
            receivedClient = client;
            receivedMessage = message;
        }

        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            caughtClient = client;
            caughtThrowable = cause;
        }

        @Override
        public void clientAdded(Client client) {
            addedClient = client;
        }

        @Override
        public void clientRemoved(Client client) {
            removedClient = client;
        }

        public Client getSentClient() {
            return sentClient;
        }

        public Object getSentMessage() {
            return sentMessage;
        }

        public Client getReceivedClient() {
            return receivedClient;
        }

        public Object getReceivedMessage() {
            return receivedMessage;
        }

        public Client getCaughtClient() {
            return caughtClient;
        }

        public Throwable getCaughtThrowable() {
            return caughtThrowable;
        }

        public Client getAddedClient() {
            return addedClient;
        }

        public Client getRemovedClient() {
            return removedClient;
        }
    }
}
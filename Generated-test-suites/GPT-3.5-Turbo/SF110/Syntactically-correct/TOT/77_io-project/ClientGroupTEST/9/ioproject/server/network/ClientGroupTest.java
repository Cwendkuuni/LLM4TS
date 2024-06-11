
package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private ClientGroup singleClientGroup;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptyGroup = new ClientGroup(null);
        singleClientGroup = new ClientGroup(null);
        
        client = new Client();
        handler = new ClientHandler();
        
        singleClientGroup.addClientHandler(handler);
    }

    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_ClientNotInGroup() {
        assertFalse(parentGroup.notifyMessageSent(client, "Hello"));
    }

    @Test
    public void testNotifyMessageSent_ClientInSubGroup() {
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "Hello"));
        assertEquals("Hello", handler.getLastMessageSent());
    }

    @Test
    public void testNotifyMessageSent_ClientNotInSubGroupWithHandler() {
        singleClientGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "Hello"));
        assertEquals("Hello", handler.getLastMessageSent());
    }

    @Test
    public void testNotifyMessageReceived_ClientNotInGroup() {
        assertFalse(parentGroup.notifyMessageReceived(client, "Hello"));
    }

    @Test
    public void testNotifyMessageReceived_ClientInSubGroup() {
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, "Hello"));
        assertEquals("Hello", handler.getLastMessageReceived());
    }

    @Test
    public void testNotifyMessageReceived_ClientNotInSubGroupWithHandler() {
        singleClientGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, "Hello"));
        assertEquals("Hello", handler.getLastMessageReceived());
    }

    @Test
    public void testNotifyExceptionCaught_ClientNotInGroup() {
        assertFalse(parentGroup.notifyExceptionCaught(client, new Exception()));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInSubGroup() {
        subGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, new Exception()));
        assertEquals(new Exception(), handler.getLastExceptionCaught());
    }

    @Test
    public void testNotifyExceptionCaught_ClientNotInSubGroupWithHandler() {
        singleClientGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, new Exception()));
        assertEquals(new Exception(), handler.getLastExceptionCaught());
    }

    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());
        
        parentGroup.add(client);
        assertTrue(parentGroup.iterator().hasNext());
        assertEquals(client, parentGroup.iterator().next());
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
    public void testAdd_ParentContainsClient() {
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testAdd_ParentDoesNotContainClient() {
        assertFalse(emptyGroup.add(client));
        assertFalse(emptyGroup.contains(client));
    }

    @Test
    public void testRemove_ClientIsNotMember() {
        assertFalse(parentGroup.remove(client));
    }

    @Test
    public void testRemove_ClientIsMember() {
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.add(client);
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }

    private static class Client implements ioproject.common.network.Client {
        // implementation of Client interface 
    }

    private static class ClientHandler implements ioproject.common.network.ClientHandler {
        private String lastMessageSent;
        private String lastMessageReceived;
        private Throwable lastExceptionCaught;

        @Override
        public void messageSent(Client client, Object message) {
            lastMessageSent = (String) message;
        }

        @Override
        public void messageReceived(Client client, Object message) {
            lastMessageReceived = (String) message;
        }

        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            lastExceptionCaught = cause;
        }

        public String getLastMessageSent() {
            return lastMessageSent;
        }

        public String getLastMessageReceived() {
            return lastMessageReceived;
        }

        public Throwable getLastExceptionCaught() {
            return lastExceptionCaught;
        }
    }
}

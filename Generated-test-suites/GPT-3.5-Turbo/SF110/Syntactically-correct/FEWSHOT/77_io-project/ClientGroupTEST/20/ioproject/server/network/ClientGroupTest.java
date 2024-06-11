package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private ClientHandler handler;
    private Client client1;
    private Client client2;
    private Client client3;

    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptyGroup = new ClientGroup(null);
        handler = new ClientHandler();
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
    }

    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
        assertEquals(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client1, "message"));
        assertFalse(emptyGroup.notifyMessageSent(client1, "message"));

        parentGroup.add(client1);
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageSent(client1, "message"));
        assertEquals("message", handler.getMessageSent());

        parentGroup.add(client2);
        subGroup.add(client3);
        subGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageSent(client2, "message2"));
        assertNull(handler.getMessageSent());
        assertTrue(subGroup.notifyMessageSent(client3, "message3"));
        assertEquals("message3", handler.getMessageSent());
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client1, "message"));
        assertFalse(emptyGroup.notifyMessageReceived(client1, "message"));

        parentGroup.add(client1);
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageReceived(client1, "message"));
        assertEquals("message", handler.getMessageReceived());

        parentGroup.add(client2);
        subGroup.add(client3);
        subGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageReceived(client2, "message2"));
        assertNull(handler.getMessageReceived());
        assertTrue(subGroup.notifyMessageReceived(client3, "message3"));
        assertEquals("message3", handler.getMessageReceived());
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client1, new Throwable()));
        assertFalse(emptyGroup.notifyExceptionCaught(client1, new Throwable()));

        parentGroup.add(client1);
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyExceptionCaught(client1, new Throwable()));
        assertEquals(client1, handler.getExceptionCaughtClient());

        parentGroup.add(client2);
        subGroup.add(client3);
        subGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyExceptionCaught(client2, new Throwable()));
        assertNull(handler.getExceptionCaughtClient());
        assertTrue(subGroup.notifyExceptionCaught(client3, new Throwable()));
        assertEquals(client3, handler.getExceptionCaughtClient());
    }

    @Test
    public void testIterator() {
        Iterator<Client> iterator = parentGroup.iterator();
        assertFalse(iterator.hasNext());

        parentGroup.add(client1);
        parentGroup.add(client2);
        iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());

        subGroup.add(client3);
        iterator = subGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client1));

        parentGroup.add(client1);
        parentGroup.add(client2);
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
        assertFalse(parentGroup.contains(client3));

        subGroup.add(client3);
        assertTrue(subGroup.contains(client3));
        assertTrue(parentGroup.contains(client3));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());

        parentGroup.add(client1);
        parentGroup.add(client2);
        assertEquals(2, parentGroup.size());

        subGroup.add(client3);
        assertEquals(3, parentGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.contains(client1));

        assertFalse(emptyGroup.add(client1));
        assertFalse(emptyGroup.contains(client1));

        parentGroup.add(client2);
        subGroup.add(client3);
        assertFalse(subGroup.add(client1));
        assertFalse(parentGroup.contains(client1));
        assertTrue(subGroup.add(client2));
        assertTrue(parentGroup.contains(client2));
        assertTrue(subGroup.add(client3));
        assertTrue(parentGroup.contains(client3));
    }

    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client1));

        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.contains(client1));
        assertFalse(parentGroup.remove(client1));
        assertTrue(parentGroup.contains(client2));
        assertTrue(parentGroup.contains(client3));

        subGroup.add(client2);
        subGroup.add(client3);
        subGroup.remove(client2);
        assertFalse(subGroup.contains(client2));
        assertFalse(parentGroup.contains(client2));
        assertTrue(subGroup.remove(client3));
        assertFalse(subGroup.contains(client3));
        assertFalse(parentGroup.contains(client3));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        assertNotEquals(subGroup, newSubGroup);
        assertTrue(subGroup.contains(newSubGroup));

        ClientGroup subGroup2 = subGroup.createSubGroup();
        assertNotEquals(subGroup, subGroup2);
        assertFalse(subGroup.contains(subGroup2));
    }

    @Test
    public void testAddClientHandler() {
        assertNull(parentGroup.handler);

        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);

        subGroup.addClientHandler(handler);
        assertEquals(handler, subGroup.handler);
    }

    private static class ClientHandler implements ioproject.common.network.ClientHandler {
  
        private String messageSent;
        private String messageReceived;
        private Client exceptionCaughtClient;
        
        @Override
        public void messageSent(Client client, Object message) {
            messageSent = (String) message;
        }
        
        @Override
        public void messageReceived(Client client, Object message) {
            messageReceived = (String) message;
        }
        
        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtClient = client;
        }
        
        public String getMessageSent() {
            return messageSent;
        }
        
        public String getMessageReceived() {
            return messageReceived;
        }
        
        public Client getExceptionCaughtClient() {
            return exceptionCaughtClient;
        }
    }

    private static class Client {
        //Sample implementation for testing purposes
    }
}
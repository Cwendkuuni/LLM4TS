package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Iterator;
import ioproject.common.network.Client;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        // Test ClientGroup with null parent group
        ClientGroup clientGroup1 = new ClientGroup(null);
        assertNull(clientGroup1.parentGroup());

        // Test ClientGroup with non-null parent group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup2 = new ClientGroup(parentGroup);
        assertEquals(parentGroup, clientGroup2.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Setup ClientGroup with client and handler
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        clientGroup.add(client);
        MockClientHandler handler = new MockClientHandler();
        clientGroup.addClientHandler(handler);

        // Test notifyMessageSent with valid client and message
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageSent(client, message));
        assertEquals(client, handler.getMessageSentClient());
        assertEquals(message, handler.getMessageSentMessage());

        // Test notifyMessageSent with invalid client
        Client invalidClient = new Client();
        assertFalse(clientGroup.notifyMessageSent(invalidClient, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        // Setup ClientGroup with client and handler
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        clientGroup.add(client);
        MockClientHandler handler = new MockClientHandler();
        clientGroup.addClientHandler(handler);

        // Test notifyMessageReceived with valid client and message
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        assertEquals(client, handler.getMessageReceivedClient());
        assertEquals(message, handler.getMessageReceivedMessage());

        // Test notifyMessageReceived with invalid client
        Client invalidClient = new Client();
        assertFalse(clientGroup.notifyMessageReceived(invalidClient, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Setup ClientGroup with client and handler
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        clientGroup.add(client);
        MockClientHandler handler = new MockClientHandler();
        clientGroup.addClientHandler(handler);

        // Test notifyExceptionCaught with valid client and exception
        Throwable exception = new Throwable();
        assertTrue(clientGroup.notifyExceptionCaught(client, exception));
        assertEquals(client, handler.getExceptionCaughtClient());
        assertEquals(exception, handler.getExceptionCaughtCause());

        // Test notifyExceptionCaught with invalid client
        Client invalidClient = new Client();
        assertFalse(clientGroup.notifyExceptionCaught(invalidClient, exception));
    }

    @Test
    public void testIterator() {
        // Setup ClientGroup with clients
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client("client1");
        Client client2 = new Client("client2");
        clientGroup.add(client1);
        clientGroup.add(client2);

        // Test iterator
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        // Setup ClientGroup with clients
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client("client1");
        clientGroup.add(client1);

        // Test contains with valid client
        assertTrue(clientGroup.contains(client1));

        // Test contains with invalid client
        Client invalidClient = new Client("invalidClient");
        assertFalse(clientGroup.contains(invalidClient));
    }

    @Test
    public void testSize() {
        // Setup ClientGroup with clients
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client("client1");
        Client client2 = new Client("client2");
        clientGroup.add(client1);
        clientGroup.add(client2);

        // Test size
        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testAdd() {
        // Setup ClientGroup with parent group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();

        // Test add with client that is not in parent group
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));

        // Test add with client that is in parent group
        parentGroup.add(client);
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));

        // Test add with handler
        MockClientHandler handler = new MockClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(client, handler.getClientAddedClient());
    }

    @Test
    public void testRemove() {
        // Setup ClientGroup with client and sub group
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client("client1");
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client2 = new Client("client2");
        subGroup.add(client2);
        clientGroup.add(client1);
        subGroup.add(client1);

        // Test remove with client that is a member
        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.contains(client1));
        assertFalse(subGroup.contains(client1));

        // Test remove with client that is not a member
        Client invalidClient = new Client("invalidClient");
        assertFalse(clientGroup.remove(invalidClient));

        // Test remove with handler
        MockClientHandler handler = new MockClientHandler();
        clientGroup.addClientHandler(handler);
        clientGroup.remove(client2);
        assertEquals(client2, handler.getClientRemovedClient());
    }

    @Test
    public void testCreateSubGroup() {
        // Setup ClientGroup
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);

        // Test createSubGroup
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(clientGroup.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        // Setup ClientGroup
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler handler = new MockClientHandler();

        // Test addClientHandler
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.getHandler());
    }
    
    // Create mock class for ClientHandler
    private class MockClientHandler implements ClientHandler {
        private Client messageSentClient;
        private Object messageSentMessage;
        private Client messageReceivedClient;
        private Object messageReceivedMessage;
        private Client exceptionCaughtClient;
        private Throwable exceptionCaughtCause;
        private Client clientAddedClient;
        private Client clientRemovedClient;
        
        public Client getMessageSentClient() {
            return messageSentClient;
        }
        
        public Object getMessageSentMessage() {
            return messageSentMessage;
        }
        
        public Client getMessageReceivedClient() {
            return messageReceivedClient;
        }
        
        public Object getMessageReceivedMessage() {
            return messageReceivedMessage;
        }
        
        public Client getExceptionCaughtClient() {
            return exceptionCaughtClient;
        }
        
        public Throwable getExceptionCaughtCause() {
            return exceptionCaughtCause;
        }
        
        public Client getClientAddedClient() {
            return clientAddedClient;
        }
        
        public Client getClientRemovedClient() {
            return clientRemovedClient;
        }
        
        @Override
        public void messageSent(Client client, Object message) {
            messageSentClient = client;
            messageSentMessage = message;
        }
        
        @Override
        public void messageReceived(Client client, Object message) {
            messageReceivedClient = client;
            messageReceivedMessage = message;
        }
        
        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtClient = client;
            exceptionCaughtCause = cause;
        }
        
        @Override
        public void clientAdded(Client client) {
            clientAddedClient = client;
        }
        
        @Override
        public void clientRemoved(Client client) {
            clientRemovedClient = client;
        }
    }
}
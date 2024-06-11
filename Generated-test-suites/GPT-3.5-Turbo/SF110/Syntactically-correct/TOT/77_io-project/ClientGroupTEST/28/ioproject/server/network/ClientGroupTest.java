import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        // Test to verify if parent group is returned correctly
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent_WhenClientExistsAndNoSubGroups() {
        // Test to verify if message is sent to handler when no subgroups exist
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        Client client = new Client();
        
        clientGroup.addClientHandler(handlerMock);
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "Hello"));
        assertEquals("Hello", handlerMock.getLastMessageSent());
        assertEquals(client, handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyMessageSent_WhenClientExistsInSubGroup() {
        // Test to verify if message is sent to handler when client exists in a sub group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        
        parentGroup.addClientHandler(handlerMock);
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "Hello"));
        assertEquals("Hello", handlerMock.getLastMessageSent());
        assertEquals(client, handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyMessageSent_WhenClientDoesNotExist() {
        // Test to verify if message is not sent when client does not exist
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        Client client = new Client();
        
        clientGroup.addClientHandler(handlerMock);
        assertFalse(clientGroup.notifyMessageSent(client, "Hello"));
        assertNull(handlerMock.getLastMessageSent());
        assertNull(handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyMessageReceived_WhenClientExistsAndNoSubGroups() {
        // Test to verify if message is received by handler when no subgroups exist
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        Client client = new Client();
        
        clientGroup.addClientHandler(handlerMock);
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, "Hello"));
        assertEquals("Hello", handlerMock.getLastMessageReceived());
        assertEquals(client, handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyMessageReceived_WhenClientExistsInSubGroup() {
        // Test to verify if message is received by handler when client exists in a sub group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        
        parentGroup.addClientHandler(handlerMock);
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, "Hello"));
        assertEquals("Hello", handlerMock.getLastMessageReceived());
        assertEquals(client, handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyMessageReceived_WhenClientDoesNotExist() {
        // Test to verify if message is not received when client does not exist
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        Client client = new Client();
        
        clientGroup.addClientHandler(handlerMock);
        assertFalse(clientGroup.notifyMessageReceived(client, "Hello"));
        assertNull(handlerMock.getLastMessageReceived());
        assertNull(handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyExceptionCaught_WhenClientExistsAndNoSubGroups() {
        // Test to verify if exception is caught by handler when no subgroups exist
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        Client client = new Client();
        Throwable cause = new Throwable();
        
        clientGroup.addClientHandler(handlerMock);
        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
        assertEquals(cause, handlerMock.getLastExceptionCaught());
        assertEquals(client, handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyExceptionCaught_WhenClientExistsInSubGroup() {
        // Test to verify if exception is caught by handler when client exists in a sub group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        Throwable cause = new Throwable();
        
        parentGroup.addClientHandler(handlerMock);
        subGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertEquals(cause, handlerMock.getLastExceptionCaught());
        assertEquals(client, handlerMock.getLastClient());
    }
    
    @Test
    public void testNotifyExceptionCaught_WhenClientDoesNotExist() {
        // Test to verify if exception is not caught when client does not exist
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        Client client = new Client();
        Throwable cause = new Throwable();
        
        clientGroup.addClientHandler(handlerMock);
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        assertNull(handlerMock.getLastExceptionCaught());
        assertNull(handlerMock.getLastClient());
    }
    
    @Test
    public void testIterator() {
        // Test to verify if the iterator returns the correct clients in the client group
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains_WhenClientExists() {
        // Test to verify if contains returns true when client exists in the group
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testContains_WhenClientDoesNotExist() {
        // Test to verify if contains returns false when client does not exist in the group
        ClientGroup clientGroup = new ClientGroup(null);
        assertFalse(clientGroup.contains(new Client()));
    }
    
    @Test
    public void testSize() {
        // Test to verify if size returns the correct number of clients in the group
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertEquals(2, clientGroup.size());
    }
    
    @Test
    public void testAdd_WhenParentContainsClient() {
        // Test to verify if client is added when the parent group contains the client
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        ClientHandlerMock handlerMockParent = new ClientHandlerMock();
        ClientHandlerMock handlerMockChild = new ClientHandlerMock();
        Client client = new Client();
        
        parentGroup.addClientHandler(handlerMockParent);
        childGroup.addClientHandler(handlerMockChild);
        parentGroup.add(client);
        
        assertTrue(childGroup.add(client));
        assertTrue(childGroup.contains(client));
        assertTrue(handlerMockChild.isClientAddedCalled());
        assertFalse(handlerMockParent.isClientAddedCalled());
        assertEquals(client, handlerMockChild.getLastClient());
    }
    
    @Test
    public void testAdd_WhenParentDoesNotContainClient() {
        // Test to verify if client is not added when the parent group does not contain the client
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        ClientHandlerMock handlerMockParent = new ClientHandlerMock();
        ClientHandlerMock handlerMockChild = new ClientHandlerMock();
        Client client = new Client();
        Client clientParent = new Client();
        
        parentGroup.addClientHandler(handlerMockParent);
        childGroup.addClientHandler(handlerMockChild);
        parentGroup.add(clientParent);
        
        assertFalse(childGroup.add(client));
        assertFalse(childGroup.contains(client));
        assertFalse(handlerMockChild.isClientAddedCalled());
        assertFalse(handlerMockParent.isClientAddedCalled());
    }
    
    @Test
    public void testRemove_WhenClientIsMember() {
        // Test to verify if client is removed when it is a member of the group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        ClientHandlerMock handlerMockParent = new ClientHandlerMock();
        ClientHandlerMock handlerMockSub = new ClientHandlerMock();
        Client client = new Client();
        
        parentGroup.addClientHandler(handlerMockParent);
        subGroup.addClientHandler(handlerMockSub);
        parentGroup.add(client);
        
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        assertFalse(subGroup.contains(client));
        assertTrue(handlerMockParent.isClientRemovedCalled());
        assertFalse(handlerMockSub.isClientRemovedCalled());
        assertEquals(client, handlerMockParent.getLastClient());
    }
    
    @Test
    public void testRemove_WhenClientIsNotMember() {
        // Test to verify if client is not removed when it is not a member of the group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        ClientHandlerMock handlerMockParent = new ClientHandlerMock();
        ClientHandlerMock handlerMockSub = new ClientHandlerMock();
        Client client = new Client();
        
        parentGroup.addClientHandler(handlerMockParent);
        subGroup.addClientHandler(handlerMockSub);
        parentGroup.add(client);
        
        assertFalse(subGroup.remove(client));
        assertTrue(parentGroup.contains(client));
        assertFalse(subGroup.contains(client));
        assertFalse(handlerMockParent.isClientRemovedCalled());
        assertFalse(handlerMockSub.isClientRemovedCalled());
    }
    
    @Test
    public void testCreateSubGroup() {
        // Test to verify if a new sub group is created when createSubGroup method is called
        ClientGroup parentGroup = new ClientGroup(null);
        
        assertNull(parentGroup.parentGroup());
        
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertEquals(parentGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        // Test to verify if client handler is set correctly
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        
        assertNull(clientGroup.handler);
        
        clientGroup.addClientHandler(handlerMock);
        
        assertEquals(handlerMock, clientGroup.handler);
    }
    
    class ClientHandlerMock implements ClientHandler {
        
        private String lastMessageSent;
        private String lastMessageReceived;
        private Throwable lastExceptionCaught;
        private Client lastClient;
        private boolean isClientAddedCalled;
        private boolean isClientRemovedCalled;
        
        public String getLastMessageSent() {
            return lastMessageSent;
        }
        
        public String getLastMessageReceived() {
            return lastMessageReceived;
        }
        
        public Throwable getLastExceptionCaught() {
            return lastExceptionCaught;
        }
        
        public Client getLastClient() {
            return lastClient;
        }
        
        public boolean isClientAddedCalled() {
            return isClientAddedCalled;
        }
        
        public boolean isClientRemovedCalled() {
            return isClientRemovedCalled;
        }
        
        @Override
        public void messageSent(Client client, Object message) {
            this.lastMessageSent = (String) message;
            this.lastClient = client;
        }
        
        @Override
        public void messageReceived(Client client, Object message) {
            this.lastMessageReceived = (String) message;
            this.lastClient = client;
        }
        
        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            this.lastExceptionCaught = cause;
            this.lastClient = client;
        }
        
        @Override
        public void clientAdded(Client client) {
            this.isClientAddedCalled = true;
            this.lastClient = client;
        }
        
        @Override
        public void clientRemoved(Client client) {
            this.isClientRemovedCalled = true;
            this.lastClient = client;
        }
    }
}
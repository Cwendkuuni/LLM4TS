package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;

public class ClientGroupTest {
    
    private ClientGroup clientGroup;
    private Client client;
    
    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        client = new Client();
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_WhenClientNotInGroup_ShouldReturnFalse() {
        boolean result = clientGroup.notifyMessageSent(client, new Object());
        assertFalse(result);
    }

    @Test
    public void testNotifyMessageSent_WhenClientInGroupButNotInSubGroup_ShouldCallHandlerMessageSent() {
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);

        boolean result = clientGroup.notifyMessageSent(client, new Object());
        assertTrue(result);
        assertTrue(handler.messageSentCalled);
    }

    @Test
    public void testNotifyMessageSent_WhenClientInSubGroup_ShouldCallHandlerMessageSent() {
        clientGroup.add(client);
        ClientGroup subGroup = clientGroup.createSubGroup();
        ClientHandler handler = new ClientHandler();
        subGroup.addClientHandler(handler);

        boolean result = clientGroup.notifyMessageSent(client, new Object());
        assertTrue(result);
        assertTrue(handler.messageSentCalled);
    }

    @Test
    public void testNotifyMessageReceived_WhenClientNotInGroup_ShouldReturnFalse() {
        boolean result = clientGroup.notifyMessageReceived(client, new Object());
        assertFalse(result);
    }

    @Test
    public void testNotifyMessageReceived_WhenClientInGroupButNotInSubGroup_ShouldCallHandlerMessageReceived() {
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);

        boolean result = clientGroup.notifyMessageReceived(client, new Object());
        assertTrue(result);
        assertTrue(handler.messageReceivedCalled);
    }

    @Test
    public void testNotifyMessageReceived_WhenClientInSubGroup_ShouldCallHandlerMessageReceived() {
        clientGroup.add(client);
        ClientGroup subGroup = clientGroup.createSubGroup();
        ClientHandler handler = new ClientHandler();
        subGroup.addClientHandler(handler);

        boolean result = clientGroup.notifyMessageReceived(client, new Object());
        assertTrue(result);
        assertTrue(handler.messageReceivedCalled);
    }

    @Test
    public void testNotifyExceptionCaught_WhenClientNotInGroup_ShouldReturnFalse() {
        boolean result = clientGroup.notifyExceptionCaught(client, new Throwable());
        assertFalse(result);
    }

    @Test
    public void testNotifyExceptionCaught_WhenClientInGroupButNotInSubGroup_ShouldCallHandlerExceptionCaught() {
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);

        boolean result = clientGroup.notifyExceptionCaught(client, new Throwable());
        assertTrue(result);
        assertTrue(handler.exceptionCaughtCalled);
    }

    @Test
    public void testNotifyExceptionCaught_WhenClientInSubGroup_ShouldCallHandlerExceptionCaught() {
        clientGroup.add(client);
        ClientGroup subGroup = clientGroup.createSubGroup();
        ClientHandler handler = new ClientHandler();
        subGroup.addClientHandler(handler);

        boolean result = clientGroup.notifyExceptionCaught(client, new Throwable());
        assertTrue(result);
        assertTrue(handler.exceptionCaughtCalled);
    }

    @Test
    public void testIterator() {
        clientGroup.add(client);
        assertEquals(client, clientGroup.iterator().next());
    }

    @Test
    public void testContains_WhenClientNotInGroup_ShouldReturnFalse() {
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testContains_WhenClientInGroup_ShouldReturnTrue() {
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize_WhenNoClientsInGroup_ShouldReturnZero() {
        assertEquals(0, clientGroup.size());
    }

    @Test
    public void testSize_WhenOneClientInGroup_ShouldReturnOne() {
        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd_WhenParentGroupIsNull_ShouldAddClientToGroup() {
        boolean result = clientGroup.add(client);
        assertTrue(result);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testAdd_WhenParentGroupIsNotNullAndContainsClient_ShouldAddClientToGroup() {
        ClientGroup parentGroup = new ClientGroup(clientGroup);
        parentGroup.add(client);
        clientGroup.addClientHandler(new ClientHandler(parentGroup));

        boolean result = clientGroup.add(client);
        assertTrue(result);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testAdd_WhenParentGroupIsNotNullAndDoesNotContainClient_ShouldNotAddClientToGroup() {
        ClientGroup parentGroup = new ClientGroup(clientGroup);
        parentGroup.addClientHandler(new ClientHandler(parentGroup));

        boolean result = clientGroup.add(client);
        assertFalse(result);
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testRemove_WhenClientNotInGroup_ShouldReturnFalse() {
        boolean result = clientGroup.remove(client);
        assertFalse(result);
    }

    @Test
    public void testRemove_WhenClientIsMember_ShouldRemoveClientFromGroupAndSubgroups() {
        clientGroup.add(client);
        ClientGroup subGroup = clientGroup.createSubGroup();
        ClientHandler handler = new ClientHandler(subGroup);
        subGroup.addClientHandler(handler);

        boolean result = clientGroup.remove(client);
        assertTrue(result);
        assertFalse(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));
    }

    @Test
    public void testRemove_WhenClientIsMember_ShouldCallHandlerClientRemoved() {
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);

        boolean result = clientGroup.remove(client);
        assertTrue(result);
        assertTrue(handler.clientRemovedCalled);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(clientGroup.iterator().next().equals(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
    
    private class ClientHandler {
        
        private boolean messageSentCalled;
        private boolean messageReceivedCalled;
        private boolean exceptionCaughtCalled;
        private boolean clientAddedCalled;
        private boolean clientRemovedCalled;

        public ClientHandler() {
        }

        public ClientHandler(ClientGroup group) {
            group.addClientHandler(this);
        }
        
        public void messageSent(Client client, Object message) {
            messageSentCalled = true;
        }

        public void messageReceived(Client client, Object message) {
            messageReceivedCalled = true;
        }

        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtCalled = true;
        }

        public void clientAdded(Client client) {
            clientAddedCalled = true;
        }

        public void clientRemoved(Client client) {
            clientRemovedCalled = true;
        }
    }

}
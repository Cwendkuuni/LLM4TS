import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private ClientGroup subGroup;
    private Client client;
    private Object message;
    private Throwable cause;
    private ClientHandler handler;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        subGroup = clientGroup.createSubGroup();
        client = new Client();
        message = new Object();
        cause = new Throwable();
        handler = new ClientHandler();
    }

    @Test
    public void testParentGroup() {
        assertEquals(null, clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_NotInGroup_ReturnsFalse() {
        assertFalse(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_InSubGroup_ReturnsTrue() {
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_NotInSubGroup_HasHandler_ReturnsTrue() {
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_NotInSubGroup_NoHandler_ReturnsFalse() {
        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived_NotInGroup_ReturnsFalse() {
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_InSubGroup_ReturnsTrue() {
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_NotInSubGroup_HasHandler_ReturnsTrue() {
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_NotInSubGroup_NoHandler_ReturnsFalse() {
        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught_NotInGroup_ReturnsFalse() {
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_InSubGroup_ReturnsTrue() {
        subGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_NotInSubGroup_HasHandler_ReturnsTrue() {
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_NotInSubGroup_NoHandler_ReturnsFalse() {
        clientGroup.add(client);
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        clientGroup.add(client);
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains_ContainsClient_ReturnsTrue() {
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testContains_DoesNotContainClient_ReturnsFalse() {
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd_ParentContainsClient_ReturnsTrue() {
        assertTrue(clientGroup.add(client));
    }

    @Test
    public void testAdd_ParentDoesNotContainClient_ReturnsFalse() {
        clientGroup.add(subGroup);
        assertFalse(clientGroup.add(client));
    }

    @Test
    public void testAdd_HasHandler() {
        clientGroup.addClientHandler(handler);
        clientGroup.add(client);
        assertTrue(handler.clientAddedCalled);
    }

    @Test
    public void testAdd_NoHandler() {
        clientGroup.add(client);
        assertFalse(handler.clientAddedCalled);
    }

    @Test
    public void testRemove_ClientIsMember_ReturnsTrue() {
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
    }

    @Test
    public void testRemove_ClientIsNotMember_ReturnsFalse() {
        assertFalse(clientGroup.remove(client));
    }

    @Test
    public void testRemove_HasHandler() {
        clientGroup.addClientHandler(handler);
        clientGroup.add(client);
        clientGroup.remove(client);
        assertTrue(handler.clientRemovedCalled);
    }

    @Test
    public void testRemove_NoHandler() {
        clientGroup.add(client);
        clientGroup.remove(client);
        assertFalse(handler.clientRemovedCalled);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertNotNull(subGroup);
        assertEquals(clientGroup, subGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }

    private class Client {}
    private class ClientHandler {
        boolean clientAddedCalled;
        boolean clientRemovedCalled;
        void clientAdded(Client client) {
            clientAddedCalled = true;
        }
        void clientRemoved(Client client) {
            clientRemovedCalled = true;
        }
    }
}
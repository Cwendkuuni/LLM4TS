import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private ClientGroup nullParentGroup;
    private ClientGroup nullClientGroup;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = new ClientGroup(parentGroup);
        emptyGroup = new ClientGroup(null);
        nullParentGroup = null;
        nullClientGroup = new ClientGroup(null);
        nullClientGroup.add(null);
    }
    
    @Test
    public void testParentGroupNotNull() {
        assertNotNull(subGroup.parentGroup());
    }
    
    @Test
    public void testParentGroupIsNull() {
        assertNull(parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSentInSubGroup() {
        Client client = new Client();
        Object message = new Object();
        subGroup.add(client);
        assertTrue(subGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageSentNotInSubGroup() {
        Client client = new Client();
        Object message = new Object();
        assertFalse(parentGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageSentInSubGroupWithHandler() {
        Client client = new Client();
        Object message = new Object();
        parentGroup.add(client);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        parentGroup.createSubGroup();

        assertTrue(parentGroup.notifyMessageSent(client, message));
        assertEquals(1, handlerMock.messageSentCalls);
    }
    
    @Test
    public void testNotifyMessageReceivedInSubGroup() {
        Client client = new Client();
        Object message = new Object();
        subGroup.add(client);
        assertTrue(subGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyMessageReceivedNotInSubGroup() {
        Client client = new Client();
        Object message = new Object();
        assertFalse(parentGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyMessageReceivedInSubGroupWithHandler() {
        Client client = new Client();
        Object message = new Object();
        parentGroup.add(client);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        parentGroup.createSubGroup();
        
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        assertEquals(1, handlerMock.messageReceivedCalls);
    }
    
    @Test
    public void testNotifyExceptionCaughtInSubGroup() {
        Client client = new Client();
        Throwable cause = new Throwable();
        subGroup.add(client);
        assertTrue(subGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testNotifyExceptionCaughtNotInSubGroup() {
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testNotifyExceptionCaughtInSubGroupWithHandler() {
        Client client = new Client();
        Throwable cause = new Throwable();
        parentGroup.add(client);
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        parentGroup.createSubGroup();
        
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertEquals(1, handlerMock.exceptionCaughtCalls);
    }
    
    @Test
    public void testIteratorNotEmpty() {
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.iterator().hasNext());
    }
    
    @Test
    public void testIteratorEmpty() {
        assertFalse(emptyGroup.iterator().hasNext());
    }
    
    @Test
    public void testContainsTrue() {
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testContainsFalse() {
        Client client = new Client();
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        Client client = new Client();
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAddWithParentGroup() {
        ClientGroup subParentGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        subParentGroup.add(client);
        assertTrue(subParentGroup.contains(client));
    }
    
    @Test
    public void testAddWithoutParentGroup() {
        Client client = new Client();
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        assertEquals(handlerMock, parentGroup.handler);
    }
    
    @Test
    public void testRemoveExistingClient() {
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testRemoveNonExistingClient() {
        Client client = new Client();
        assertFalse(parentGroup.remove(client));
    }
    
    @Test
    public void testRemoveClientWithSubGroups() {
        Client client = new Client();
        parentGroup.add(client);
        ClientGroup subParentGroup = new ClientGroup(parentGroup);
        subParentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(subParentGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddNull() {
        assertFalse(nullClientGroup.add(null));
    }
    
    @Test
    public void testAddNullWithoutParentGroup() {
        assertFalse(emptyGroup.add(null));
    }
    
    class ClientHandlerMock implements ClientHandler {
        
        public int messageSentCalls;
        public int messageReceivedCalls;
        public int exceptionCaughtCalls;
        
        public void messageSent(Client client, Object message) {
            messageSentCalls++;
        }
        
        public void messageReceived(Client client, Object message) {
            messageReceivedCalls++;
        }
        
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtCalls++;
        }
        
        public void clientAdded(Client client) {
            
        }
        
        public void clientRemoved(Client client) {
            
        }
        
    }

}
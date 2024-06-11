import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        
        assertSame(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent_WithHandler() {
        Client client = new Client();
        Object message = new Object();
        
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(handler);
        
        assertTrue(clientGroup.notifyMessageSent(client, message));
        assertTrue(handler.isMessageSentCalled);
        assertSame(client, handler.sentClient);
        assertSame(message, handler.sentMessage);
    }
    
    @Test
    public void testNotifyMessageSent_WithSubGroup() {
        Client client = new Client();
        Object message = new Object();
        
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup1 = parentGroup.createSubGroup();
        ClientGroup subGroup2 = parentGroup.createSubGroup();
        
        subGroup1.add(client);
        
        assertTrue(parentGroup.notifyMessageSent(client, message));
        assertFalse(subGroup2.handler.isMessageSentCalled);
        assertTrue(subGroup1.handler.isMessageSentCalled);
        assertSame(client, subGroup1.handler.sentClient);
        assertSame(message, subGroup1.handler.sentMessage);
    }
    
    @Test
    public void testNotifyMessageSent_ClientNotInGroup() {
        Client client = new Client();
        Object message = new Object();
        
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(handler);
        
        assertFalse(clientGroup.notifyMessageSent(client, message));
        assertFalse(handler.isMessageSentCalled);
    }
    
    @Test
    public void testNotifyMessageReceived_WithHandler() {
        Client client = new Client();
        Object message = new Object();
        
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(handler);
        
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        assertTrue(handler.isMessageReceivedCalled);
        assertSame(client, handler.receivedClient);
        assertSame(message, handler.receivedMessage);
    }
    
    @Test
    public void testNotifyMessageReceived_WithSubGroup() {
        Client client = new Client();
        Object message = new Object();
        
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup1 = parentGroup.createSubGroup();
        ClientGroup subGroup2 = parentGroup.createSubGroup();
        
        subGroup1.add(client);
        
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        assertFalse(subGroup2.handler.isMessageReceivedCalled);
        assertTrue(subGroup1.handler.isMessageReceivedCalled);
        assertSame(client, subGroup1.handler.receivedClient);
        assertSame(message, subGroup1.handler.receivedMessage);
    }
    
    @Test
    public void testNotifyMessageReceived_ClientNotInGroup() {
        Client client = new Client();
        Object message = new Object();
        
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(handler);
        
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        assertFalse(handler.isMessageReceivedCalled);
    }
    
    @Test
    public void testNotifyExceptionCaught_WithHandler() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(handler);
        
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
        assertTrue(handler.isExceptionCaughtCalled);
        assertSame(client, handler.caughtClient);
        assertSame(cause, handler.caughtCause);
    }
    
    @Test
    public void testNotifyExceptionCaught_WithSubGroup() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup1 = parentGroup.createSubGroup();
        ClientGroup subGroup2 = parentGroup.createSubGroup();
        
        subGroup1.add(client);
        
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertFalse(subGroup2.handler.isExceptionCaughtCalled);
        assertTrue(subGroup1.handler.isExceptionCaughtCalled);
        assertSame(client, subGroup1.handler.caughtClient);
        assertSame(cause, subGroup1.handler.caughtCause);
    }
    
    @Test
    public void testNotifyExceptionCaught_ClientNotInGroup() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(handler);
        
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        assertFalse(handler.isExceptionCaughtCalled);
    }
    
    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        int count = 0;
        for (Client client : clientGroup) {
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    public void testContains_ClientInGroup() {
        Client client = new Client();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.add(client);
        
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testContains_ClientNotInGroup() {
        Client client = new Client();
        ClientGroup clientGroup = new ClientGroup(null);
        
        assertFalse(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        Client client1 = new Client();
        Client client2 = new Client();
        ClientGroup clientGroup = new ClientGroup(null);
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertEquals(2, clientGroup.size());
    }
    
    @Test
    public void testAdd_ClientInParentGroup() {
        Client client = new Client();
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        
        assertTrue(childGroup.add(client));
        assertTrue(childGroup.contains(client));
    }
    
    @Test
    public void testAdd_ClientInDifferentGroup() {
        Client client = new Client();
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup otherGroup = new ClientGroup(null);
        
        assertFalse(otherGroup.add(client));
        assertFalse(otherGroup.contains(client));
    }
    
    @Test
    public void testRemove_ClientInGroup() {
        Client client = new Client();
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        subGroup.add(client);
        
        assertTrue(subGroup.remove(client));
        assertFalse(subGroup.contains(client));
    }
    
    @Test
    public void testRemove_ClientNotInGroup() {
        Client client = new Client();
        ClientGroup parentGroup = new ClientGroup(null);
        
        assertFalse(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertNotNull(subGroup);
        assertTrue(subGroup.parentGroup() instanceof ClientGroup);
        assertSame(parentGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        ClientGroup clientGroup = new ClientGroup(null);
        
        clientGroup.addClientHandler(handler);
        
        assertSame(handler, clientGroup.handler);
    }
}

class Client {
    // Implementation of Client class goes here...
}

class ClientHandler {
    boolean isMessageSentCalled;
    Client sentClient;
    Object sentMessage;
    
    boolean isMessageReceivedCalled;
    Client receivedClient;
    Object receivedMessage;
    
    boolean isExceptionCaughtCalled;
    Client caughtClient;
    Throwable caughtCause;
    
    void messageSent(Client client, Object message) {
        isMessageSentCalled = true;
        sentClient = client;
        sentMessage = message;
    }
    
    void messageReceived(Client client, Object message) {
        isMessageReceivedCalled = true;
        receivedClient = client;
        receivedMessage = message;
    }
    
    void exceptionCaught(Client client, Throwable cause) {
        isExceptionCaughtCalled = true;
        caughtClient = client;
        caughtCause = cause;
    }
}
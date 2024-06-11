
import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(null, parentGroup.parentGroup());
        
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        group.add(client1);
        TestClientHandler handler = new TestClientHandler();
        group.addClientHandler(handler);
        
        assertTrue(group.notifyMessageSent(client1, "Test message"));
        assertTrue(handler.messageSentCalled);
        
        Client client2 = new Client();
        assertFalse(group.notifyMessageSent(client2, "Test message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        group.add(client1);
        TestClientHandler handler = new TestClientHandler();
        group.addClientHandler(handler);
        
        assertTrue(group.notifyMessageReceived(client1, "Test message"));
        assertTrue(handler.messageReceivedCalled);
        
        Client client2 = new Client();
        assertFalse(group.notifyMessageReceived(client2, "Test message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        group.add(client1);
        TestClientHandler handler = new TestClientHandler();
        group.addClientHandler(handler);
        
        assertTrue(group.notifyExceptionCaught(client1, new Throwable()));
        assertTrue(handler.exceptionCaughtCalled);
        
        Client client2 = new Client();
        assertFalse(group.notifyExceptionCaught(client2, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        group.add(client2);
        
        int count = 0;
        for (Client client : group) {
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    public void testContains() {
        ClientGroup group = new ClientGroup(null);
        
        Client client = new Client();
        assertFalse(group.contains(client));
        
        group.add(client);
        assertTrue(group.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        group.add(client2);
        
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        
        Client client = new Client();
        
        assertTrue(group.add(client));
        assertTrue(group.contains(client));
        
        TestClientHandler handler = new TestClientHandler();
        group.addClientHandler(handler);
        assertTrue(handler.clientAddedCalled);
        
        assertFalse(group.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        
        Client client = new Client();
        group.add(client);
        
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
        
        TestClientHandler handler = new TestClientHandler();
        group.addClientHandler(handler);
        assertTrue(handler.clientRemovedCalled);
        
        assertFalse(group.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        
        ClientGroup subGroup = group.createSubGroup();
        
        assertEquals(group, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        TestClientHandler handler = new TestClientHandler();
        
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }
    
    private class TestClientHandler implements ClientHandler {
        boolean messageSentCalled = false;
        boolean messageReceivedCalled = false;
        boolean exceptionCaughtCalled = false;
        boolean clientAddedCalled = false;
        boolean clientRemovedCalled = false;
        
        @Override
        public void messageSent(Client client, Object message) {
            messageSentCalled = true;
        }
        
        @Override
        public void messageReceived(Client client, Object message) {
            messageReceivedCalled = true;
        }
        
        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtCalled = true;
        }
        
        @Override
        public void clientAdded(Client client) {
            clientAddedCalled = true;
        }
        
        @Override
        public void clientRemoved(Client client) {
            clientRemovedCalled = true;
        }
    }
}

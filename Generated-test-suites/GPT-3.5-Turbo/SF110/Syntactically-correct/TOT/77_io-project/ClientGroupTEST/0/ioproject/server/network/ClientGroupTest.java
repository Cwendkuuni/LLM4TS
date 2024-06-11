import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(group.notifyMessageSent(client, message));
        
        group.add(client);
        assertTrue(group.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(group.notifyMessageReceived(client, message));
        
        group.add(client);
        assertTrue(group.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        group.add(client);
        assertTrue(group.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        group.add(client2);
        
        Iterator<Client> iterator = group.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
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
        
        assertEquals(0, group.size());
        
        group.add(client1);
        assertEquals(1, group.size());
        
        group.add(client2);
        assertEquals(2, group.size());
        
        group.remove(client1);
        assertEquals(1, group.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        
        assertTrue(group.add(client1));
        assertTrue(group.contains(client1));
        
        subGroup.add(client2);
        assertTrue(group.contains(client2));
        
        assertFalse(group.add(client3));
        assertFalse(group.contains(client3));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(group.remove(client1));
        
        group.add(client1);
        assertTrue(group.remove(client1));
        assertFalse(group.contains(client1));
        
        subGroup.add(client2);
        assertFalse(group.remove(client2));
        assertTrue(subGroup.contains(client2));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        
        assertNotNull(subGroup1);
        assertNotNull(subGroup2);
        assertNotEquals(subGroup1, subGroup2);
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        TestClientHandler handler = new TestClientHandler();
        
        group.add(client);
        group.addClientHandler(handler);
        group.notifyMessageSent(client, new Object());
        
        assertTrue(handler.messageSentCalled);
    }
    
    private class TestClientHandler implements ClientHandler {
        boolean messageSentCalled = false;
        
        @Override
        public void messageSent(Client client, Object message) {
            messageSentCalled = true;
        }
        
        // Implement other methods of ClientHandler
    }

}
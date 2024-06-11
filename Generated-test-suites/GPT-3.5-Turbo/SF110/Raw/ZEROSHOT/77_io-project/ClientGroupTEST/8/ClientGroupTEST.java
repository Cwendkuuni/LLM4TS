import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group1 = new ClientGroup(parentGroup);
        assertEquals(parentGroup, group1.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        group.notifyMessageSent(client1, "Hello");
        // Assert expected behavior
        
        group.notifyMessageSent(client2, "World");
        // Assert expected behavior
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        group.notifyMessageReceived(client1, "Hello");
        // Assert expected behavior
        
        group.notifyMessageReceived(client2, "World");
        // Assert expected behavior
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        group.notifyExceptionCaught(client1, new Exception("Error"));
        // Assert expected behavior
        
        group.notifyExceptionCaught(client2, new Exception("Error"));
        // Assert expected behavior
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
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        assertTrue(group.contains(client1));
        assertFalse(group.contains(client2));
        
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client2);
        assertTrue(group.contains(client2));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        assertEquals(1, group.size());
        
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client2);
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertTrue(group.add(client1));
        assertTrue(group.contains(client1));
        
        assertTrue(group.add(client2));
        assertTrue(group.contains(client2));
        
        ClientGroup subGroup = group.createSubGroup();
        assertFalse(subGroup.add(client1));
        assertTrue(subGroup.contains(client1));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        group.add(client2);
        
        assertTrue(group.remove(client1));
        assertFalse(group.contains(client1));
        
        assertFalse(group.remove(client1));
        
        assertFalse(group.remove(client2));
        assertFalse(group.contains(client2));
        
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client1);
        subGroup.add(client2);
        
        assertTrue(group.remove(client1));
        assertFalse(group.contains(client1));
        assertFalse(subGroup.contains(client1));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        
        assertNotNull(subGroup);
        assertEquals(group, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }
}
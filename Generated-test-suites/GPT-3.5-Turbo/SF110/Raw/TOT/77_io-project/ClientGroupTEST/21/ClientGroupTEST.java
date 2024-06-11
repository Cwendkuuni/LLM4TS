
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        // Verify that the child group has the correct parent group
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        // Test case: Client is not part of the group
        assertFalse(group.notifyMessageSent(client, "Hello"));
        
        // Test case: Client is part of the group and no subgroups
        group.add(client);
        assertTrue(group.notifyMessageSent(client, "Hello"));
        
        // Test case: Client is part of a subgroup
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        assertTrue(group.notifyMessageSent(client, "Hello"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        // Test case: Client is not part of the group
        assertFalse(group.notifyMessageReceived(client, "Hello"));
        
        // Test case: Client is part of the group and no subgroups
        group.add(client);
        assertTrue(group.notifyMessageReceived(client, "Hello"));
        
        // Test case: Client is part of a subgroup
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        assertTrue(group.notifyMessageReceived(client, "Hello"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        // Test case: Client is not part of the group
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        // Test case: Client is part of the group and no subgroups
        group.add(client);
        assertTrue(group.notifyExceptionCaught(client, cause));
        
        // Test case: Client is part of a subgroup
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        assertTrue(group.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Test case: Group with no clients
        assertFalse(group.iterator().hasNext());
        
        // Test case: Group with one client
        group.add(client1);
        assertEquals(client1, group.iterator().next());
        
        // Test case: Group with multiple clients
        group.add(client2);
        assertTrue(group.iterator().hasNext());
        assertEquals(client1, group.iterator().next());
        assertEquals(client2, group.iterator().next());
    }
    
    @Test
    public void testContains() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        // Test case: Group with no clients
        assertFalse(group.contains(client));
        
        // Test case: Client is part of the group
        group.add(client);
        assertTrue(group.contains(client));
        
        // Test case: Client is not part of the group
        group.remove(client);
        assertFalse(group.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Test case: Group with no clients
        assertEquals(0, group.size());
        
        // Test case: Group with one client
        group.add(client1);
        assertEquals(1, group.size());
        
        // Test case: Group with multiple clients
        group.add(client2);
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        // Test case: Client is part of the parent group
        assertTrue(group.add(client));
        assertTrue(group.contains(client));
        
        // Test case: Client is not part of the parent group
        ClientGroup parentGroup = new ClientGroup(null);
        assertFalse(parentGroup.add(client));
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        // Test case: Client is not part of the group
        assertFalse(group.remove(client));
        
        // Test case: Client is part of the group
        group.add(client);
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        
        // Test case: Group with no subgroups
        ClientGroup subGroup = group.createSubGroup();
        assertTrue(group.iterator().hasNext());
        assertEquals(subGroup, group.iterator().next());
        
        // Test case: Group with subgroups
        subGroup.createSubGroup();
        subGroup.createSubGroup();
        assertEquals(3, group.size());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        // Test case: Handler is added to the group
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
        
        // Test case: Handler is replaced in the group
        ClientHandler newHandler = new ClientHandler();
        group.addClientHandler(newHandler);
        assertEquals(newHandler, group.handler);
    }
}


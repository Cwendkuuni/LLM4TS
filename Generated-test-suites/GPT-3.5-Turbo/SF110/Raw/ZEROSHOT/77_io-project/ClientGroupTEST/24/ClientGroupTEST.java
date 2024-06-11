import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        // Test if parentGroup method returns the correct parent group
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Test the notifyMessageSent method
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(group.notifyMessageSent(client1, "Hello")); // Client not in group, should return false
        
        group.add(client1);
        
        assertTrue(group.notifyMessageSent(client1, "Hello")); // Client in group, should return true
        
        group.add(client2);
        subGroup1.add(client2);
        
        assertTrue(group.notifyMessageSent(client2, "Hello")); // Client in sub group, should return true
        assertTrue(subGroup1.notifyMessageSent(client2, "Hello")); // Client in sub group, should return true
        assertFalse(subGroup2.notifyMessageSent(client2, "Hello")); // Client not in sub group, should return false
    }

    @Test
    public void testNotifyMessageReceived() {
        // Test the notifyMessageReceived method
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(group.notifyMessageReceived(client1, "Hello")); // Client not in group, should return false
        
        group.add(client1);
        
        assertTrue(group.notifyMessageReceived(client1, "Hello")); // Client in group, should return true
        
        group.add(client2);
        subGroup1.add(client2);
        
        assertTrue(group.notifyMessageReceived(client2, "Hello")); // Client in sub group, should return true
        assertTrue(subGroup1.notifyMessageReceived(client2, "Hello")); // Client in sub group, should return true
        assertFalse(subGroup2.notifyMessageReceived(client2, "Hello")); // Client not in sub group, should return false
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Test the notifyExceptionCaught method
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        Throwable exception = new Throwable();
        
        assertFalse(group.notifyExceptionCaught(client1, exception)); // Client not in group, should return false
        
        group.add(client1);
        
        assertTrue(group.notifyExceptionCaught(client1, exception)); // Client in group, should return true
        
        group.add(client2);
        subGroup1.add(client2);
        
        assertTrue(group.notifyExceptionCaught(client2, exception)); // Client in sub group, should return true
        assertTrue(subGroup1.notifyExceptionCaught(client2, exception)); // Client in sub group, should return true
        assertFalse(subGroup2.notifyExceptionCaught(client2, exception)); // Client not in sub group, should return false
    }

    @Test
    public void testIterator() {
        // Test the iterator method
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
        // Test the contains method
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(group.contains(client1));
        assertFalse(group.contains(client2));
        
        group.add(client1);
        group.add(client2);
        
        assertTrue(group.contains(client1));
        assertTrue(group.contains(client2));
    }

    @Test
    public void testSize() {
        // Test the size method
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertEquals(0, group.size());
        
        group.add(client1);
        group.add(client2);
        
        assertEquals(2, group.size());
    }

    @Test
    public void testAdd() {
        // Test the add method
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertTrue(group.add(client1)); // Parent group is null, should return true
        
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client2);
        
        assertFalse(group.add(client2)); // Client already in sub group, should return false
        assertTrue(subGroup.add(client1)); // Parent group contains client, should return true
    }

    @Test
    public void testRemove() {
        // Test the remove method
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(group.remove(client1)); // Client not in group, should return false
        
        group.add(client1);
        group.add(client2);
        subGroup.add(client2);
        
        assertTrue(group.remove(client1)); // Client in group, should return true
        assertTrue(subGroup.remove(client2)); // Client in sub group, should return true
        assertFalse(subGroup.remove(client1)); // Client not in sub group, should return false
    }

    @Test
    public void testCreateSubGroup() {
        // Test the createSubGroup method
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        
        assertNotNull(subGroup1);
        assertNotNull(subGroup2);
    }

    @Test
    public void testAddClientHandler() {
        // Test the addClientHandler method
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        Client client = new Client();
        
        group.add(client);
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }

}
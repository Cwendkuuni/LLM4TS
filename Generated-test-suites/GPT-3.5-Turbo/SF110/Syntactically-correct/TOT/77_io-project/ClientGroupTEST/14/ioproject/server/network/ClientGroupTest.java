
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testConstructor() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertNotNull(parentGroup);
    }

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup1 = new ClientGroup(null);
        assertNull(parentGroup1.parentGroup());

        ClientGroup parentGroup2 = new ClientGroup(parentGroup1);
        assertEquals(parentGroup1, parentGroup2.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test sending message to clients
        assertTrue(parentGroup.notifyMessageSent(client1, "Hello"));
        assertTrue(parentGroup.notifyMessageSent(client2, "World"));
        
        // Test sending message to non-existent client
        assertFalse(parentGroup.notifyMessageSent(new Client(), "Test"));
    }

    @Test
    public void testNotifyMessageReceived() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test receiving message from clients
        assertTrue(parentGroup.notifyMessageReceived(client1, "Hello"));
        assertTrue(parentGroup.notifyMessageReceived(client2, "World"));
        
        // Test receiving message from non-existent client
        assertFalse(parentGroup.notifyMessageReceived(new Client(), "Test"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test catching exception from clients
        assertTrue(parentGroup.notifyExceptionCaught(client1, new Throwable()));
        assertTrue(parentGroup.notifyExceptionCaught(client2, new Throwable()));
        
        // Test catching exception from non-existent client
        assertFalse(parentGroup.notifyExceptionCaught(new Client(), new Throwable()));
    }

    @Test
    public void testIterator() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test iterator
        int count = 0;
        for (Client client : parentGroup) {
            assertNotNull(client);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testContains() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test contains
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
        assertFalse(parentGroup.contains(new Client()));
    }

    @Test
    public void testSize() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test size
        assertEquals(2, parentGroup.size());
    }

    @Test
    public void testAdd() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Test adding client
        assertTrue(parentGroup.add(client1));
        
        // Test adding client already in parent group
        assertFalse(parentGroup.add(client1));
        
        // Test adding client in sub group
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(subGroup.add(client2));
        
        // Test adding client not in parent group
        assertFalse(subGroup.add(client1));
    }

    @Test
    public void testRemove() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Add clients to the group
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        
        // Test removing client
        assertTrue(parentGroup.remove(client1));
        
        // Test removing client not in the group
        assertFalse(parentGroup.remove(client1));
        
        // Test removing client from sub group
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(subGroup.add(client2));
        assertTrue(parentGroup.remove(client2));
        
        // Test removing client from sub group recursively
        assertTrue(parentGroup.add(client2));
        assertTrue(subGroup.remove(client2));
    }

    @Test
    public void testCreateSubGroup() {
        // Create ClientGroup and Clients
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Create sub groups
        ClientGroup subGroup1 = parentGroup.createSubGroup();
        assertNotNull(subGroup1);
        ClientGroup subGroup2 = parentGroup.createSubGroup();
        assertNotNull(subGroup2);
        
        // Add clients to sub groups
        assertTrue(subGroup1.add(client1));
        assertTrue(subGroup2.add(client2));
        
        // Test parent group contains clients from sub groups
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
    }

    @Test
    public void testAddClientHandler() {
        // Create ClientGroup and ClientHandler
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        // Test adding client handler to group
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}


// Helper classes for testing
class Client {}
class ClientHandler {}

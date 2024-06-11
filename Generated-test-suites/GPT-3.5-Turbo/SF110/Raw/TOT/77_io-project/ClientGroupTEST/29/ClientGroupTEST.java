import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {
    
    @Test
    public void testConstructor() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);

        // Check if parentGroup of group is set correctly
        assertEquals(parentGroup, group.parentGroup());

        // Check if subGroups and clients are initialized to empty sets
        assertEquals(0, group.subGroups.size());
        assertEquals(0, group.clients.size());

        // Check if handler is set to null
        assertNull(group.handler);
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);

        // Check if notifyMessageSent returns false when client is not in the group
        assertFalse(group.notifyMessageSent(client2, "Hello"));

        // Check if handler's messageSent method is called when client is in the group
        group.handler = new ClientHandler();
        group.notifyMessageSent(client1, "Hello");
        assertTrue(group.handler.messageSentCalled);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);

        // Check if notifyMessageReceived returns false when client is not in the group
        assertFalse(group.notifyMessageReceived(client2, "Hello"));

        // Check if handler's messageReceived method is called when client is in the group
        group.handler = new ClientHandler();
        group.notifyMessageReceived(client1, "Hello");
        assertTrue(group.handler.messageReceivedCalled);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);

        // Check if notifyExceptionCaught returns false when client is not in the group
        assertFalse(group.notifyExceptionCaught(client2, new Throwable()));

        // Check if handler's exceptionCaught method is called when client is in the group
        group.handler = new ClientHandler();
        group.notifyExceptionCaught(client1, new Throwable());
        assertTrue(group.handler.exceptionCaughtCalled);
    }
    
    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);

        // Check if contains returns true when client is in the group
        assertTrue(group.contains(client1));

        // Check if contains returns false when client is not in the group
        assertFalse(group.contains(client2));
    }
    
    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        // Check if size returns the correct number of clients in the group
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Check if add returns true when client is added to the group
        assertTrue(group.add(client1));
        
        // Check if add returns false when client is already in the group
        assertFalse(group.add(client1));
        
        // Check if clientAdded method is called when client is added to the group
        group.handler = new ClientHandler();
        group.add(client2);
        assertTrue(group.handler.clientAddedCalled);
    }
    
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        // Check if remove returns true when client is removed from the group
        assertTrue(group.remove(client1));
        
        // Check if remove returns false when client is not in the group
        assertFalse(group.remove(client1));
        
        // Check if clientRemoved method is called when client is removed from the group
        group.handler = new ClientHandler();
        group.remove(client2);
        assertTrue(group.handler.clientRemovedCalled);
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        ClientGroup subGroup = group.createSubGroup();
        
        // Check if createSubGroup returns a new instance of ClientGroup
        assertNotNull(subGroup);
        
        // Check if subGroup is added to the subGroups set of group
        assertTrue(group.subGroups.contains(subGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);

        // Check if handler is set correctly
        assertEquals(handler, group.handler);
    }
}
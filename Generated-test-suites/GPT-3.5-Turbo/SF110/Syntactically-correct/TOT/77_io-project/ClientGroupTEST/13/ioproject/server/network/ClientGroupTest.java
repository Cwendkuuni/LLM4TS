import org.junit.Test;
import static org.junit.Assert.*;

import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;

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
        
        // Test when client is not in the group
        assertFalse(group.notifyMessageSent(client, message));
        
        // Test when client is in a sub-group
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        subGroup.addClientHandler(new ClientHandler());
        
        assertTrue(group.notifyMessageSent(client, message));
        
        // Test when client is in the group and handler is not null
        group.add(client);
        group.addClientHandler(new ClientHandler());
        
        assertTrue(group.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        
        Client client = new Client();
        Object message = new Object();
        
        // Test when client is not in the group
        assertFalse(group.notifyMessageReceived(client, message));
        
        // Test when client is in a sub-group
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        subGroup.addClientHandler(new ClientHandler());
        
        assertTrue(group.notifyMessageReceived(client, message));
        
        // Test when client is in the group and handler is not null
        group.add(client);
        group.addClientHandler(new ClientHandler());
        
        assertTrue(group.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        
        Client client = new Client();
        Throwable cause = new Throwable();
        
        // Test when client is not in the group
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        // Test when client is in a sub-group
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        subGroup.addClientHandler(new ClientHandler());
        
        assertTrue(group.notifyExceptionCaught(client, cause));
        
        // Test when client is in the group and handler is not null
        group.add(client);
        group.addClientHandler(new ClientHandler());
        
        assertTrue(group.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        group.add(client2);
        
        int count = 0;
        for (Client c : group) {
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
        
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        
        // Test parent contains client
        assertTrue(group.add(client1));
        
        // Test parent does not contain client
        group.remove(client1);
        assertFalse(group.add(client2));
        
        // Test when handler is not null
        group.addClientHandler(new ClientHandler());
        assertTrue(group.add(client2));
        
        // Test when client is added to sub-group
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client3);
        
        assertTrue(group.add(client3));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        
        Client client1 = new Client();
        Client client2 = new Client();
        
        // Test when client is not a member
        assertFalse(group.remove(client1));
        
        // Test when client is a member
        group.add(client1);
        group.add(client2);
        
        assertTrue(group.remove(client1));
        assertFalse(group.contains(client1));
        
        // Test when client is a member of a sub-group
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client2);
        
        assertTrue(group.remove(client2));
        assertFalse(subGroup.contains(client2));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        
        assertNotNull(subGroup);
        assertTrue(group.contains(subGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }
}
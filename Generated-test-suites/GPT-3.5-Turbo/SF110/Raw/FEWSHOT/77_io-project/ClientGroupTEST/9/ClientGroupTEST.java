package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;
import ioproject.common.network.*;

public class ClientGroupTest {
  
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        // Test when clients do not contain the specified client
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.notifyMessageSent(client, "message"));
        
        // Test when clients contain the specified client and there are no subgroups or handler
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "message"));
        
        // Test when there are subgroups and handler, but not in a subgroup
        ClientGroup subgroup = clientGroup.createSubGroup();
        clientGroup.addClientHandler(new ClientHandler());
        assertFalse(clientGroup.notifyMessageSent(client, "message"));
        
        // Test when there are subgroups and handler, in a subgroup
        subgroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        // Test when clients do not contain the specified client
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.notifyMessageReceived(client, "message"));
        
        // Test when clients contain the specified client and there are no subgroups or handler
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "message"));
        
        // Test when there are subgroups and handler, but not in a subgroup
        ClientGroup subgroup = clientGroup.createSubGroup();
        clientGroup.addClientHandler(new ClientHandler());
        assertFalse(clientGroup.notifyMessageReceived(client, "message"));
        
        // Test when there are subgroups and handler, in a subgroup
        subgroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, "message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        // Test when clients do not contain the specified client
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
        
        // Test when clients contain the specified client and there are no subgroups or handler
        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
        
        // Test when there are subgroups and handler, but not in a subgroup
        ClientGroup subgroup = clientGroup.createSubGroup();
        clientGroup.addClientHandler(new ClientHandler());
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
        
        // Test when there are subgroups and handler, in a subgroup
        subgroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        int count = 0;
        for (Client client : clientGroup) {
            count++;
        }
        assertEquals(2, count);
    }
    
    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        assertEquals(0, clientGroup.size());
        
        clientGroup.add(client1);
        assertEquals(1, clientGroup.size());
        
        clientGroup.add(client2);
        assertEquals(2, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
        
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        ClientGroup childGroup = parentGroup.createSubGroup();
        assertTrue(childGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.remove(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
        
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        ClientGroup childGroup = parentGroup.createSubGroup();
        childGroup.add(client);
        assertTrue(childGroup.remove(client));
        assertFalse(childGroup.contains(client));
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertNotNull(subGroup);
        assertTrue(parentGroup.iterator().hasNext());
        assertEquals(parentGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup(null);
        assertNull(clientGroup.handler);
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}
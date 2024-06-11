package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClientGroupTest {
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(parentGroup.notifyMessageSent(client, message));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, message));
        
        assertFalse(childGroup.notifyMessageSent(client, message));
        
        childGroup.add(client);
        assertTrue(childGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(parentGroup.notifyMessageReceived(client, message));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        
        assertFalse(childGroup.notifyMessageReceived(client, message));
        
        childGroup.add(client);
        assertTrue(childGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        
        assertFalse(childGroup.notifyExceptionCaught(client, cause));
        
        childGroup.add(client);
        assertTrue(childGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.iterator().hasNext());
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.contains(client1));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.contains(client1));
        assertFalse(clientGroup.contains(client2));
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
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertTrue(parentGroup.add(client1));
        
        assertTrue(parentGroup.contains(client1));
        assertFalse(childGroup.contains(client1));
        
        assertFalse(parentGroup.add(client2));
        
        assertTrue(parentGroup.contains(client2));
        assertFalse(childGroup.contains(client2));
    }
    
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(parentGroup.remove(client1));
        
        parentGroup.add(client1);
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.contains(client1));
        
        assertFalse(childGroup.remove(client1));
        
        childGroup.add(client1);
        assertTrue(childGroup.remove(client1));
        assertFalse(childGroup.contains(client1));
        
        assertFalse(parentGroup.remove(client2));
        
        parentGroup.add(client2);
        childGroup.add(client2);
        
        assertTrue(parentGroup.remove(client2));
        assertFalse(parentGroup.contains(client2));
        assertFalse(childGroup.contains(client2));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        assertNull(clientGroup.handler);
        
        clientGroup.addClientHandler(handler);
        
        assertEquals(handler, clientGroup.handler);
    }
}
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
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(parentGroup.notifyMessageSent(client, message));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, message));
        
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertFalse(childGroup.notifyMessageSent(client, message));
        
        childGroup.add(client);
        assertTrue(childGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(parentGroup.notifyMessageReceived(client, message));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertFalse(childGroup.notifyMessageReceived(client, message));
        
        childGroup.add(client);
        assertTrue(childGroup.notifyMessageReceived(client, message));        
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertFalse(childGroup.notifyExceptionCaught(client, cause));
        
        childGroup.add(client);
        assertTrue(childGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(parentGroup.iterator().hasNext());
        
        parentGroup.add(client);
        assertTrue(parentGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(parentGroup.contains(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertEquals(0, parentGroup.size());
        
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertTrue(parentGroup.add(client));
        assertFalse(parentGroup.add(client));
        
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertFalse(childGroup.add(client));
        
        childGroup.remove(client);
        assertTrue(childGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(parentGroup.remove(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        
        ClientGroup childGroup = new ClientGroup(parentGroup);
        assertFalse(childGroup.remove(client));
        
        childGroup.add(client);
        assertTrue(childGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertNotNull(subGroup);
        assertEquals(parentGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        parentGroup.addClientHandler(handler);
        
        assertEquals(handler, parentGroup.handler);
    }
}
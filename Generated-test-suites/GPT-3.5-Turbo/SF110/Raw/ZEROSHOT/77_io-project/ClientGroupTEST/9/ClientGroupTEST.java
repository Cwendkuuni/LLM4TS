package ioproject.server.network;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        Object message = new Object();
        
        parentGroup.add(client);
        subGroup.add(client);
        
        assertTrue(parentGroup.notifyMessageSent(client, message));
        assertTrue(subGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        Object message = new Object();
        
        parentGroup.add(client);
        subGroup.add(client);
        
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        assertTrue(subGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        Throwable cause = new Throwable();
        
        parentGroup.add(client);
        subGroup.add(client);
        
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertTrue(subGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        int count = 0;
        for (Client client : parentGroup) {
            count++;
        }
        
        assertEquals(2, count);
    }

    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        parentGroup.add(client);
        
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        assertEquals(2, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertTrue(parentGroup.add(client));
        
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        
        parentGroup.add(client);
        
        assertTrue(parentGroup.remove(client));
        
        assertFalse(parentGroup.contains(client));
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
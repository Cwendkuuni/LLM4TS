package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.ref.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, group.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(group.notifyMessageSent(client, message));
        
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        
        subGroup1.add(client);
        
        assertTrue(group.notifyMessageSent(client, message));
        
        assertNull(group.notifyMessageSent(null, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(group.notifyMessageReceived(client, message));
        
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        
        subGroup1.add(client);
        
        assertTrue(group.notifyMessageReceived(client, message));
        
        assertNull(group.notifyMessageReceived(null, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        ClientGroup subGroup1 = group.createSubGroup();
        ClientGroup subGroup2 = group.createSubGroup();
        
        subGroup1.add(client);
        
        assertTrue(group.notifyExceptionCaught(client, cause));
        
        assertNull(group.notifyExceptionCaught(null, cause));
    }
    
    @Test
    public void testIterator() {
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
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(group.contains(client));
        
        group.add(client);
        
        assertTrue(group.contains(client));
        
        assertFalse(group.contains(null));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        
        assertEquals(0, group.size());
        
        Client client1 = new Client();
        group.add(client1);
        
        assertEquals(1, group.size());
        
        Client client2 = new Client();
        group.add(client2);
        
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        assertTrue(group.add(client));
        assertTrue(group.contains(client));
        
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertTrue(subGroup.add(client));
        assertTrue(subGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(group.remove(client));
        
        group.add(client);
        
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
        
        ClientGroup subGroup = group.createSubGroup();
        subGroup.add(client);
        
        assertTrue(subGroup.remove(client));
        assertFalse(subGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        
        ClientGroup subGroup = group.createSubGroup();
        
        assertNotNull(subGroup);
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }
}
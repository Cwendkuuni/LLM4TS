package ioproject.server.network;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.Iterator;

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
        group.add(client);
        
        assertTrue(group.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        group.add(client);
        
        assertTrue(group.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        group.add(client);
        
        assertTrue(group.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        Iterator<Client> iterator = group.iterator();
        assertEquals(client1, iterator.next());
        assertEquals(client2, iterator.next());
    }
    
    @Test
    public void testContains() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        
        assertTrue(group.contains(client));
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
        Client client = new Client();
        
        assertTrue(group.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        
        assertNotNull(subGroup);
        assertEquals(group, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }
    
}
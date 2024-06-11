package ioproject.server.network;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        assertEquals(parentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(clientGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup clientGroup = new ClientGroup(null);
        Iterator<Client> iterator = clientGroup.iterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup clientGroup = new ClientGroup(null);
        assertEquals(0, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertTrue(clientGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(clientGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotNull(subGroup);
        assertTrue(parentGroup.iterator().hasNext());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}
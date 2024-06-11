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
        ClientHandler handler = new ClientHandler();
        ClientGroup group = new ClientGroup(null);
        group.addClientHandler(handler);
        
        Client client = new Client();
        Object message = new Object();
        assertTrue(group.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientHandler handler = new ClientHandler();
        ClientGroup group = new ClientGroup(null);
        group.addClientHandler(handler);
        
        Client client = new Client();
        Object message = new Object();
        assertTrue(group.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientHandler handler = new ClientHandler();
        ClientGroup group = new ClientGroup(null);
        group.addClientHandler(handler);
        
        Client client = new Client();
        Throwable cause = new Throwable();
        assertTrue(group.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        group.add(new Client());
        group.add(new Client());
        
        Iterator<Client> iterator = group.iterator();
        assertEquals(true, iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertTrue(group.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        group.add(new Client());
        group.add(new Client());
        
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        Client client = new Client();
        ClientGroup group = new ClientGroup(null);
        
        assertTrue(group.add(client));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertTrue(group.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        assertNotNull(group.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        ClientGroup group = new ClientGroup(null);
        
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }
    
    private class ClientHandler implements ioproject.common.network.ClientHandler {
        @Override
        public void messageSent(Client client, Object message) {
        }
        @Override
        public void messageReceived(Client client, Object message) {
        }
        @Override
        public void exceptionCaught(Client client, Throwable cause) {
        }
        @Override
        public void clientAdded(Client client) {
        }
        @Override
        public void clientRemoved(Client client) {
        }
    }
}
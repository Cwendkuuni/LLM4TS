package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, group.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertFalse(group.notifyMessageSent(new Client(), message));
        
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                
            }
        };
        
        group.addClientHandler(handler);
        assertTrue(group.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertFalse(group.notifyMessageReceived(new Client(), message));
        
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                
            }
        };
        
        group.addClientHandler(handler);
        assertTrue(group.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertFalse(group.notifyExceptionCaught(new Client(), cause));
        
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                
            }
        };
        
        group.addClientHandler(handler);
        assertTrue(group.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        
        assertFalse(group.iterator().hasNext());
        
        Client client = new Client();
        group.add(client);
        
        assertTrue(group.iterator().hasNext());
        assertEquals(client, group.iterator().next());
    }

    @Test
    public void testContains() {
        Client client = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertTrue(group.contains(client));
        assertFalse(group.contains(new Client()));
    }

    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        
        assertEquals(0, group.size());
        
        group.add(new Client());
        
        assertEquals(1, group.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
        
        ClientGroup group = new ClientGroup(null);
        assertTrue(group.add(client));
        
        ClientGroup parentGroup = new ClientGroup(null);
        group = new ClientGroup(parentGroup);
        assertTrue(group.add(client));
        
        assertFalse(group.add(new Client()));
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        
        assertTrue(group.remove(client));
        assertFalse(group.remove(new Client()));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        
        assertNotNull(group.createSubGroup());
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientAdded(Client client) {
                
            }
        };
        
        ClientGroup group = new ClientGroup(null);
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }
}
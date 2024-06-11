package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup clientGroup;
    private ClientHandler handler;
    
    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        clientGroup = new ClientGroup(parentGroup);
        handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}
        };
    }
    
    @Test
    public void testParentGroup() {
        Assert.assertEquals(parentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();
        
        Assert.assertFalse(clientGroup.notifyMessageSent(client, message));
        
        parentGroup.add(client);
        
        Assert.assertTrue(clientGroup.notifyMessageSent(client, message));

        handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                Assert.assertEquals(client, client);
                Assert.assertEquals(message, message);
            }

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}
        };

        clientGroup.addClientHandler(handler);
        Assert.assertTrue(clientGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        
        Assert.assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        parentGroup.add(client);
        
        Assert.assertTrue(clientGroup.notifyMessageReceived(client, message));

        handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {
                Assert.assertEquals(client, client);
                Assert.assertEquals(message, message);
            }

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}
        };

        clientGroup.addClientHandler(handler);
        Assert.assertTrue(clientGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        Assert.assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.add(client);
        
        Assert.assertTrue(clientGroup.notifyExceptionCaught(client, cause));

        handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                Assert.assertEquals(client, client);
                Assert.assertEquals(cause, cause);
            }

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}
        };

        clientGroup.addClientHandler(handler);
        Assert.assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        Iterator<Client> iterator = clientGroup.iterator();
        Assert.assertFalse(iterator.hasNext());
        
        Client client = new Client();
        parentGroup.add(client);
        
        iterator = clientGroup.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(client, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        
        Assert.assertFalse(clientGroup.contains(client));
        
        parentGroup.add(client);
        
        Assert.assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(0, clientGroup.size());
        
        Client client = new Client();
        parentGroup.add(client);
        
        Assert.assertEquals(1, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client = new Client();
        
        Assert.assertTrue(clientGroup.add(client));
        Assert.assertTrue(clientGroup.contains(client));
        
        handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}

            @Override
            public void clientAdded(Client client) {
                Assert.assertEquals(client, client);
            }

            @Override
            public void clientRemoved(Client client) {}
        };

        clientGroup.addClientHandler(handler);
        
        Assert.assertTrue(clientGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        
        Assert.assertFalse(clientGroup.remove(client));
        
        parentGroup.add(client);
        
        Assert.assertTrue(clientGroup.remove(client));
        Assert.assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        
        subGroup.add(client);
        
        Assert.assertTrue(clientGroup.remove(client));
        Assert.assertFalse(clientGroup.contains(client));
        Assert.assertFalse(subGroup.contains(client));
        
        handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {
                Assert.assertEquals(client, client);
            }
        };

        clientGroup.addClientHandler(handler);
        
        Assert.assertTrue(clientGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        
        Assert.assertEquals(clientGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        clientGroup.addClientHandler(handler);
        
        Assert.assertEquals(handler, clientGroup.handler);
    }
}
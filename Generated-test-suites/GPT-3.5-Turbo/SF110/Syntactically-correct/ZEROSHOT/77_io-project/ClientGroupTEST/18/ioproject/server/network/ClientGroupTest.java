package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class ClientGroupTest {
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client = new Client();
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client, "Hello"));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "Hello"));
        
        parentGroup.addClientHandler(new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertEquals(client, client);
                assertEquals("Hello", message);
            }
        });
        assertTrue(parentGroup.notifyMessageSent(client, "Hello"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(subGroup.notifyMessageReceived(client, "Hello"));
        
        subGroup.add(client);
        assertTrue(subGroup.notifyMessageReceived(client, "Hello"));
        
        subGroup.addClientHandler(new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                assertEquals(client, client);
                assertEquals("Hello", message);
            }
        });
        assertTrue(subGroup.notifyMessageReceived(client, "Hello"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(subGroup.notifyExceptionCaught(client, new Exception()));
        
        subGroup.add(client);
        assertTrue(subGroup.notifyExceptionCaught(client, new Exception()));
        
        subGroup.addClientHandler(new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                assertEquals(client, client);
                assertTrue(cause instanceof Exception);
            }
        });
        assertTrue(subGroup.notifyExceptionCaught(client, new Exception()));
    }
    
    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());
        
        parentGroup.add(client);
        Iterator<Client> iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
        assertFalse(iterator.hasNext());

        parentGroup.add(new Client());
        iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
        
        parentGroup.addClientHandler(new ClientHandler() {
            @Override
            public void clientAdded(Client client) {
                assertEquals(client, client);
            }
        });
        assertTrue(parentGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        
        parentGroup.add(new Client());
        subGroup.add(client);
        assertTrue(subGroup.remove(client));
        assertFalse(subGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.contains(subGroup));
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientAdded(Client client) {
                assertEquals(client, client);
            }
        };
        parentGroup.addClientHandler(handler);

        parentGroup.add(client);
    }
}
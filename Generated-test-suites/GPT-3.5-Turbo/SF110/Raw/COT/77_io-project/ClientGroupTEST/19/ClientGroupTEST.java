
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {
    
    private ClientGroup clientGroup;
    
    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
    }
    
    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(clientGroup.notifyMessageSent(client, message));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        Iterator<Client> iterator = clientGroup.iterator();
        
        assertFalse(iterator.hasNext());
        
        Client client1 = new Client();
        clientGroup.add(client1);
        
        Client client2 = new Client();
        clientGroup.add(client2);
        
        iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertSame(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        
        assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
        
        Client client = new Client();
        clientGroup.add(client);
        
        assertEquals(1, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client = new Client();
        
        assertTrue(clientGroup.add(client));
        
        assertTrue(clientGroup.contains(client));
        
        Client client2 = new Client();
        clientGroup.createSubGroup().add(client2);
        
        assertFalse(clientGroup.add(client2));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        
        assertFalse(clientGroup.remove(client));
        
        clientGroup.add(client);
        
        assertTrue(clientGroup.remove(client));
        
        assertFalse(clientGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        
        assertNotNull(subGroup);
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler() {
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
        
        clientGroup.addClientHandler(handler);
        
        assertSame(handler, clientGroup.handler);
    }
}

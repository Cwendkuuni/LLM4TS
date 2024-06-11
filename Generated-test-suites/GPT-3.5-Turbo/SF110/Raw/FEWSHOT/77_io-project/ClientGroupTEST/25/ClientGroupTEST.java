import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class ClientGroupTest {
    private ClientGroup clientGroup;
    private Client client;
    private Object message;
    private Throwable throwable;
    
    @Before
    public void setup() {
        clientGroup = new ClientGroup(null);
        client = new Client();
        message = new Object();
        throwable = new Throwable();
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(clientGroup.notifyMessageSent(client, message));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        clientGroup.notifyExceptionCaught(client, throwable);
        clientGroup.addClientHandler(new ClientHandler());
        clientGroup.notifyExceptionCaught(client, throwable);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.notifyExceptionCaught(client, throwable);
        clientGroup.addClientHandler(new ClientHandler());
        clientGroup.notifyExceptionCaught(client, throwable);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(clientGroup.notifyExceptionCaught(client, throwable));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, throwable));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, throwable));
        
        clientGroup.notifyMessageReceived(client, message);
        clientGroup.addClientHandler(new ClientHandler());
        clientGroup.notifyMessageReceived(client, message);
    }
    
    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());
        
        clientGroup.add(client);
        assertTrue(clientGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
        
        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertEquals(2, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(subGroup.add(client));
        
        subGroup.notifyMessageSent(client, message);
        clientGroup.addClientHandler(new ClientHandler());
        subGroup.notifyMessageSent(client, message);
    }
    
    @Test
    public void testRemove() {
        assertFalse(clientGroup.remove(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.contains(client));
        assertTrue(subGroup.contains(client));
        
        assertTrue(subGroup.remove(client));
        assertFalse(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        
        assertFalse(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));
        
        subGroup.add(client);
        assertTrue(clientGroup.contains(client));
        assertTrue(subGroup.contains(client));
    }
    
    @Test
    public void testAddClientHandler() {
        assertNull(clientGroup.handler);
        
        clientGroup.addClientHandler(new ClientHandler());
        assertNotNull(clientGroup.handler);
    }
}

class Client {
    
}

class ClientHandler {
    public void messageSent(Client client, Object message) {}
    public void messageReceived(Client client, Object message) {}
    public void exceptionCaught(Client client, Throwable cause) {}
    public void clientAdded(Client client) {}
    public void clientRemoved(Client client) {}
}
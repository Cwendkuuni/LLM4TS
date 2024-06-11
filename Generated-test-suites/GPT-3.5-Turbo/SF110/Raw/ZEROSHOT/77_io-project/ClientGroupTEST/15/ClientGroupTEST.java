import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        assertFalse(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client = new Client();
        assertFalse(clientGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        assertFalse(clientGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(clientGroup.createSubGroup());
        assertEquals(1, clientGroup.size());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}

class Client {}

class ClientHandler {
    public void messageSent(Client client, Object message) {}
    
    public void messageReceived(Client client, Object message) {}
    
    public void exceptionCaught(Client client, Throwable cause) {}
    
    public void clientAdded(Client client) {}
    
    public void clientRemoved(Client client) {}
}
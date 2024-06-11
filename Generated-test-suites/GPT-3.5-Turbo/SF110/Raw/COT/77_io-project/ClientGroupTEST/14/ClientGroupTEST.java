import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    
    private ClientGroup clientGroup;
    
    @Before
    public void setup() {
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
        assertFalse(clientGroup.notifyMessageSent(client, message));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        assertFalse(subGroup.notifyMessageSent(client, message));
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        clientGroup.notifyMessageSent(client, message);
        assertTrue(handler.isMessageSent());
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        assertFalse(subGroup.notifyMessageReceived(client, message));
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        clientGroup.notifyMessageReceived(client, message);
        assertTrue(handler.isMessageReceived());
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable exception = new Throwable();
        
        assertFalse(clientGroup.notifyExceptionCaught(client, exception));
        
        clientGroup.add(client);
        assertFalse(clientGroup.notifyExceptionCaught(client, exception));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, exception));
        assertFalse(subGroup.notifyExceptionCaught(client, exception));
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        clientGroup.notifyExceptionCaught(client, exception);
        assertTrue(handler.isExceptionCaught());
    }
    
    @Test
    public void testContains() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.contains(client1));
        assertFalse(clientGroup.contains(client2));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.contains(client1));
        assertFalse(clientGroup.contains(client2));
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
        assertEquals(1, clientGroup.size());
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(subGroup.add(client));
        assertTrue(subGroup.contains(client));
        
        ClientGroup parentGroup = subGroup.parentGroup();
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        
        assertFalse(clientGroup.remove(client));
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(subGroup.remove(client));
        assertFalse(subGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        
        assertNotNull(subGroup);
        assertEquals(clientGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}
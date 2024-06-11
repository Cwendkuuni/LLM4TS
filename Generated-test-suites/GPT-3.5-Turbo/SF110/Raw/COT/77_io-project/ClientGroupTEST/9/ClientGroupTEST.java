
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(clientGroup.notifyMessageSent(client, "Test Message"));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "Test Message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(clientGroup.notifyMessageReceived(client, "Test Message"));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, "Test Message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        
        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertEquals(0, clientGroup.size());
        
        clientGroup.add(client1);
        assertEquals(1, clientGroup.size());
        
        clientGroup.add(client2);
        assertEquals(2, clientGroup.size());
        
        clientGroup.remove(client1);
        assertEquals(1, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
        
        ClientGroup parentGroup = new ClientGroup(null);
        clientGroup = new ClientGroup(parentGroup);
        
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));        
    }
    
    @Test
    public void testRemove() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(clientGroup.remove(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}


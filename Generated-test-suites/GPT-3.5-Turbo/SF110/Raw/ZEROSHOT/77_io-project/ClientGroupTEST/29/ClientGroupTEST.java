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
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.notifyMessageSent(client1, "Hello"));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyMessageSent(client1, "Hello"));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.notifyMessageSent(client2, "Hi"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.notifyMessageReceived(client1, "Hello"));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyMessageReceived(client1, "Hello"));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.notifyMessageReceived(client2, "Hi"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.notifyExceptionCaught(client1, new Throwable()));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyExceptionCaught(client1, new Throwable()));
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.notifyExceptionCaught(client2, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.iterator().hasNext());
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertTrue(clientGroup.iterator().hasNext());
        assertEquals(client1, clientGroup.iterator().next());
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
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertEquals(0, clientGroup.size());
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertEquals(2, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.add(client1));
        assertFalse(clientGroup.add(client2));
        
        clientGroup.createSubGroup();
        
        assertTrue(clientGroup.add(client1));
        assertTrue(clientGroup.add(client2));
    }
    
    @Test
    public void testRemove() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertFalse(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client2));
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client1));
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

import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    // Test constructor
    @Test
    public void testConstructor() {
        ClientGroup parentGroup = new ClientGroup(null);
        
        assertNotNull(parentGroup);
        assertNull(parentGroup.parentGroup());
        assertEquals(0, parentGroup.size());
    }
    
    // Test notifyMessageSent method
    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        assertFalse(parentGroup.notifyMessageSent(null, "Test message"));
        assertFalse(parentGroup.notifyMessageSent(new Client(), "Test message"));
        assertTrue(parentGroup.notifyMessageSent(client1, "Test message"));
        assertTrue(parentGroup.notifyMessageSent(client2, "Test message"));
    }
    
    // Test notifyMessageReceived method
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        assertFalse(parentGroup.notifyMessageReceived(null, "Test message"));
        assertFalse(parentGroup.notifyMessageReceived(new Client(), "Test message"));
        assertTrue(parentGroup.notifyMessageReceived(client1, "Test message"));
        assertTrue(parentGroup.notifyMessageReceived(client2, "Test message"));
    }
    
    // Test notifyExceptionCaught method
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        assertFalse(parentGroup.notifyExceptionCaught(null, new Throwable()));
        assertFalse(parentGroup.notifyExceptionCaught(new Client(), new Throwable()));
        assertTrue(parentGroup.notifyExceptionCaught(client1, new Throwable()));
        assertTrue(parentGroup.notifyExceptionCaught(client2, new Throwable()));
    }
    
    // Test contains method
    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        
        assertFalse(parentGroup.contains(client2));
        assertTrue(parentGroup.contains(client1));
    }
    
    // Test size method
    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        assertEquals(2, parentGroup.size());
    }
    
    // Test add method
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.contains(client1));
        assertFalse(parentGroup.add(client2));
        assertTrue(parentGroup.contains(client2));
    }
    
    // Test remove method
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        parentGroup.add(client2);
        
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.contains(client1));
        assertFalse(parentGroup.remove(client1));
        assertTrue(parentGroup.remove(client2));
        assertFalse(parentGroup.contains(client2));
    }
    
    // Test createSubGroup method
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertNotNull(subGroup);
        assertEquals(parentGroup, subGroup.parentGroup());
    }
    
    // Test addClientHandler method
    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        parentGroup.addClientHandler(handler);
        
        assertEquals(handler, parentGroup.handler);
    }
}


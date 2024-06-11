import static org.junit.Assert.*;
import org.junit.Test;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = "test message";
        assertFalse(parentGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);        
        Client client = new Client();
        Object message = "test message";
        assertFalse(parentGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);        
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup parentGroup = new ClientGroup(null);        
        assertNotNull(parentGroup.iterator());
    }
    
    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);        
        Client client = new Client();
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);        
        assertEquals(0, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);        
        Client client = new Client();
        assertTrue(parentGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);        
        Client client = new Client();
        assertFalse(parentGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);        
        assertNotNull(parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);        
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
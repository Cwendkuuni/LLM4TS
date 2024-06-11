import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        ClientGroup group = new ClientGroup(null);
        
        group.add(client);
        
        assertTrue(group.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        
        group.add(client1);
        group.add(client2);
        
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAddRemoveClient() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        assertTrue(group.add(client));
        assertTrue(group.contains(client));
        
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        
        ClientGroup subGroup = parentGroup.createSubGroup();
        
        assertNotNull(subGroup);
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}
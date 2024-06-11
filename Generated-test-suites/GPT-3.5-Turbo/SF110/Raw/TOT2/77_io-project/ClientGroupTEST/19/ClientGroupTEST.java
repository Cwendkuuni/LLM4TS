import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    
    private ClientGroup parentGroup;
    private ClientGroup subGroup1;
    private ClientGroup subGroup2;
    private Client client1;
    private Client client2;
    private ClientHandler handler;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup1 = parentGroup.createSubGroup();
        subGroup2 = parentGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
        handler = new ClientHandler();
        
        parentGroup.addClientHandler(handler);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client1, "Hello"));
        parentGroup.add(client1);
        assertTrue(parentGroup.notifyMessageSent(client1, "Hello"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client1, "Hello"));
        parentGroup.add(client1);
        assertTrue(parentGroup.notifyMessageReceived(client1, "Hello"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client1, new Exception()));
        parentGroup.add(client1);
        assertTrue(parentGroup.notifyExceptionCaught(client1, new Exception()));
    }
    
    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client1));
        parentGroup.add(client1);
        assertTrue(parentGroup.contains(client1));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        parentGroup.add(client1);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertFalse(parentGroup.add(client2));
        assertTrue(subGroup1.add(client2));
    }
    
    @Test
    public void testRemove() {
        parentGroup.add(client1);
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.remove(client1));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.contains(newSubGroup.iterator().next()));
    }
    
    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
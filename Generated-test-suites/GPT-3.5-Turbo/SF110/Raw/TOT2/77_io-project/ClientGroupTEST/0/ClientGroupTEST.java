import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client1;
    private Client client2;
    private ClientHandler handler;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
        handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        parentGroup.add(client1);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client2, "Hello"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client2, "Hello"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client2, new Exception()));
    }
    
    @Test
    public void testContains() {
        assertTrue(parentGroup.contains(client1));
        assertFalse(parentGroup.contains(client2));
    }
    
    @Test
    public void testSize() {
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client2));
        assertFalse(parentGroup.add(client2));
    }
    
    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client2));
        assertTrue(parentGroup.remove(client1));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(subGroup);
    }
    
    @Test
    public void testAddClientHandler() {
        assertEquals(handler, parentGroup.handler);
    }
}
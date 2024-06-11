import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client1;
    private Client client2;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client1, "test message"));
        parentGroup.add(client1);
        assertTrue(parentGroup.notifyMessageSent(client1, "test message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client1, "test message"));
        parentGroup.add(client1);
        assertTrue(parentGroup.notifyMessageReceived(client1, "test message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client1, new Exception()));
        parentGroup.add(client1);
        assertTrue(parentGroup.notifyExceptionCaught(client1, new Exception()));
    }
    
    @Test
    public void testIterator() {
        parentGroup.add(client1);
        parentGroup.add(client2);
        int count = 0;
        for (Client client : parentGroup) {
            count++;
        }
        assertEquals(2, count);
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
        assertFalse(parentGroup.add(client1));
    }
    
    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client1));
        parentGroup.add(client1);
        assertTrue(parentGroup.remove(client1));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertEquals(0, parentGroup.size());
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
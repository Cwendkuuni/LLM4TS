import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {
    
    private ClientGroup topLevelGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler handler;
    
    @Before
    public void setUp() {
        topLevelGroup = new ClientGroup(null);
        subGroup = topLevelGroup.createSubGroup();
        emptyGroup = new ClientGroup(null);
        
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
        
        handler = new ClientHandler();
        
        topLevelGroup.add(client1);
        subGroup.add(client2);
        subGroup.add(client3);
        
        topLevelGroup.addClientHandler(handler);
    }
    
    @Test
    public void testParentGroup() {
        assertNull(topLevelGroup.parentGroup());
        assertEquals(topLevelGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertTrue(topLevelGroup.notifyMessageSent(client1, "Hello World"));
        assertFalse(topLevelGroup.notifyMessageSent(client2, "Hello World"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertTrue(topLevelGroup.notifyMessageReceived(client1, "Hello World"));
        assertFalse(topLevelGroup.notifyMessageReceived(client2, "Hello World"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertTrue(topLevelGroup.notifyExceptionCaught(client1, new Exception()));
        assertFalse(topLevelGroup.notifyExceptionCaught(client2, new Exception()));
    }
    
    @Test
    public void testIterator() {
        int count = 0;
        
        for (Client client : topLevelGroup) {
            count++;
        }
        
        assertEquals(1, count);
        
        count = 0;
        
        for (Client client : subGroup) {
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    public void testContains() {
        assertTrue(topLevelGroup.contains(client1));
        assertFalse(topLevelGroup.contains(client3));
    }
    
    @Test
    public void testSize() {
        assertEquals(1, topLevelGroup.size());
        assertEquals(2, subGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertFalse(topLevelGroup.add(client2));
        assertTrue(topLevelGroup.add(client3));
    }
    
    @Test
    public void testRemove() {
        assertFalse(topLevelGroup.remove(client3));
        assertTrue(topLevelGroup.remove(client1));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup newGroup = topLevelGroup.createSubGroup();
        assertNotNull(newGroup);
        assertEquals(topLevelGroup, newGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        handler = new ClientHandler();
        topLevelGroup.addClientHandler(handler);
        assertEquals(handler, topLevelGroup.handler);
    }
}

class Client {
}

class ClientHandler {
}
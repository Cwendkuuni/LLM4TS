import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    private ClientHandler handler;
    private Object message;
    private Throwable cause;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client = new Client();
        handler = new ClientHandler();
        message = new Object();
        cause = new Throwable();
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client, message));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, message));
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, message));
        
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client, message));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        Iterator<Client> iterator = parentGroup.iterator();
        assertFalse(iterator.hasNext());
        
        parentGroup.add(client);
        iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
    }
    
    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.contains(client));
        
        parentGroup.add(subGroup);
        assertFalse(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client));
        
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        assertFalse(subGroup.contains(client));
        
        parentGroup.add(subGroup);
        subGroup.remove(client);
        assertFalse(parentGroup.remove(client));
        assertFalse(subGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.contains(newSubGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
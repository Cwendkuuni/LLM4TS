import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private Client testClient;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptyGroup = new ClientGroup(null);
        testClient = new Client();
    }
    
    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
        assertEquals(parentGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(testClient, "Test Message"));
        parentGroup.add(testClient);
        assertTrue(parentGroup.notifyMessageSent(testClient, "Test Message"));
        
        subGroup.add(testClient);
        assertTrue(parentGroup.notifyMessageSent(testClient, "Test Message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(testClient, "Test Message"));
        parentGroup.add(testClient);
        assertTrue(parentGroup.notifyMessageReceived(testClient, "Test Message"));
        
        subGroup.add(testClient);
        assertTrue(parentGroup.notifyMessageReceived(testClient, "Test Message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(testClient, new Throwable()));
        parentGroup.add(testClient);
        assertTrue(parentGroup.notifyExceptionCaught(testClient, new Throwable()));
        
        subGroup.add(testClient);
        assertTrue(parentGroup.notifyExceptionCaught(testClient, new Throwable()));
    }
    
    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(testClient));
        parentGroup.add(testClient);
        assertTrue(parentGroup.contains(testClient));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        parentGroup.add(testClient);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(testClient));
        assertFalse(emptyGroup.add(testClient));
        
        parentGroup.remove(testClient);
        assertFalse(subGroup.add(testClient));
    }
    
    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(testClient));
        
        parentGroup.add(testClient);
        assertTrue(parentGroup.remove(testClient));
        
        emptyGroup.add(testClient);
        assertTrue(emptyGroup.remove(testClient));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        assertNull(parentGroup.handler);
        parentGroup.addClientHandler(new ClientHandler());
        assertNotNull(parentGroup.handler);
    }
}
import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {

    private ClientGroup testGroup;
    
    @Before
    public void setUp() {
        testGroup = new ClientGroup(null);
    }
    
    @Test
    public void testParentGroup() {
        assertNull(testGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = "Test Message";
        
        assertFalse(testGroup.notifyMessageSent(client, message));
        
        testGroup.add(client);
        assertTrue(testGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = "Test Message";
        
        assertFalse(testGroup.notifyMessageReceived(client, message));
        
        testGroup.add(client);
        assertTrue(testGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable("Test Exception");
        
        assertFalse(testGroup.notifyExceptionCaught(client, cause));
        
        testGroup.add(client);
        assertTrue(testGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        
        assertFalse(testGroup.contains(client));
        
        testGroup.add(client);
        assertTrue(testGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, testGroup.size());
        
        Client client = new Client();
        testGroup.add(client);
        assertEquals(1, testGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client = new Client();
        
        assertTrue(testGroup.add(client));
        assertTrue(testGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        
        assertFalse(testGroup.remove(client));
        
        testGroup.add(client);
        assertTrue(testGroup.remove(client));
        assertFalse(testGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = testGroup.createSubGroup();
        
        assertNotNull(subGroup.parentGroup());
        assertTrue(testGroup.iterator().next().equals(subGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        
        testGroup.addClientHandler(handler);
        assertEquals(handler, testGroup.handler);
    }

}
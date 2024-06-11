import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client = new Client();
        parentGroup.add(client);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertTrue(subGroup.notifyMessageSent(client, "Test message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertTrue(subGroup.notifyMessageReceived(client, "Test message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertTrue(subGroup.notifyExceptionCaught(client, new Exception("Test exception")));
    }
    
    @Test
    public void testContains() {
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client newClient = new Client();
        assertTrue(parentGroup.add(newClient));
        assertFalse(parentGroup.add(newClient));
    }
    
    @Test
    public void testRemove() {
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ClientGroupTest {
    
    private ClientGroup parentGroup;
    private ClientGroup clientGroup;
    private Client client;
    private ClientHandler handler;
    
    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        clientGroup = new ClientGroup(parentGroup);
        client = Mockito.mock(Client.class);
        handler = Mockito.mock(ClientHandler.class);
        clientGroup.addClientHandler(handler);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertTrue(clientGroup.notifyMessageSent(client, "Test message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertTrue(clientGroup.notifyMessageReceived(client, "Test message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertTrue(clientGroup.notifyExceptionCaught(client, new Exception("Test exception")));
    }
    
    @Test
    public void testContains() {
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(clientGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}
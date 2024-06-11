import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client;
    private Object message;
    
    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        client = new Client("TestClient");
        message = new Object();
    }
    
    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
        clientGroup = new ClientGroup(new ClientGroup(null));
        assertNotNull(clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(clientGroup.notifyMessageSent(client, message));

        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        ClientGroup childGroup = clientGroup.createSubGroup();
        childGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(clientGroup.notifyMessageReceived(client, message));

        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        
        ClientGroup childGroup = clientGroup.createSubGroup();
        childGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));

        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
        
        ClientGroup childGroup = clientGroup.createSubGroup();
        childGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
        
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());
        
        clientGroup.add(client);
        assertTrue(clientGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        assertFalse(clientGroup.contains(client));

        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());

        clientGroup.add(client);
        assertEquals(1, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client));
        assertFalse(clientGroup.add(client)); // Since client is already in the group
        
        Client duplicateClient = new Client("TestClient");
        assertFalse(clientGroup.add(duplicateClient)); // Since duplicateClient is not in the parent group
        
        clientGroup = new ClientGroup(new ClientGroup(null));
        assertTrue(clientGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        assertFalse(clientGroup.remove(client)); // Since client is not in the group
        
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        
        ClientGroup childGroup = clientGroup.createSubGroup();
        childGroup.add(client);
        assertTrue(clientGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNull(clientGroup.createSubGroup().parentGroup());
        
        clientGroup.add(new Client("SubTestClient"));
        assertNotNull(clientGroup.createSubGroup().parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        assertNull(clientGroup.handler);

        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
    
    private class Client {
        private String name;
        
        public Client(String name) {
            this.name = name;
        }
    }
    
    private class ClientHandler {
        
        public void messageSent(Client client, Object message) {
            // Implement messageSent method
        }
        
        public void messageReceived(Client client, Object message) {
            // Implement messageReceived method
        }
        
        public void exceptionCaught(Client client, Throwable cause) {
            // Implement exceptionCaught method
        }
        
        public void clientAdded(Client client) {
            // Implement clientAdded method
        }
        
        public void clientRemoved(Client client) {
            // Implement clientRemoved method
        }
    }
}
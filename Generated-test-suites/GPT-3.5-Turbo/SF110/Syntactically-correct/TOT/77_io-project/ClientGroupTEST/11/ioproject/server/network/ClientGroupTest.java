import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();
        assertFalse(clientGroup.notifyMessageSent(client, message));
        
        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageSent(client, message));

        ClientGroup subGroup1 = clientGroup.createSubGroup();
        subGroup1.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        clientGroup.remove(client);
        assertFalse(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.add(client);
        assertFalse(clientGroup.notifyMessageReceived(client, message));

        ClientGroup subGroup1 = clientGroup.createSubGroup();
        subGroup1.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.remove(client);
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        
        clientGroup.add(client);
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));

        ClientGroup subGroup1 = clientGroup.createSubGroup();
        subGroup1.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
        
        clientGroup.remove(client);
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());
        
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertTrue(clientGroup.iterator().hasNext());
        assertEquals(client1, clientGroup.iterator().next());
        assertEquals(client2, clientGroup.iterator().next());
    }

    @Test
    public void testContains() {
        Client client = new Client();
        assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
        
        clientGroup.remove(client);
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
        
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertEquals(2, clientGroup.size());
        
        clientGroup.remove(client1);
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
        assertTrue(clientGroup.add(client));
        
        ClientGroup subGroup1 = clientGroup.createSubGroup();
        assertTrue(subGroup1.add(client));
        
        clientGroup.remove(client);
        assertFalse(clientGroup.add(client));
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        assertFalse(clientGroup.remove(client));
        
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        
        ClientGroup subGroup1 = clientGroup.createSubGroup();
        subGroup1.add(client);
        assertFalse(clientGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup1 = clientGroup.createSubGroup();
        assertNotNull(subGroup1);
        
        ClientGroup subGroup2 = clientGroup.createSubGroup();
        assertNotNull(subGroup2);
        
        assertNotEquals(subGroup1, subGroup2);
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }

}
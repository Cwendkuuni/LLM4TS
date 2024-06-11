import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;

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
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_withSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        Object message = new Object();
    
        assertFalse(clientGroup.notifyMessageSent(client, message));
    
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
    
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_withSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        Object message = new Object();
    
        assertFalse(clientGroup.notifyMessageReceived(client, message));
    
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
    
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    
        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_withSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        Throwable cause = new Throwable();
    
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    
        subGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();
    
        clientGroup.add(client1);
        clientGroup.add(client2);
    
        HashSet<Client> expectedSet = new HashSet<>(Arrays.asList(client1, client2));
    
        assertEquals(expectedSet, new HashSet<>(clientGroup));
    }

    @Test
    public void testContains() {
        Client client = new Client();
    
        assertFalse(clientGroup.contains(client));
    
        clientGroup.add(client);
    
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        Client client1 = new Client();
        Client client2 = new Client();
    
        assertEquals(0, clientGroup.size());
    
        clientGroup.add(client1);
        clientGroup.add(client2);
    
        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
    
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
    
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertFalse(subGroup.add(client));
        assertTrue(subGroup.contains(client));
    }

    @Test
    public void testRemove() {
        Client client = new Client();
    
        assertFalse(clientGroup.remove(client));
    
        clientGroup.add(client);
    
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
    
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
    
        assertTrue(subGroup.remove(client));
        assertFalse(subGroup.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
    
        assertNotNull(subGroup);
        assertTrue(clientGroup.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
    
        clientGroup.addClientHandler(handler);
    
        assertEquals(handler, clientGroup.handler);
    }
}
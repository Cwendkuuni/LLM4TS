import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();

        ClientGroup clientGroup = new ClientGroup(null);

        assertFalse(clientGroup.notifyMessageSent(client, message));
        
        clientGroup.add(client);
        
        assertTrue(clientGroup.notifyMessageSent(client, message));
        // Test behavior when subGroups have been added
        
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();

        ClientGroup clientGroup = new ClientGroup(null);

        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        clientGroup.add(client);
        
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        // Test behavior when subGroups have been added
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();

        ClientGroup clientGroup = new ClientGroup(null);

        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        
        clientGroup.add(client);
        
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
        // Test behavior when subGroups have been added
    }

    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup clientGroup = new ClientGroup(null);

        clientGroup.add(client1);
        clientGroup.add(client2);
        
        Iterator<Client> iterator = clientGroup.iterator();

        assertEquals(client1, iterator.next());
        assertEquals(client2, iterator.next());
    }

    @Test
    public void testContains() {
        Client client = new Client();
        
        ClientGroup clientGroup = new ClientGroup(null);

        assertFalse(clientGroup.contains(client));
        
        clientGroup.add(client);
        
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup clientGroup = new ClientGroup(null);

        assertEquals(0, clientGroup.size());
        
        clientGroup.add(client1);
        clientGroup.add(client2);
        
        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
        
        ClientGroup clientGroup = new ClientGroup(null);

        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));

        ClientGroup subGroup = clientGroup.createSubGroup();
        assertFalse(subGroup.add(client));
        assertFalse(subGroup.contains(client));
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        
        ClientGroup clientGroup = new ClientGroup(null);

        assertFalse(clientGroup.remove(client));
        
        clientGroup.add(client);
        
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
        // Test behavior when subGroups have been added
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup clientGroup = new ClientGroup(null);

        ClientGroup subGroup = clientGroup.createSubGroup();

        assertNotNull(subGroup);
        assertTrue(clientGroup.iterator().hasNext());
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        
        ClientGroup clientGroup = new ClientGroup(null);

        assertNull(clientGroup.handler);
        
        clientGroup.addClientHandler(handler);
        
        assertEquals(handler, clientGroup.handler);
    }

}
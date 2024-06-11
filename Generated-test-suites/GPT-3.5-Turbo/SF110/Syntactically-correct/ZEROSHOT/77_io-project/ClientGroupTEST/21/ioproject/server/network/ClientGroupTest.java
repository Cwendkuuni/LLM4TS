import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client1;
    private Client client2;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        clientGroup.add(client1);
        assertFalse(clientGroup.notifyMessageSent(client2, "Hello"));
        assertTrue(clientGroup.notifyMessageSent(client1, "Hello"));
    }

    @Test
    public void testNotifyMessageReceived() {
        clientGroup.add(client1);
        assertFalse(clientGroup.notifyMessageReceived(client2, "Hello"));
        assertTrue(clientGroup.notifyMessageReceived(client1, "Hello"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        clientGroup.add(client1);
        assertFalse(clientGroup.notifyExceptionCaught(client2, new Throwable()));
        assertTrue(clientGroup.notifyExceptionCaught(client1, new Throwable()));
    }

    @Test
    public void testIterator() {
        clientGroup.add(client1);
        clientGroup.add(client2);
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        clientGroup.add(client1);
        assertFalse(clientGroup.contains(client2));
        assertTrue(clientGroup.contains(client1));
    }

    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
        clientGroup.add(client1);
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client1));
        assertFalse(clientGroup.add(client1));
        assertFalse(clientGroup.add(client2));
    }

    @Test
    public void testRemove() {
        clientGroup.add(client1);
        clientGroup.add(client2);
        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client2));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(clientGroup.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}

class ClientHandler {
    public void messageSent(Client client, Object message) {}
    public void messageReceived(Client client, Object message) {}
    public void exceptionCaught(Client client, Throwable cause) {}
}

class Client {}
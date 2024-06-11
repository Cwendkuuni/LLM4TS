import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientGroup subGroup;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
        subGroup = clientGroup.createSubGroup();
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
        assertEquals(clientGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(clientGroup.notifyMessageSent(client1, "message"));
        assertFalse(clientGroup.notifyMessageSent(client2, "message"));
        assertFalse(clientGroup.notifyMessageSent(client3, "message"));

        clientGroup.add(client1);
        clientGroup.add(client3);
        subGroup.add(client2);

        assertTrue(clientGroup.notifyMessageSent(client1, "message"));
        assertTrue(subGroup.notifyMessageSent(client2, "message"));
        assertFalse(subGroup.notifyMessageSent(client3, "message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(clientGroup.notifyMessageReceived(client1, "message"));
        assertFalse(clientGroup.notifyMessageReceived(client2, "message"));
        assertFalse(clientGroup.notifyMessageReceived(client3, "message"));

        clientGroup.add(client1);
        clientGroup.add(client3);
        subGroup.add(client2);

        assertTrue(clientGroup.notifyMessageReceived(client1, "message"));
        assertTrue(subGroup.notifyMessageReceived(client2, "message"));
        assertFalse(subGroup.notifyMessageReceived(client3, "message"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(clientGroup.notifyExceptionCaught(client1, new Throwable()));
        assertFalse(clientGroup.notifyExceptionCaught(client2, new Throwable()));
        assertFalse(clientGroup.notifyExceptionCaught(client3, new Throwable()));

        clientGroup.add(client1);
        clientGroup.add(client3);
        subGroup.add(client2);

        assertTrue(clientGroup.notifyExceptionCaught(client1, new Throwable()));
        assertTrue(subGroup.notifyExceptionCaught(client2, new Throwable()));
        assertFalse(subGroup.notifyExceptionCaught(client3, new Throwable()));
    }

    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());

        clientGroup.add(client1);
        clientGroup.add(client2);
        subGroup.add(client3);

        assertNotNull(clientGroup.iterator().next());
        assertNotNull(clientGroup.iterator().next());
        assertNotNull(subGroup.iterator().next());
        assertNull(subGroup.iterator().next());
    }

    @Test
    public void testContains() {
        assertFalse(clientGroup.contains(client1));

        clientGroup.add(client1);

        assertTrue(clientGroup.contains(client1));
    }

    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());

        clientGroup.add(client1);
        clientGroup.add(client2);

        assertEquals(2, clientGroup.size());
        assertEquals(0, subGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client1));
        assertFalse(clientGroup.add(client1));
        assertTrue(clientGroup.add(client2));
        assertFalse(clientGroup.add(client1));
        assertTrue(subGroup.add(client3));
        assertFalse(subGroup.add(client1));
        assertFalse(subGroup.add(client2));
    }

    @Test
    public void testRemove() {
        assertFalse(clientGroup.remove(client1));

        clientGroup.add(client1);
        clientGroup.add(client2);

        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client1));
        assertTrue(clientGroup.remove(client2));
    }

    @Test
    public void testCreateSubGroup() {
        subGroup = clientGroup.createSubGroup();

        assertTrue(clientGroup.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        assertNull(clientGroup.handler);

        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);

        assertEquals(handler, clientGroup.handler);
    }

    private class Client {
        // Empty stub
    }

    private class ClientHandler {
        // Empty stub
    }
}
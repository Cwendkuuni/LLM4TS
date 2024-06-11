import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private ClientHandler handler;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        handler = new ClientHandler() {
            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}

            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}
        };
        clientGroup.addClientHandler(handler);
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertEquals(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertFalse(clientGroup.notifyMessageSent(client1, "Test message"));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyMessageSent(client1, "Test message"));
        assertFalse(clientGroup.notifyMessageSent(client2, "Test message"));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.notifyMessageSent(client2, "Test message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertFalse(clientGroup.notifyMessageReceived(client1, "Test message"));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyMessageReceived(client1, "Test message"));
        assertFalse(clientGroup.notifyMessageReceived(client2, "Test message"));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.notifyMessageReceived(client2, "Test message"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertFalse(clientGroup.notifyExceptionCaught(client1, new Exception()));
        
        clientGroup.add(client1);
        assertTrue(clientGroup.notifyExceptionCaught(client1, new Exception()));
        assertFalse(clientGroup.notifyExceptionCaught(client2, new Exception()));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.notifyExceptionCaught(client2, new Exception()));
    }

    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        int count = 0;
        for (Client client : clientGroup) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testContains() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertFalse(clientGroup.contains(client1));
        assertFalse(clientGroup.contains(client2));

        clientGroup.add(client1);
        assertTrue(clientGroup.contains(client1));
        assertFalse(clientGroup.contains(client2));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertTrue(clientGroup.contains(client2));
    }

    @Test
    public void testSize() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertEquals(0, clientGroup.size());

        clientGroup.add(client1);
        assertEquals(1, clientGroup.size());

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertTrue(clientGroup.add(client1));
        assertFalse(clientGroup.add(client1));

        assertTrue(clientGroup.add(client2));

        ClientGroup subGroup = clientGroup.createSubGroup();
        assertFalse(subGroup.add(client2));
    }

    @Test
    public void testRemove() {
        Client client1 = new Client();
        Client client2 = new Client();
        assertFalse(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client2));

        clientGroup.add(client1);
        clientGroup.add(client2);
        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.contains(client1));

        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client2);
        subGroup.remove(client2);
        assertFalse(clientGroup.contains(client2));
    }

    @Test
    public void testCreateSubGroup() {
        assertNull(clientGroup.parentGroup());

        ClientGroup subGroup = clientGroup.createSubGroup();
        assertEquals(clientGroup, subGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        assertSame(handler, clientGroup.handler);
    }
}
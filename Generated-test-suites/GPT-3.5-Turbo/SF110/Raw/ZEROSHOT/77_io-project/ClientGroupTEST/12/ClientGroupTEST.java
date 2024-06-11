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
    public void testNotifyMessageSent_notInGroup() {
        Client client = new Client();
        assertFalse(clientGroup.notifyMessageSent(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageSent_inSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageSent_notInSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        assertFalse(clientGroup.notifyMessageSent(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageReceived_notInGroup() {
        Client client = new Client();
        assertFalse(clientGroup.notifyMessageReceived(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageReceived_inSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        subGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageReceived_notInSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        assertFalse(clientGroup.notifyMessageReceived(client, "Test Message"));
    }

    @Test
    public void testNotifyExceptionCaught_notInGroup() {
        Client client = new Client();
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    @Test
    public void testNotifyExceptionCaught_inSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        subGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    @Test
    public void testNotifyExceptionCaught_notInSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client client = new Client();
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    @Test
    public void testIterator() {
        assertNotNull(clientGroup.iterator());
    }

    @Test
    public void testContains() {
        Client client = new Client();
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
    }

    @Test
    public void testAdd() {
        Client client = new Client();
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        clientGroup.add(client);
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
        assertEquals(0, clientGroup.size());
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
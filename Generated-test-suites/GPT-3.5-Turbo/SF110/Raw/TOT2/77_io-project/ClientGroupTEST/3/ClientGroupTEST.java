import static org.junit.Assert.*;
import org.junit.Test;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private Client client1;
    private Client client2;
    private ClientGroup group;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
        group = new ClientGroup(parentGroup);
        group.addClient(client1);
    }

    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, group.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertTrue(group.notifyMessageSent(client1, "Message"));
        assertFalse(group.notifyMessageSent(client2, "Message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        assertTrue(group.notifyMessageReceived(client1, "Message"));
        assertFalse(group.notifyMessageReceived(client2, "Message"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Throwable cause = new Throwable();
        assertTrue(group.notifyExceptionCaught(client1, cause));
        assertFalse(group.notifyExceptionCaught(client2, cause));
    }

    @Test
    public void testIterator() {
        Iterator<Client> iterator = group.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
    }

    @Test
    public void testContains() {
        assertTrue(group.contains(client1));
        assertFalse(group.contains(client2));
    }

    @Test
    public void testSize() {
        assertEquals(1, group.size());
    }

    @Test
    public void testAdd() {
        assertFalse(group.add(client2));
        assertTrue(group.add(client1));
        assertEquals(1, group.size());
    }

    @Test
    public void testRemove() {
        assertFalse(group.remove(client2));
        assertTrue(group.remove(client1));
        assertEquals(0, group.size());
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = group.createSubGroup();
        assertTrue(group.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);
        assertEquals(handler, group.getHandler());
    }

    @After
    public void tearDown() {
        parentGroup = null;
        client1 = null;
        client2 = null;
        group = null;
    }
}
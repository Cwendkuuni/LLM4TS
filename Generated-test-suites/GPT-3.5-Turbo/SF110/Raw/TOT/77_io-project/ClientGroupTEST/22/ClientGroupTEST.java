import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup grandSubGroup;
    private Client client;
    private Object message;
    private Throwable throwable;
    private ClientHandler handler;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        grandSubGroup = subGroup.createSubGroup();
        client = new Client();
        message = new Object();
        throwable = new Throwable();
        handler = new ClientHandler();
    }

    @Test
    public void testParentGroup() {
        assertSame(null, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_withExistingClient() {
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_withNonExistingClient() {
        assertFalse(parentGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived_withExistingClient() {
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_withNonExistingClient() {
        assertFalse(parentGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught_withExistingClient() {
        parentGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, throwable));
    }

    @Test
    public void testNotifyExceptionCaught_withNonExistingClient() {
        assertFalse(parentGroup.notifyExceptionCaught(client, throwable));
    }

    @Test
    public void testIterator_emptyGroup() {
        assertFalse(parentGroup.iterator().hasNext());
    }

    @Test
    public void testIterator_nonEmptyGroup() {
        parentGroup.add(client);
        assertTrue(parentGroup.iterator().hasNext());
    }

    @Test
    public void testContains_withExistingClient() {
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testContains_withNonExistingClient() {
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testSize_withEmptyGroup() {
        assertEquals(0, parentGroup.size());
    }

    @Test
    public void testSize_withNonEmptyGroup() {
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAdd_withExistingParent() {
        parentGroup.add(client);
        assertTrue(parentGroup.add(client));
    }

    @Test
    public void testAdd_withNonExistingParent() {
        assertFalse(parentGroup.add(client));
    }

    @Test
    public void testRemove_withExistingClient() {
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
    }

    @Test
    public void testRemove_withNonExistingClient() {
        assertFalse(parentGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        assertNotNull(subGroup);
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertSame(handler, parentGroup.handler);
    }
}
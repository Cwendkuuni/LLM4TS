import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {

    private ClientGroup group;
    private Client client1;
    private Client client2;
    private ClientGroup subGroup;
    private ClientHandler handler;

    @Before
    public void setUp() {
        // Set up ClientGroup and clients for testing
        group = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
        subGroup = group.createSubGroup();
        handler = new ClientHandler();
        group.addClientHandler(handler);
    }

    @After
    public void tearDown() {
        // Clean up after testing
        group = null;
        client1 = null;
        client2 = null;
        subGroup = null;
        handler = null;
    }

    @Test
    public void testParentGroup() {
        // Check if parentGroup method returns the correct parent group
        assertNull(subGroup.parentGroup());
        assertEquals(group, subGroup.createSubGroup().parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Test calling notifyMessageSent method for different scenarios
        assertTrue(group.notifyMessageSent(client1, "Hello"));
        assertTrue(group.notifyMessageSent(client2, "World"));
        assertFalse(group.notifyMessageSent(new Client(), "Test"));
    }

    @Test
    public void testNotifyMessageReceived() {
        // Test calling notifyMessageReceived method for different scenarios
        assertTrue(group.notifyMessageReceived(client1, "Hello"));
        assertTrue(group.notifyMessageReceived(client2, "World"));
        assertFalse(group.notifyMessageReceived(new Client(), "Test"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Test calling notifyExceptionCaught method for different scenarios
        assertTrue(group.notifyExceptionCaught(client1, new Throwable()));
        assertTrue(group.notifyExceptionCaught(client2, new Throwable()));
        assertFalse(group.notifyExceptionCaught(new Client(), new Throwable()));
    }

    @Test
    public void testIterator() {
        // Test iterating through the clients in the group
        group.add(client1);
        group.add(client2);
        assertNotNull(group.iterator());
        assertTrue(group.iterator().hasNext());
    }

    @Test
    public void testContains() {
        // Test checking if a client is contained in the group
        group.add(client1);
        assertTrue(group.contains(client1));
        assertFalse(group.contains(client2));
    }

    @Test
    public void testSize() {
        // Test getting the size of the group
        group.add(client1);
        group.add(client2);
        assertEquals(2, group.size());
    }

    @Test
    public void testAdd() {
        // Test adding a client to the group
        assertTrue(group.add(client1));
        assertFalse(group.add(client1));
        assertFalse(group.add(new Client()));
    }

    @Test
    public void testRemove() {
        // Test removing a client from the group
        group.add(client1);
        group.add(client2);
        assertTrue(group.remove(client1));
        assertFalse(group.remove(client1));
        assertFalse(group.remove(new Client()));
    }

    @Test
    public void testCreateSubGroup() {
        // Test creating a sub group of the group
        assertNotNull(group.createSubGroup());
        assertNotNull(subGroup.createSubGroup());
    }

    @Test
    public void testAddClientHandler() {
        // Test adding a client handler to the group
        assertNotNull(group.handler);
        group.addClientHandler(null);
        assertNull(group.handler);
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }
}
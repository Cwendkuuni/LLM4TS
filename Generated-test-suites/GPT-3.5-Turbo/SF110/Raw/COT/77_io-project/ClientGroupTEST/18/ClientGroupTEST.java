import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    // Helper method to create a new instance of ClientGroup
    private ClientGroup createClientGroup() {
        return new ClientGroup(null);
    }

    @Test
    public void testParentGroup() {
        ClientGroup group = createClientGroup();
        assertNull(group.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // TODO: Write test cases for notifyMessageSent method
    }

    @Test
    public void testNotifyMessageReceived() {
        // TODO: Write test cases for notifyMessageReceived method
    }

    @Test
    public void testNotifyExceptionCaught() {
        // TODO: Write test cases for notifyExceptionCaught method
    }

    @Test
    public void testContains() {
        ClientGroup group = createClientGroup();
        Client client = new Client();
        assertFalse(group.contains(client));
        group.add(client);
        assertTrue(group.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup group = createClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        assertEquals(0, group.size());
        group.add(client1);
        assertEquals(1, group.size());
        group.add(client2);
        assertEquals(2, group.size());
        group.remove(client1);
        assertEquals(1, group.size());
    }

    @Test
    public void testAdd() {
        ClientGroup group = createClientGroup();
        Client client = new Client();
        assertTrue(group.add(client));
        assertTrue(group.contains(client));
    }

    @Test
    public void testRemove() {
        ClientGroup group = createClientGroup();
        Client client = new Client();
        group.add(client);
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup group = createClientGroup();
        ClientGroup subGroup = group.createSubGroup();
        assertNotNull(subGroup);
        assertEquals(group, subGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup group = createClientGroup();
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }
}


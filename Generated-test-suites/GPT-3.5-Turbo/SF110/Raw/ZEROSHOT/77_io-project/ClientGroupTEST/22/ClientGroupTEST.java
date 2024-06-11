import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        assertEquals(parentGroup, group.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(group.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(group.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(group.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        assertNotNull(group.iterator());
    }

    @Test
    public void testContains() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.contains(client));
        group.add(client);
        assertTrue(group.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        assertEquals(0, group.size());
        Client client = new Client();
        group.add(client);
        assertEquals(1, group.size());
    }

    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertTrue(group.add(client));
        assertTrue(group.contains(client));
        assertFalse(group.add(client));
    }

    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.remove(client));
        group.add(client);
        assertTrue(group.remove(client));
        assertFalse(group.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        assertTrue(group.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }
}
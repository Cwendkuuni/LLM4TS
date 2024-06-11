import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    // Test case for parentGroup()
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = new ClientGroup(parentGroup);

        assertEquals(parentGroup, subGroup.parentGroup());
    }

    // Test case for notifyMessageSent()
    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        boolean result = parentGroup.notifyMessageSent(client, message);

        assertFalse(result);

        parentGroup.add(client);
        result = parentGroup.notifyMessageSent(client, message);

        assertTrue(result);
    }

    // Test case for notifyMessageReceived()
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        boolean result = parentGroup.notifyMessageReceived(client, message);

        assertFalse(result);

        parentGroup.add(client);
        result = parentGroup.notifyMessageReceived(client, message);

        assertTrue(result);
    }

    // Test case for notifyExceptionCaught()
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        boolean result = parentGroup.notifyExceptionCaught(client, cause);

        assertFalse(result);

        parentGroup.add(client);
        result = parentGroup.notifyExceptionCaught(client, cause);

        assertTrue(result);
    }

    // Test case for contains()
    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        boolean result = parentGroup.contains(client);

        assertFalse(result);

        parentGroup.add(client);
        result = parentGroup.contains(client);

        assertTrue(result);
    }

    // Test case for size()
    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        int size = parentGroup.size();

        assertEquals(0, size);

        parentGroup.add(client);
        size = parentGroup.size();

        assertEquals(1, size);
    }

    // Test case for add()
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        boolean result = parentGroup.add(client);

        assertTrue(result);
        assertTrue(parentGroup.contains(client));
    }

    // Test case for remove()
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        boolean result = parentGroup.remove(client);

        assertFalse(result);

        parentGroup.add(client);
        result = parentGroup.remove(client);

        assertTrue(result);
        assertFalse(parentGroup.contains(client));
    }

    // Test case for createSubGroup()
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();

        assertNotNull(subGroup);
        assertTrue(parentGroup.contains(subGroup));
    }

    // Test case for addClientHandler()
    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();

        parentGroup.addClientHandler(handler);

        assertEquals(handler, parentGroup.handler);
    }
}
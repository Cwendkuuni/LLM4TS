
import static org.junit.Assert.*;
import org.junit.Test;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);

        assertEquals(parentGroup, childGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();

        assertFalse(childGroup.notifyMessageSent(client, message));

        parentGroup.add(client);
        assertTrue(childGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();

        assertFalse(childGroup.notifyMessageReceived(client, message));

        parentGroup.add(client);
        assertTrue(childGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Throwable cause = new Throwable();

        assertFalse(childGroup.notifyExceptionCaught(client, cause));

        parentGroup.add(client);
        assertTrue(childGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();

        assertFalse(childGroup.contains(client));

        parentGroup.add(client);
        assertTrue(childGroup.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();

        assertEquals(0, childGroup.size());

        parentGroup.add(client1);
        assertEquals(1, childGroup.size());

        childGroup.add(client2);
        assertEquals(2, childGroup.size());
    }

    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        ClientHandler handler = new ClientHandler();

        assertTrue(childGroup.add(client));
        assertTrue(childGroup.contains(client));

        childGroup.addClientHandler(handler);
        assertTrue(handler.clientAddedCalled);
    }

    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        ClientHandler handler = new ClientHandler();

        assertFalse(childGroup.remove(client));

        parentGroup.add(client);
        assertTrue(childGroup.remove(client));
        assertFalse(childGroup.contains(client));

        parentGroup.addClientHandler(handler);
        assertTrue(handler.clientRemovedCalled);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();

        assertNotNull(childGroup);
        assertSame(parentGroup, childGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);

        assertSame(handler, parentGroup.handler);
    }

    private class Client {
    }

    private class ClientHandler {
        public boolean clientAddedCalled = false;
        public boolean clientRemovedCalled = false;

        public void clientAdded(Client client) {
            clientAddedCalled = true;
        }

        public void clientRemoved(Client client) {
            clientRemovedCalled = true;
        }
    }

}

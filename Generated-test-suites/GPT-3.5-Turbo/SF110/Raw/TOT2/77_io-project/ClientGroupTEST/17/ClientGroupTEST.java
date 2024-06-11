import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.ref.WeakReference;

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
        Client client = new Client();
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertTrue(parentGroup.notifyMessageSent(client, "Hello"));
    }

    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertTrue(parentGroup.notifyMessageReceived(client, "Hello"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        parentGroup.add(client2);
        assertEquals(2, parentGroup.size());
    }

    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        assertTrue(parentGroup.add(client));
    }

    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.iterator().hasNext());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
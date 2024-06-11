import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testAddClient() {
        Client client = new Client();
        ClientGroup parentGroup = new ClientGroup(null);
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testRemoveClient() {
        Client client = new Client();
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = "Test Message";
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertTrue(parentGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = "Test Message";
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertTrue(parentGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable("Test Exception");
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotNull(subGroup);
    }

}
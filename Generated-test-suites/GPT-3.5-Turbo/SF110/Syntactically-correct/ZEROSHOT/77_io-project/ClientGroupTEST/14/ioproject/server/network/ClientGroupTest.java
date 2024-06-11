import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        client = new Client();
    }

    @Test
    public void parentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void notifyMessageSent() {
        assertFalse(clientGroup.notifyMessageSent(client, "Test Message"));
    }

    @Test
    public void notifyMessageReceived() {
        assertFalse(clientGroup.notifyMessageReceived(client, "Test Message"));
    }

    @Test
    public void notifyExceptionCaught() {
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    @Test
    public void iterator() {
        assertFalse(clientGroup.iterator().hasNext());
    }

    @Test
    public void contains() {
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void size() {
        assertEquals(0, clientGroup.size());
    }

    @Test
    public void add() {
        assertTrue(clientGroup.add(client));
    }

    @Test
    public void remove() {
        assertFalse(clientGroup.remove(client));
    }

    @Test
    public void createSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertNotNull(subGroup);
        assertTrue(clientGroup.contains(subGroup));
    }

    @Test
    public void addClientHandler() {
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.getHandler());
    }

}

class Client {}

class ClientHandler {}
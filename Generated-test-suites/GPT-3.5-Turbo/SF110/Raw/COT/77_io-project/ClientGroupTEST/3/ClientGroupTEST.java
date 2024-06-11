
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.ref.WeakReference;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private Client client;
    private Object message;
    private Throwable cause;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        client = new Client();
        message = new Object();
        cause = new Throwable();
    }

    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testIterator() {
        Iterator<Client> iterator = parentGroup.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client));
    }

    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotNull(subGroup);
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientGroup emptyGroup;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        emptyGroup = new ClientGroup(parentGroup);
        client = new Client();
        handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
    }

    @Test
    public void testParentGroup() {
        Assert.assertEquals(parentGroup, parentGroup.parentGroup());
        Assert.assertEquals(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        Assert.assertFalse(parentGroup.notifyMessageSent(client, "message"));
        parentGroup.add(client);
        Assert.assertTrue(parentGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        Assert.assertFalse(parentGroup.notifyMessageReceived(client, "message"));
        parentGroup.add(client);
        Assert.assertTrue(parentGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Assert.assertFalse(parentGroup.notifyExceptionCaught(client, new Throwable("error")));
        parentGroup.add(client);
        Assert.assertTrue(parentGroup.notifyExceptionCaught(client, new Throwable("error")));
    }

    @Test
    public void testIterator() {
        parentGroup.add(client);
        Iterator<Client> iterator = parentGroup.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(client, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        Assert.assertFalse(parentGroup.contains(client));
        parentGroup.add(client);
        Assert.assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(0, parentGroup.size());
        parentGroup.add(client);
        Assert.assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAdd() {
        Assert.assertTrue(parentGroup.add(client));
        Assert.assertEquals(1, parentGroup.size());
    }

    @Test
    public void testRemove() {
        Assert.assertFalse(parentGroup.remove(client));
        parentGroup.add(client);
        Assert.assertTrue(parentGroup.remove(client));
        Assert.assertEquals(0, parentGroup.size());
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        Assert.assertNotNull(newSubGroup);
        Assert.assertEquals(parentGroup, newSubGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.add(client);
        parentGroup.addClientHandler(handler);
    }
}

class Client {
}

class ClientHandler {
    void messageSent(Client client, Object message) {}
    void messageReceived(Client client, Object message) {}
    void exceptionCaught(Client client, Throwable cause) {}
    void clientAdded(Client client) {}
    void clientRemoved(Client client) {}
}

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client1;
    private Client client2;
    private ClientHandler handler;

    @Before
    public void setup() {
        clientGroup = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
        handler = new ClientHandler();
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_NotInGroup() {
        assertFalse(clientGroup.notifyMessageSent(client1, "message"));
    }

    @Test
    public void testNotifyMessageSent_InSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        assertTrue(clientGroup.notifyMessageSent(client1, "message"));
    }

    @Test
    public void testNotifyMessageSent_NoSubGroupHandler() {
        clientGroup.add(client1);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageSent(client1, "message"));
        assertEquals(1, handler.getMessageSentCount());
    }

    @Test
    public void testNotifyMessageSent_WithSubGroupHandler() {
        clientGroup.add(client1);
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageSent(client1, "message"));
        assertEquals(0, handler.getMessageSentCount());
    }

    @Test
    public void testNotifyMessageReceived_NotInGroup() {
        assertFalse(clientGroup.notifyMessageReceived(client1, "message"));
    }

    @Test
    public void testNotifyMessageReceived_InSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        assertTrue(clientGroup.notifyMessageReceived(client1, "message"));
    }

    @Test
    public void testNotifyMessageReceived_NoSubGroupHandler() {
        clientGroup.add(client1);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageReceived(client1, "message"));
        assertEquals(1, handler.getMessageReceivedCount());
    }

    @Test
    public void testNotifyMessageReceived_WithSubGroupHandler() {
        clientGroup.add(client1);
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyMessageReceived(client1, "message"));
        assertEquals(0, handler.getMessageReceivedCount());
    }

    @Test
    public void testNotifyExceptionCaught_NotInGroup() {
        assertFalse(clientGroup.notifyExceptionCaught(client1, new Throwable()));
    }

    @Test
    public void testNotifyExceptionCaught_InSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        assertTrue(clientGroup.notifyExceptionCaught(client1, new Throwable()));
    }

    @Test
    public void testNotifyExceptionCaught_NoSubGroupHandler() {
        clientGroup.add(client1);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyExceptionCaught(client1, new Throwable()));
        assertEquals(1, handler.getExceptionCaughtCount());
    }

    @Test
    public void testNotifyExceptionCaught_WithSubGroupHandler() {
        clientGroup.add(client1);
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        clientGroup.addClientHandler(handler);
        assertTrue(clientGroup.notifyExceptionCaught(client1, new Throwable()));
        assertEquals(0, handler.getExceptionCaughtCount());
    }

    @Test
    public void testIterator() {
        clientGroup.add(client1);
        clientGroup.add(client2);
        int count = 0;
        for (Client client : clientGroup) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testContains() {
        clientGroup.add(client1);
        assertTrue(clientGroup.contains(client1));
    }

    @Test
    public void testSize() {
        clientGroup.add(client1);
        clientGroup.add(client2);
        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(clientGroup.add(client1));
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd_AlreadyInParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(client1);
        clientGroup = new ClientGroup(parentGroup);
        assertFalse(clientGroup.add(client1));
        assertEquals(0, clientGroup.size());
    }

    @Test
    public void testRemove() {
        clientGroup.add(client1);
        assertTrue(clientGroup.remove(client1));
        assertEquals(0, clientGroup.size());
    }

    @Test
    public void testRemove_NotInGroup() {
        assertFalse(clientGroup.remove(client1));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertNotNull(subGroup);
        assertTrue(clientGroup.subGroups.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class ClientGroupTest {
    private static ClientGroup clientGroup;
    private static ClientGroup parentGroup;
    private static Client client1;
    private static Client client2;
    private static Object message;
    private static Throwable throwable;

    @BeforeClass
    public static void setUp() {
        parentGroup = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
        message = new Object();
        throwable = new Throwable();

        clientGroup = new ClientGroup(parentGroup);
        clientGroup.add(client1);
        clientGroup.add(client2);
    }

    @AfterClass
    public static void tearDown() {
        parentGroup = null;
        client1 = null;
        client2 = null;
        message = null;
        throwable = null;
        clientGroup = null;
    }

    // Test for parentGroup() method
    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, clientGroup.parentGroup());
    }

    // Test for notifyMessageSent() method when client is in sub group
    @Test
    public void testNotifyMessageSentWhenClientIsInSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        subGroup.add(client2);

        ClientHandler handler = new ClientHandler();
        subGroup.addClientHandler(handler);

        assertTrue(subGroup.notifyMessageSent(client1, message));
        assertTrue(handler.isMessageSent());
    }

    // Test for notifyMessageSent() method when client is not in sub group or parent group
    @Test
    public void testNotifyMessageSentWhenClientIsNotInSubGroupOrParentGroup() {
        Client client3 = new Client();

        // Remove client2 from clientGroup
        clientGroup.remove(client2);

        assertFalse(clientGroup.notifyMessageSent(client2, message));
        assertNull(clientGroup.handler);
    }

    // Test for notifyMessageReceived() method when client is in sub group
    @Test
    public void testNotifyMessageReceivedWhenClientIsInSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        subGroup.add(client2);

        ClientHandler handler = new ClientHandler();
        subGroup.addClientHandler(handler);

        assertTrue(subGroup.notifyMessageReceived(client1, message));
        assertTrue(handler.isMessageReceived());
    }

    // Test for notifyMessageReceived() method when client is not in sub group or parent group
    @Test
    public void testNotifyMessageReceivedWhenClientIsNotInSubGroupOrParentGroup() {
        Client client3 = new Client();

        // Remove client2 from clientGroup
        clientGroup.remove(client2);

        assertFalse(clientGroup.notifyMessageReceived(client2, message));
        assertNull(clientGroup.handler);
    }

    // Test for notifyExceptionCaught() method when client is in sub group
    @Test
    public void testNotifyExceptionCaughtWhenClientIsInSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);
        subGroup.add(client2);

        ClientHandler handler = new ClientHandler();
        subGroup.addClientHandler(handler);

        assertTrue(subGroup.notifyExceptionCaught(client1, throwable));
        assertTrue(handler.isExceptionCaught());
    }

    // Test for notifyExceptionCaught() method when client is not in sub group or parent group
    @Test
    public void testNotifyExceptionCaughtWhenClientIsNotInSubGroupOrParentGroup() {
        Client client3 = new Client();

        // Remove client2 from clientGroup
        clientGroup.remove(client2);

        assertFalse(clientGroup.notifyExceptionCaught(client2, throwable));
        assertNull(clientGroup.handler);
    }

    // Test for iterator() method
    @Test
    public void testIterator() {
        Iterator<Client> iterator = clientGroup.iterator();
        Set<Client> clientSet = new HashSet<>();

        while (iterator.hasNext()) {
            clientSet.add(iterator.next());
        }

        assertEquals(clientSet, clientGroup.clients);
    }

    // Test for contains() method when client is present
    @Test
    public void testContainsWhenClientIsPresent() {
        assertTrue(clientGroup.contains(client1));
    }

    // Test for contains() method when client is not present
    @Test
    public void testContainsWhenClientIsNotPresent() {
        Client client3 = new Client();

        assertFalse(clientGroup.contains(client3));
    }

    // Test for size() method
    @Test
    public void testSize() {
        assertEquals(2, clientGroup.size());
    }

    // Test for add() method when client is in parent group
    @Test
    public void testAddWhenClientIsInParentGroup() {
        ClientGroup newParentGroup = new ClientGroup(null);
        newParentGroup.add(client1);

        assertTrue(newParentGroup.add(client2));
        assertTrue(newParentGroup.contains(client2));
    }

    // Test for add() method when client is not in parent group
    @Test
    public void testAddWhenClientIsNotInParentGroup() {
        assertTrue(clientGroup.add(client1));
        assertFalse(clientGroup.contains(client1));
    }

    // Test for remove() method when client is a member
    @Test
    public void testRemoveWhenClientIsMember() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client1);

        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.contains(client1));
        assertFalse(subGroup.contains(client1));
    }

    // Test for remove() method when client is not a member
    @Test
    public void testRemoveWhenClientIsNotMember() {
        Client client3 = new Client();

        assertFalse(clientGroup.remove(client3));
    }

    // Test for createSubGroup() method
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();

        assertNotNull(subGroup);
        assertTrue(clientGroup.subGroups.contains(subGroup));
    }

    // Test for addClientHandler() method
    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);

        assertEquals(handler, clientGroup.handler);
    }
}
import static org.junit.Assert.*;
import java.lang.ref.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ClientGroupTest {

    private ClientGroup testGroup;
    private ClientGroup parentGroup;
    private Set<Client> clients;
    private ClientHandler handler;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        testGroup = new ClientGroup(parentGroup);
        clients = new LinkedHashSet<Client>();
        handler = new ClientHandler();

        testGroup.addClientHandler(handler);
    }

    @Test
    public void testParentGroup() {
        assertSame(parentGroup, testGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_NotInGroup() {
        Client client = new Client();
        Object message = new Object();

        assertFalse(testGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_NotInSubGroupOrHandler() {
        Client client = new Client();
        Object message = new Object();
        Client subClient = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();

        assertFalse(testGroup.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSent_InSubGroup() {
        Client client = new Client();
        Object message = new Object();
        Client subClient = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();
        subGroup.add(subClient);

        assertTrue(testGroup.notifyMessageSent(subClient, message));
    }

    @Test
    public void testNotifyMessageSent_InHandler() {
        Client client = new Client();
        Object message = new Object();

        assertTrue(testGroup.notifyMessageSent(client, message));
        assertEquals(1, handler.getSentMessages().size());
        assertEquals(message, handler.getSentMessages().get(0));
    }

    @Test
    public void testNotifyMessageReceived_NotInGroup() {
        Client client = new Client();
        Object message = new Object();

        assertFalse(testGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_NotInSubGroupOrHandler() {
        Client client = new Client();
        Object message = new Object();
        Client subClient = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();

        assertFalse(testGroup.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceived_InSubGroup() {
        Client client = new Client();
        Object message = new Object();
        Client subClient = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();
        subGroup.add(subClient);

        assertTrue(testGroup.notifyMessageReceived(subClient, message));
    }

    @Test
    public void testNotifyMessageReceived_InHandler() {
        Client client = new Client();
        Object message = new Object();

        assertTrue(testGroup.notifyMessageReceived(client, message));
        assertEquals(1, handler.getReceivedMessages().size());
        assertEquals(message, handler.getReceivedMessages().get(0));
    }

    @Test
    public void testNotifyExceptionCaught_NotInGroup() {
        Client client = new Client();
        Throwable cause = new Throwable();

        assertFalse(testGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_NotInSubGroupOrHandler() {
        Client client = new Client();
        Throwable cause = new Throwable();
        Client subClient = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();

        assertFalse(testGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_InSubGroup() {
        Client client = new Client();
        Throwable cause = new Throwable();
        Client subClient = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();
        subGroup.add(subClient);

        assertTrue(testGroup.notifyExceptionCaught(subClient, cause));
    }

    @Test
    public void testNotifyExceptionCaught_InHandler() {
        Client client = new Client();
        Throwable cause = new Throwable();

        assertTrue(testGroup.notifyExceptionCaught(client, cause));
        assertEquals(1, handler.getExceptions().size());
        assertEquals(cause, handler.getExceptions().get(0));
    }

    @Test
    public void testIterator() {
        Iterator<Client> iterator = testGroup.iterator();
        assertFalse(iterator.hasNext());

        Client client = new Client();
        testGroup.add(client);

        iterator = testGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
    }

    @Test
    public void testContains() {
        Client client = new Client();
        assertFalse(testGroup.contains(client));

        testGroup.add(client);
        assertTrue(testGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, testGroup.size());

        testGroup.add(new Client());
        testGroup.add(new Client());

        assertEquals(2, testGroup.size());
    }

    @Test
    public void testAdd_NotInParentGroup() {
        Client client = new Client();
        assertTrue(testGroup.add(client));
        assertTrue(testGroup.contains(client));
    }

    @Test
    public void testAdd_InParentGroup() {
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(testGroup.add(client));
        assertTrue(testGroup.contains(client));
    }

    @Test
    public void testAddClientHandler() {
        Client client = new Client();
        handler = new ClientHandler();
        testGroup.addClientHandler(handler);

        testGroup.add(client);
        assertEquals(1, handler.getClientsAdded().size());
        assertEquals(client, handler.getClientsAdded().get(0));
    }

    @Test
    public void testRemove_NotInGroup() {
        Client client = new Client();
        assertFalse(testGroup.remove(client));
    }

    @Test
    public void testRemove_InGroup() {
        Client client = new Client();
        testGroup.add(client);
        assertTrue(testGroup.remove(client));
        assertFalse(testGroup.contains(client));
    }

    @Test
    public void testRemove_InSubGroup() {
        Client client = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();
        subGroup.add(client);
        assertTrue(testGroup.remove(client));
        assertFalse(testGroup.contains(client));
        assertFalse(subGroup.contains(client));
    }

    @Test
    public void testRemove_InHandler() {
        Client client = new Client();
        testGroup.add(client);
        assertTrue(testGroup.remove(client));
        assertFalse(testGroup.contains(client));
        assertEquals(1, handler.getClientsRemoved().size());
        assertEquals(client, handler.getClientsRemoved().get(0));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = testGroup.createSubGroup();
        assertNotNull(subGroup);
        assertTrue(testGroup.contains(subGroup));
    }
}

class Client {
    // implementation of Client class
}

class ClientHandler {
    private List<Object> sentMessages;
    private List<Object> receivedMessages;
    private List<Throwable> exceptions;
    private List<Client> clientsAdded;
    private List<Client> clientsRemoved;

    public ClientHandler() {
        sentMessages = new ArrayList<Object>();
        receivedMessages = new ArrayList<Object>();
        exceptions = new ArrayList<Throwable>();
        clientsAdded = new ArrayList<Client>();
        clientsRemoved = new ArrayList<Client>();
    }

    public List<Object> getSentMessages() {
        return sentMessages;
    }

    public List<Object> getReceivedMessages() {
        return receivedMessages;
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    public List<Client> getClientsAdded() {
        return clientsAdded;
    }

    public List<Client> getClientsRemoved() {
        return clientsRemoved;
    }

    public void messageSent(Client client, Object message) {
        sentMessages.add(message);
    }

    public void messageReceived(Client client, Object message) {
        receivedMessages.add(message);
    }

    public void exceptionCaught(Client client, Throwable cause) {
        exceptions.add(cause);
    }

    public void clientAdded(Client client) {
        clientsAdded.add(client);
    }

    public void clientRemoved(Client client) {
        clientsRemoved.add(client);
    }
}
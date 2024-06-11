import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private ClientHandlerMock clientHandler;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        clientHandler = new ClientHandlerMock();
        clientGroup.addClientHandler(clientHandler);
    }

    @After
    public void tearDown() {
        clientGroup = null;
        clientHandler = null;
    }

    @Test
    public void testClientGroup() {
        assertNull(clientGroup.parentGroup());
        assertNotNull(clientGroup.iterator());
        assertEquals(clientGroup.size(), 0);
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();

        assertFalse(clientGroup.notifyMessageSent(client, message));

        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));

        assertTrue(clientHandler.isMessageSentCalled());
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();

        assertFalse(clientGroup.notifyMessageReceived(client, message));

        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, message));

        assertTrue(clientHandler.isMessageReceivedCalled());
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();

        assertFalse(clientGroup.notifyExceptionCaught(client, cause));

        clientGroup.add(client);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));

        assertTrue(clientHandler.isExceptionCaughtCalled());
    }

    @Test
    public void testIterator() {
        assertFalse(clientGroup.iterator().hasNext());

        Client client1 = new Client();
        clientGroup.add(client1);
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), client1);
        assertFalse(iterator.hasNext());

        Client client2 = new Client();
        clientGroup.add(client2);
        iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), client1);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), client2);
        assertFalse(iterator.hasNext());

    }

    @Test
    public void testContains() {
        Client client = new Client();

        assertFalse(clientGroup.contains(client));

        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(clientGroup.size(), 0);

        Client client = new Client();
        clientGroup.add(client);
        assertEquals(clientGroup.size(), 1);
    }

    @Test
    public void testAdd() {
        Client client = new Client();

        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));

        assertTrue(clientHandler.isClientAddedCalled());

        assertFalse(clientGroup.add(client));
    }

    @Test
    public void testRemove() {
        Client client = new Client();

        assertFalse(clientGroup.remove(client));

        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));

        assertTrue(clientHandler.isClientRemovedCalled());
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();

        assertNotNull(subGroup);
        assertEquals(clientGroup, subGroup.parentGroup());
        assertTrue(clientGroup.size() > 0);
    }

    @Test
    public void testAddClientHandler() {
        ClientHandlerMock customHandler = new ClientHandlerMock();
        clientGroup.addClientHandler(customHandler);

        assertNotNull(clientGroup.handler);
        assertTrue(clientHandler.isClientAddedCalled());
        assertTrue(customHandler.isClientAddedCalled());
    }
}
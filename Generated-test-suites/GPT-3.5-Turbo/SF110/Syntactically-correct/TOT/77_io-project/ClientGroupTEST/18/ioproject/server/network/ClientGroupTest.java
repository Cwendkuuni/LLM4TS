import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
   
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
    }

    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup()); // Testing parent group when parent group is null
    }

    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        assertTrue(parentGroup.add(client)); // Adding a client to the parent group
        assertTrue(parentGroup.notifyMessageSent(client, "Test message")); // Notifying a message sent

        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        assertTrue(parentGroup.notifyMessageSent(client, "Test message")); // Notifying a message sent with a handler
        
        assertEquals(1, handlerMock.getMessageSentCount()); // Checking the handler's messageSent count
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        assertTrue(parentGroup.add(client)); // Adding a client to the parent group
        assertTrue(parentGroup.notifyMessageReceived(client, "Test message")); // Notifying a message received

        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        assertTrue(parentGroup.notifyMessageReceived(client, "Test message")); // Notifying a message received with a handler
        
        assertEquals(1, handlerMock.getMessageReceivedCount()); // Checking the handler's messageReceived count
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        assertTrue(parentGroup.add(client)); // Adding a client to the parent group
        assertTrue(parentGroup.notifyExceptionCaught(client, new Exception("Test exception"))); // Notifying an exception caught

        ClientHandlerMock handlerMock = new ClientHandlerMock();
        parentGroup.addClientHandler(handlerMock);
        assertTrue(parentGroup.notifyExceptionCaught(client, new Exception("Test exception"))); // Notifying an exception caught with a handler
        
        assertEquals(1, handlerMock.getExceptionCaughtCount()); // Checking the handler's exceptionCaught count
    }

    @Test
    public void testIterator() {
        Client client1 = new Client();
        parentGroup.add(client1); // Adding a client to the parent group
        Iterator<Client> iterator = parentGroup.iterator(); // Getting an iterator

        assertTrue(iterator.hasNext()); // Checking if there is a next element
        assertEquals(client1, iterator.next()); // Checking if the next element is the added client

        Client client2 = new Client();
        parentGroup.add(client2); // Adding another client to the parent group

        assertTrue(iterator.hasNext()); // Checking if there is a next element
        assertEquals(client2, iterator.next()); // Checking if the next element is the newly added client

        assertFalse(iterator.hasNext()); // Checking if there are no more elements
    }

    @Test
    public void testContains() {
        Client client = new Client();
        assertFalse(parentGroup.contains(client)); // Checking if the parent group does not contain the client

        parentGroup.add(client); // Adding a client to the parent group
        assertTrue(parentGroup.contains(client)); // Checking if the parent group contains the client
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size()); // Checking the size of the parent group when it is empty

        Client client = new Client();
        parentGroup.add(client); // Adding a client to the parent group

        assertEquals(1, parentGroup.size()); // Checking the size of the parent group after adding a client
    }

    @Test
    public void testAdd() {
        Client client = new Client();

        assertTrue(parentGroup.add(client)); // Adding a client to the parent group
        assertTrue(parentGroup.contains(client)); // Checking if the client is added to the parent group

        Client duplicateClient = new Client();
        assertFalse(parentGroup.add(duplicateClient)); // Adding a duplicate client to the parent group should fail
    }

    @Test
    public void testRemove() {
        Client client = new Client();
        assertFalse(parentGroup.remove(client)); // Removing a client from the parent group when it is not present should fail

        parentGroup.add(client); // Adding a client to the parent group
        assertTrue(parentGroup.remove(client)); // Removing a client from the parent group
        assertFalse(parentGroup.contains(client)); // Checking if the client is removed from the parent group
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup(); // Creating a sub group
        
        assertEquals(parentGroup, subGroup.parentGroup()); // Checking the parent group of the sub group
    }

    @Test
    public void testAddClientHandler() {
        ClientHandlerMock handlerMock = new ClientHandlerMock();
        assertNull(parentGroup.handler); // Checking that the handler is initially null

        parentGroup.addClientHandler(handlerMock); // Adding a client handler
        assertEquals(handlerMock, parentGroup.handler); // Checking if the handler is added
    }

    // Helper class for testing

    private class ClientHandlerMock implements ClientHandler {
        private int messageSentCount;
        private int messageReceivedCount;
        private int exceptionCaughtCount;
        
        public ClientHandlerMock() {
            messageSentCount = 0;
            messageReceivedCount = 0;
            exceptionCaughtCount = 0;
        }

        public void messageSent(Client client, Object message) {
            messageSentCount++;
        }

        public void messageReceived(Client client, Object message) {
            messageReceivedCount++;
        }

        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtCount++;
        }

        public void clientAdded(Client client) {}

        public void clientRemoved(Client client) {}

        public int getMessageSentCount() {
            return messageSentCount;
        }

        public int getMessageReceivedCount() {
            return messageReceivedCount;
        }

        public int getExceptionCaughtCount() {
            return exceptionCaughtCount;
        }
    }
}
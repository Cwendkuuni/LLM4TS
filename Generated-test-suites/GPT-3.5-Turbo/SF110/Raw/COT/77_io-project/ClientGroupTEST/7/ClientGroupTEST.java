import ioproject.server.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import org.junit.Test;
import java.util.Iterator;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        // Instantiate ClientGroup object
        // Create parent group
        // Set parent group using constructor
        // Call parentGroup() method
        // Assert the returned value is equal to the parent group object
    }

    @Test
    public void testNotifyMessageSent() {
        // Instantiate ClientGroup object
        // Create client and message objects
        // Add client to the ClientGroup object
        // Call notifyMessageSent() method with client and message as parameters
        // Assert the returned value is true
    }

    @Test
    public void testNotifyMessageReceived() {
        // Instantiate ClientGroup object
        // Create client and message objects
        // Add client to the ClientGroup object
        // Call notifyMessageReceived() method with client and message as parameters
        // Assert the returned value is true
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Instantiate ClientGroup object
        // Create client and cause objects
        // Add client to the ClientGroup object
        // Call notifyExceptionCaught() method with client and cause as parameters
        // Assert the returned value is true
    }

    @Test
    public void testIterator() {
        // Instantiate ClientGroup object
        // Create client objects
        // Add clients to the ClientGroup object
        // Call iterator() method
        // Loop through the iterator and assert each returned value is equal to a client object
    }

    @Test
    public void testContains() {
        // Instantiate ClientGroup object
        // Create client object
        // Add client to the ClientGroup object
        // Call contains() method with client as parameter
        // Assert the returned value is true
    }

    @Test
    public void testSize() {
        // Instantiate ClientGroup object
        // Create client objects
        // Add clients to the ClientGroup object
        // Call size() method
        // Assert the returned value is equal to the number of clients added
    }

    @Test
    public void testAdd() {
        // Instantiate ClientGroup object
        // Create client object
        // Call add() method with client as parameter
        // Assert the returned value is true
    }

    @Test
    public void testRemove() {
        // Instantiate ClientGroup object
        // Create client objects
        // Add clients to the ClientGroup object
        // Call remove() method with client as parameter
        // Assert the returned value is true
        // Assert the ClientGroup object no longer contains the removed client
    }

    @Test
    public void testCreateSubGroup() {
        // Instantiate ClientGroup object
        // Call createSubGroup() method
        // Assert the returned value is a new instance of ClientGroup
        // Assert the new instance is a sub-group of the ClientGroup object
    }

    @Test
    public void testAddClientHandler() {
        // Instantiate ClientGroup object
        // Create ClientHandler object
        // Call addClientHandler() method with ClientHandler as parameter
        // Assert the handler field of the ClientGroup object is equal to the added ClientHandler
    }
}
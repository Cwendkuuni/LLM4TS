import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup clientGroup;

    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
    }

    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Test when client is not in the group
        assertFalse(clientGroup.notifyMessageSent(new Client(), "Test message"));

        // Test message sending functionality
        Client client = new Client();
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, "Test message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        // Test when client is not in the group
        assertFalse(clientGroup.notifyMessageReceived(new Client(), "Test message"));

        // Test message receiving functionality
        Client client = new Client();
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageReceived(client, "Test message"));
    }

    // Additional test cases for other methods should be added here

}
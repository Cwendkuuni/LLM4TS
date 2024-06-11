import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.ref.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

    // Test for parentGroup() method
    @Test
    public void testParentGroup() {
        ClientGroup parent = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parent);
        assertEquals(parent, group.parentGroup());
    }

    // Test for notifyMessageSent() method
    @Test
    public void testNotifyMessageSent() {
        // Test when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyMessageSent(client, "Hello"));
        
        // Test when client is in the group
        group.add(client);
        assertTrue(group.notifyMessageSent(client, "Hello"));
    }

    // Test for notifyMessageReceived() method
    @Test
    public void testNotifyMessageReceived() {
        // Test when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyMessageReceived(client, "Hello"));
        
        // Test when client is in the group
        group.add(client);
        assertTrue(group.notifyMessageReceived(client, "Hello"));
    }

    // Test for notifyExceptionCaught() method
    @Test
    public void testNotifyExceptionCaught() {
        // Test when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyExceptionCaught(client, new Exception("Error")));
        
        // Test when client is in the group
        group.add(client);
        assertTrue(group.notifyExceptionCaught(client, new Exception("Error")));
    }

    // Add more test cases for the remaining methods...

}
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        // Test when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = Mockito.mock(Client.class);
        Object message = "Test message";
        
        assertFalse(group.notifyMessageSent(client, message));
        
        // Test when client is in the group
        group.add(client);
        assertTrue(group.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        // Test when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = Mockito.mock(Client.class);
        Object message = "Test message";
        
        assertFalse(group.notifyMessageReceived(client, message));
        
        // Test when client is in the group
        group.add(client);
        assertTrue(group.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        // Test when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = Mockito.mock(Client.class);
        Throwable cause = new Throwable("Test exception");
        
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        // Test when client is in the group
        group.add(client);
        assertTrue(group.notifyExceptionCaught(client, cause));
    }

    // Additional test cases for other methods follow
}
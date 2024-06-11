import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testClientGroupConstructor() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertNotNull(parentGroup);
    }

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertNull(parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        // Test when client is not in the group
        ClientGroup parentGroup = new ClientGroup(null);
        assertFalse(parentGroup.notifyMessageSent(new Client(), "Test message"));
        
        // Test when client is in the group
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "Test message"));
    }
    
    // Additional test cases for other public methods of ClientGroup class
    
}
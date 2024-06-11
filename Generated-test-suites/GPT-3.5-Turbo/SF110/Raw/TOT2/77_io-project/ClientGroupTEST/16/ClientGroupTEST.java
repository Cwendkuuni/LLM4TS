import static org.junit.Assert.*;
import org.junit.Test;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        // Test for when client is not in the group
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyMessageSent(client, "Test message"));
    }
    
    // Additional test cases for other methods can be added here
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.contains(subGroup.iterator().next()));
    }
    
}
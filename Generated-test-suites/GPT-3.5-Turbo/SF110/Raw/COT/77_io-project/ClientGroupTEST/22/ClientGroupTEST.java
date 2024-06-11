
import org.junit.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        
        Assert.assertEquals("Parent group does not match", parentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        Assert.assertFalse("Client should not exist in the group", clientGroup.notifyMessageSent(client, message));
        
        clientGroup.add(client);
        
        Assert.assertTrue("Client should exist in the group", clientGroup.notifyMessageSent(client, message));
        
        // Additional test for subgroups
        
        ClientGroup subGroup1 = clientGroup.createSubGroup();
        ClientGroup subGroup2 = clientGroup.createSubGroup();
        
        Client client2 = new Client();
        
        clientGroup.add(client2);
        subGroup1.add(client2);
        
        Assert.assertTrue("Client should exist in the subgroup", subGroup1.notifyMessageSent(client2, message));
        Assert.assertFalse("Client should not exist in the parent group", clientGroup.notifyMessageSent(client2, message));
    }
    
    // Additional test cases for other methods...
    
}


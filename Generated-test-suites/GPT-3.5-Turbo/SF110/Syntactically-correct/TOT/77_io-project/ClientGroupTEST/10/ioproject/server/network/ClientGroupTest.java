import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        Client client = new Client();
        Object message = new Object();
        
        // Test when client is not in the group
        assertFalse(childGroup.notifyMessageSent(client, message));
        
        // Test when client is in the group but there are no subgroups or handler
        childGroup.add(client);
        assertTrue(childGroup.notifyMessageSent(client, message));
        
        // Test when there are subgroups but the message is not sent to any subgroup
        ClientGroup subGroup1 = childGroup.createSubGroup();
        Client subGroupClient1 = new Client();
        subGroup1.add(subGroupClient1);
        
        ClientGroup subGroup2 = childGroup.createSubGroup();
        Client subGroupClient2 = new Client();
        subGroup2.add(subGroupClient2);
        
        assertTrue(childGroup.notifyMessageSent(client, message));
        assertNull(subGroup1.handler);
        assertNull(subGroup2.handler);
        
        // Test when the message is sent to a subgroup
        ChildHandler childHandler = new ChildHandler();
        childGroup.addClientHandler(childHandler);
        
        assertTrue(childGroup.notifyMessageSent(client, message));
        assertNull(subGroup1.handler);
        assertNull(subGroup2.handler);
        assertEquals(message, childHandler.getMessage());
        assertEquals(client, childHandler.getClient());
        
        // Test when the message is sent to a subgroup and the subgroup also has a handler
        SubGroupHandler subGroupHandler = new SubGroupHandler();
        subGroup2.addClientHandler(subGroupHandler);
        
        assertTrue(childGroup.notifyMessageSent(client, message));
        assertNull(subGroup1.handler);
        assertEquals(message, subGroupHandler.getMessage());
        assertEquals(subGroupClient2, subGroupHandler.getClient());
    }
    
    @Test
    public void testNotifyMessageReceived() {
        // Similar to testNotifyMessageSent, but testing notifyMessageReceived method
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        // Similar to testNotifyMessageSent, but testing notifyExceptionCaught method
    }
    
    // Rest of the test methods for the remaining methods in ClientGroup
        
    private class ChildHandler implements ClientHandler {
        private Client client;
        private Object message;
        
        @Override
        public void messageSent(Client client, Object message) {
            this.client = client;
            this.message = message;
        }
        
        // Rest of the methods in ClientHandler
    }
    
    private class SubGroupHandler implements ClientHandler {
        private Client client;
        private Object message;
        
        @Override
        public void messageSent(Client client, Object message) {
            this.client = client;
            this.message = message;
        }
        
        // Rest of the methods in ClientHandler
    }
}
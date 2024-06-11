import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;
import java.util.HashSet;

public class ClientGroupTest {
    
    private ClientGroup clientGroup;
    private Client client1;
    private Client client2;
    private ClientHandlerMock handler;
    
    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        client1 = new Client();
        client2 = new Client();
        handler = new ClientHandlerMock();
        clientGroup.addClientHandler(handler);
    }
    
    @Test
    public void testParentGroup() {
        Assert.assertNull(clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent_ClientInGroup() {
        clientGroup.add(client1);
        Object message = new Object();
        boolean result = clientGroup.notifyMessageSent(client1, message);
        Assert.assertTrue(result);
        Assert.assertEquals(client1, handler.sentClient);
        Assert.assertEquals(message, handler.sentMessage);
    }
    
    @Test
    public void testNotifyMessageSent_ClientNotInGroup() {
        boolean result = clientGroup.notifyMessageSent(client1, null);
        Assert.assertFalse(result);
        Assert.assertNull(handler.sentClient);
        Assert.assertNull(handler.sentMessage);
    }
    
    @Test
    public void testNotifyMessageSent_SubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client clientInSubGroup = new Client();
        subGroup.add(clientInSubGroup);
        Object message = new Object();
        boolean result = clientGroup.notifyMessageSent(clientInSubGroup, message);
        Assert.assertTrue(result);
        Assert.assertEquals(clientInSubGroup, handler.sentClient);
        Assert.assertEquals(message, handler.sentMessage);
    }
    
    @Test
    public void testNotifyMessageSent_NoHandler() {
        clientGroup.removeClientHandler();
        clientGroup.add(client1);
        Object message = new Object();
        boolean result = clientGroup.notifyMessageSent(client1, message);
        Assert.assertTrue(result);
        Assert.assertNull(handler.sentClient);
        Assert.assertNull(handler.sentMessage);
    }
    
    @Test
    public void testNotifyMessageReceived_ClientInGroup() {
        clientGroup.add(client1);
        Object message = new Object();
        boolean result = clientGroup.notifyMessageReceived(client1, message);
        Assert.assertTrue(result);
        Assert.assertEquals(client1, handler.receivedClient);
        Assert.assertEquals(message, handler.receivedMessage);
    }
    
    @Test
    public void testNotifyMessageReceived_ClientNotInGroup() {
        boolean result = clientGroup.notifyMessageReceived(client1, null);
        Assert.assertFalse(result);
        Assert.assertNull(handler.receivedClient);
        Assert.assertNull(handler.receivedMessage);
    }
    
    @Test
    public void testNotifyMessageReceived_SubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client clientInSubGroup = new Client();
        subGroup.add(clientInSubGroup);
        Object message = new Object();
        boolean result = clientGroup.notifyMessageReceived(clientInSubGroup, message);
        Assert.assertTrue(result);
        Assert.assertEquals(clientInSubGroup, handler.receivedClient);
        Assert.assertEquals(message, handler.receivedMessage);
    }
    
    @Test
    public void testNotifyMessageReceived_NoHandler() {
        clientGroup.removeClientHandler();
        clientGroup.add(client1);
        Object message = new Object();
        boolean result = clientGroup.notifyMessageReceived(client1, message);
        Assert.assertTrue(result);
        Assert.assertNull(handler.receivedClient);
        Assert.assertNull(handler.receivedMessage);
    }
    
    @Test
    public void testNotifyExceptionCaught_ClientInGroup() {
        clientGroup.add(client1);
        Throwable cause = new Throwable();
        boolean result = clientGroup.notifyExceptionCaught(client1, cause);
        Assert.assertTrue(result);
        Assert.assertEquals(client1, handler.caughtClient);
        Assert.assertEquals(cause, handler.caughtCause);
    }
    
    @Test
    public void testNotifyExceptionCaught_ClientNotInGroup() {
        boolean result = clientGroup.notifyExceptionCaught(client1, null);
        Assert.assertFalse(result);
        Assert.assertNull(handler.caughtClient);
        Assert.assertNull(handler.caughtCause);
    }
    
    @Test
    public void testNotifyExceptionCaught_SubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Client clientInSubGroup = new Client();
        subGroup.add(clientInSubGroup);
        Throwable cause = new Throwable();
        boolean result = clientGroup.notifyExceptionCaught(clientInSubGroup, cause);
        Assert.assertTrue(result);
        Assert.assertEquals(clientInSubGroup, handler.caughtClient);
        Assert.assertEquals(cause, handler.caughtCause);
    }
    
    @Test
    public void testNotifyExceptionCaught_NoHandler() {
        clientGroup.removeClientHandler();
        clientGroup.add(client1);
        Throwable cause = new Throwable();
        boolean result = clientGroup.notifyExceptionCaught(client1, cause);
        Assert.assertTrue(result);
        Assert.assertNull(handler.caughtClient);
        Assert.assertNull(handler.caughtCause);
    }
    
    @Test
    public void testIterator() {
        clientGroup.add(client1);
        clientGroup.add(client2);
        Iterator<Client> iterator = clientGroup.iterator();
        HashSet<Client> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        Assert.assertTrue(result.contains(client1));
        Assert.assertTrue(result.contains(client2));
    }
    
    @Test
    public void testContains_ClientInGroup() {
        clientGroup.add(client1);
        boolean result = clientGroup.contains(client1);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testContains_ClientNotInGroup() {
        boolean result = clientGroup.contains(client1);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testSize_EmptyGroup() {
        int result = clientGroup.size();
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testSize_NonEmptyGroup() {
        clientGroup.add(client1);
        clientGroup.add(client2);
        int result = clientGroup.size();
        Assert.assertEquals(2, result);
    }
    
    @Test
    public void testAdd_ParentContainsClient() {
        clientGroup.add(client1);
        boolean result = clientGroup.add(client2);
        Assert.assertTrue(result);
        Assert.assertTrue(clientGroup.contains(client2));
    }
    
    @Test
    public void testAdd_ParentDoesNotContainClient() {
        clientGroup.remove(client1);
        boolean result = clientGroup.add(client1);
        Assert.assertFalse(result);
        Assert.assertFalse(clientGroup.contains(client1));
    }
    
    @Test
    public void testRemove_ClientIsMember() {
        clientGroup.add(client1);
        boolean result = clientGroup.remove(client1);
        Assert.assertTrue(result);
        Assert.assertFalse(clientGroup.contains(client1));
    }
    
    @Test
    public void testRemove_ClientIsNotMember() {
        boolean result = clientGroup.remove(client1);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        Assert.assertNotNull(subGroup);
        Assert.assertTrue(clientGroup.contains(subGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        ClientHandlerMock newHandler = new ClientHandlerMock();
        clientGroup.addClientHandler(newHandler);
        Assert.assertEquals(newHandler, clientGroup.getClientHandler());
    }
    
    private static class ClientHandlerMock implements ClientHandler {
        
        private Client sentClient;
        private Object sentMessage;
        private Client receivedClient;
        private Object receivedMessage;
        private Client caughtClient;
        private Throwable caughtCause;
        
        @Override
        public void messageSent(Client client, Object message) {
            sentClient = client;
            sentMessage = message;
        }
        
        @Override
        public void messageReceived(Client client, Object message) {
            receivedClient = client;
            receivedMessage = message;
        }
        
        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            caughtClient = client;
            caughtCause = cause;
        }
    }
}
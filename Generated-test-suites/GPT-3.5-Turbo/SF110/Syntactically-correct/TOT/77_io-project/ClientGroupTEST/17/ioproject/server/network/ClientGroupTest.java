import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    
    private ClientGroup clientGroup;
    
    @Before
    public void setup() {
        clientGroup = new ClientGroup(null);
    }
    
    @Test
    public void testParentGroup() {
        Assert.assertNull(clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Client client = new Client();
        Object message = new Object();
        
        boolean result = clientGroup.notifyMessageSent(client, message);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = new Client();
        Object message = new Object();
        
        boolean result = clientGroup.notifyMessageReceived(client, message);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = new Client();
        Throwable cause = new Throwable();
        
        boolean result = clientGroup.notifyExceptionCaught(client, cause);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        Assert.assertNotNull(clientGroup.iterator());
    }
    
    @Test
    public void testContains() {
        Client client = new Client();
        
        boolean result = clientGroup.contains(client);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testSize() {
        int size = clientGroup.size();
        
        Assert.assertEquals(0, size);
    }
    
    @Test
    public void testAdd() {
        Client client = new Client();
        
        boolean result = clientGroup.add(client);
        
        Assert.assertTrue(result);
        Assert.assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        Client client = new Client();
        
        boolean result = clientGroup.remove(client);
        
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
        ClientHandler handler = new ClientHandler();
        
        clientGroup.addClientHandler(handler);
        
        Assert.assertEquals(handler, clientGroup.handler);
    }
}
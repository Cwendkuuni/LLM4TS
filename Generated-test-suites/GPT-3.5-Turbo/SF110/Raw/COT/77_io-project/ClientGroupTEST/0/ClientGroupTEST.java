
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(group.notifyMessageSent(client, message));
        
        group.add(client);
        
        assertTrue(group.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        
        assertFalse(group.notifyMessageReceived(client, message));
        
        group.add(client);
        
        assertTrue(group.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        group.add(client);
        
        assertTrue(group.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testContains() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        assertFalse(group.contains(client));
        
        group.add(client);
        
        assertTrue(group.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        
        group.add(client1);
        group.add(client2);
        group.add(client3);
        
        assertEquals(3, group.size());
        
        group.remove(client1);
        
        assertEquals(2, group.size());
    }

    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        
        assertTrue(group.add(client1));
        assertTrue(group.add(client2));
        assertTrue(group.add(client3));
    }

    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        
        group.add(client1);
        group.add(client2);
        group.add(client3);
        
        assertTrue(group.remove(client1));
        assertFalse(group.remove(client1));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        
        assertNotNull(subGroup);
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        group.addClientHandler(handler);
        
        assertEquals(handler, group.handler);
    }

}

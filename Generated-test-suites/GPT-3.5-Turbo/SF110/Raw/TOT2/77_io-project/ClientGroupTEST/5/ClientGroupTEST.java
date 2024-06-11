import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.ref.*;
import ioproject.common.network.*;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(parentGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(parentGroup.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(parentGroup.notifyExceptionCaught(client, cause));
    }
    
    @Test
    public void testIterator() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertNotNull(parentGroup.iterator());
    }
    
    @Test
    public void testContains() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(0, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        assertTrue(parentGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(parentGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertNotNull(parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
            }
            @Override
            public void messageReceived(Client client, Object message) {
            }
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
            }
            @Override
            public void clientAdded(Client client) {
            }
            @Override
            public void clientRemoved(Client client) {
            }
        };
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
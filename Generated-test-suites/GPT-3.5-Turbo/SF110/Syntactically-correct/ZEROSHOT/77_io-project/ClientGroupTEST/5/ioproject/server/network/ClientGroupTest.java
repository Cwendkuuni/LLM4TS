import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.ref.*;
import java.util.*;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        assertEquals(parentGroup, group.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        
        assertFalse(group.notifyMessageSent(client2, "Hello"));
        
        group.addClientHandler(new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertEquals(client1, client);
                assertEquals("Hello", message);
            }
        });
        
        assertTrue(group.notifyMessageSent(client1, "Hello"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        
        assertFalse(group.notifyMessageReceived(client2, "World"));
        
        group.addClientHandler(new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                assertEquals(client1, client);
                assertEquals("World", message);
            }
        });
        
        assertTrue(group.notifyMessageReceived(client1, "World"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        
        assertFalse(group.notifyExceptionCaught(client2, new Throwable("Error")));
        
        group.addClientHandler(new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                assertEquals(client1, client);
                assertEquals("Error", cause.getMessage());
            }
        });
        
        assertTrue(group.notifyExceptionCaught(client1, new Throwable("Error")));
    }
    
    @Test
    public void testIterator() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        group.add(client2);
        
        Iterator<Client> iterator = group.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        
        assertTrue(group.contains(client1));
        assertFalse(group.contains(client2));
    }
    
    @Test
    public void testSize() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        group.add(client2);
        
        assertEquals(2, group.size());
    }
    
    @Test
    public void testAdd() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        
        assertTrue(group.add(client1));
        assertTrue(group.contains(client1));
        assertFalse(group.add(client2));
    }
    
    @Test
    public void testRemove() {
        Client client1 = new Client();
        Client client2 = new Client();
        
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        group.add(client2);
        
        assertTrue(group.remove(client1));
        assertFalse(group.contains(client1));
        assertTrue(group.remove(client2));
        assertFalse(group.contains(client2));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        
        assertNotNull(subGroup);
        assertEquals(group, subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                // Test functionality of added client handler
            }
        };
        
        group.addClientHandler(handler);
        group.add(new Client());
        
        assertEquals(handler, group.handler);
    }
}
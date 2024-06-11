import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {
    
    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        
        assertEquals(parentGroup, childGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        boolean expectedResult = true;
        
        // test
        boolean result = group.notifyMessageSent(client, message);
        
        // assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        boolean expectedResult = true;
        
        // test
        boolean result = group.notifyMessageReceived(client, message);
        
        // assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();
        boolean expectedResult = true;
        
        // test
        boolean result = group.notifyExceptionCaught(client, cause);
        
        // assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIterator() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        group.add(client1);
        group.add(client2);
        group.add(client3);
        
        // test
        Iterator<Client> iterator = group.iterator();
        Set<Client> clients = new HashSet<Client>();
        while (iterator.hasNext()) {
            clients.add(iterator.next());
        }
        
        // assert
        assertEquals(3, clients.size());
        assertTrue(clients.contains(client1));
        assertTrue(clients.contains(client2));
        assertTrue(clients.contains(client3));
    }
    
    @Test
    public void testContains() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        
        // test
        boolean result = group.contains(client);
        
        // assert
        assertTrue(result);
    }
    
    @Test
    public void testSize() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        
        // test
        int size = group.size();
        
        // assert
        assertEquals(2, size);
    }
    
    @Test
    public void testAdd() {
        // setup
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        
        // test
        boolean result = group.add(client);
        
        // assert
        assertTrue(result);
    }
    
    @Test
    public void testRemove() {
        // setup
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        parentGroup.add(client);
        
        // test
        boolean result = childGroup.remove(client);
        
        // assert
        assertTrue(result);
    }
    
    @Test
    public void testCreateSubGroup() {
        // setup
        ClientGroup group = new ClientGroup(null);
        
        // test
        ClientGroup subGroup = group.createSubGroup();
        
        // assert
        assertNotNull(subGroup);
    }
    
    @Test
    public void testAddClientHandler() {
        // setup
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        
        // test
        group.addClientHandler(handler);
        
        // assert
        assertEquals(handler, group.handler);
    }
    
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    private ClientGroup clientGroup;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client = new Client();
        clientGroup = new ClientGroup(subGroup);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
        assertEquals(parentGroup, subGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertEquals(false, parentGroup.notifyMessageSent(client, "message"));
        parentGroup.add(client);
        assertEquals(true, parentGroup.notifyMessageSent(client, "message"));
        subGroup.add(client);
        assertEquals(true, subGroup.notifyMessageSent(client, "message"));
        parentGroup.remove(client);
        assertEquals(false, parentGroup.notifyMessageSent(client, "message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertEquals(false, parentGroup.notifyMessageReceived(client, "message"));
        parentGroup.add(client);
        assertEquals(true, parentGroup.notifyMessageReceived(client, "message"));
        subGroup.add(client);
        assertEquals(true, subGroup.notifyMessageReceived(client, "message"));
        parentGroup.remove(client);
        assertEquals(false, parentGroup.notifyMessageReceived(client, "message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertEquals(false, parentGroup.notifyExceptionCaught(client, new Throwable()));
        parentGroup.add(client);
        assertEquals(true, parentGroup.notifyExceptionCaught(client, new Throwable()));
        subGroup.add(client);
        assertEquals(true, subGroup.notifyExceptionCaught(client, new Throwable()));
        parentGroup.remove(client);
        assertEquals(false, parentGroup.notifyExceptionCaught(client, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        parentGroup.add(client);
        Iterator<Client> iterator = parentGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        parentGroup.add(client);
        assertEquals(true, parentGroup.contains(client));
        assertEquals(false, subGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertEquals(true, parentGroup.add(client));
        assertEquals(false, parentGroup.add(client));
        assertEquals(true, subGroup.add(client));
        assertEquals(true, parentGroup.contains(client));
    }
    
    @Test
    public void testRemove() {
        parentGroup.add(client);
        assertEquals(true, parentGroup.remove(client));
        assertEquals(false, parentGroup.remove(client));
        assertEquals(false, parentGroup.contains(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertEquals(subGroup, parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        parentGroup.add(client);
        parentGroup.addClientHandler(new ClientHandler());
        assertNotNull(parentGroup.handler);
    }
}
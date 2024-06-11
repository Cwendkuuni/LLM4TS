import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    private ClientHandler handler;
    private ClientGroup testGroup;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = new ClientGroup(parentGroup);
        client = new Client();
        handler = new ClientHandlerImpl();
        testGroup = new ClientGroup(parentGroup);
        testGroup.createSubGroup();
        testGroup.add(client);
        testGroup.addClientHandler(handler);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, testGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertTrue(testGroup.notifyMessageSent(client, "Test message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertTrue(testGroup.notifyMessageReceived(client, "Test message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertTrue(testGroup.notifyExceptionCaught(client, new Exception("Test exception")));
    }
    
    @Test
    public void testIterator() {
        assertTrue(testGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        assertTrue(testGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(1, testGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client newClient = new Client();
        assertTrue(testGroup.add(newClient));
    }
    
    @Test
    public void testRemove() {
        assertTrue(testGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(testGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        testGroup.addClientHandler(new ClientHandlerImpl());
        assertNotNull(testGroup.handler);
    }
}
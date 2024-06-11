import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Iterator;
import ioproject.server.network.*;

public class ClientGroupTest {
    
    ClientGroup parentGroup;
    Client client;
    ClientHandler handler;
    ClientGroup cg;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        client = new Client();
        handler = new ClientHandler();
        cg = new ClientGroup(parentGroup);
        cg.addClientHandler(handler);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, cg.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertFalse(cg.notifyMessageSent(client, "test message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertFalse(cg.notifyMessageReceived(client, "test message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(cg.notifyExceptionCaught(client, new Exception()));
    }
    
    @Test
    public void testContains() {
        assertFalse(cg.contains(client));
        cg.add(client);
        assertTrue(cg.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, cg.size());
        cg.add(client);
        assertEquals(1, cg.size());
    }
    
    @Test
    public void testAdd() {
        assertFalse(cg.add(client));
        assertTrue(cg.add(client));
    }
    
    @Test
    public void testRemove() {
        assertFalse(cg.remove(client));
        cg.add(client);
        assertTrue(cg.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = cg.createSubGroup();
        assertNotNull(subGroup);
        assertTrue(cg.contains(subGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        assertEquals(handler, cg.handler);
    }
}
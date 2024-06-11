package ioproject.server.network;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler handler;
    
    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
        handler = new ClientHandler();
    }
    
    @Test
    public void testParentGroup() {
        assertSame(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        assertEquals(false, parentGroup.notifyMessageSent(client1, "message"));
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertEquals(true, parentGroup.notifyMessageSent(client1, "message"));
        assertEquals(true, parentGroup.notifyMessageSent(client2, "message"));
        assertEquals(false, parentGroup.notifyMessageSent(client3, "message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        assertEquals(false, parentGroup.notifyMessageReceived(client1, "message"));
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertEquals(true, parentGroup.notifyMessageReceived(client1, "message"));
        assertEquals(true, parentGroup.notifyMessageReceived(client2, "message"));
        assertEquals(false, parentGroup.notifyMessageReceived(client3, "message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        assertEquals(false, parentGroup.notifyExceptionCaught(client1, new Throwable()));
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertEquals(true, parentGroup.notifyExceptionCaught(client1, new Throwable()));
        assertEquals(true, parentGroup.notifyExceptionCaught(client2, new Throwable()));
        assertEquals(false, parentGroup.notifyExceptionCaught(client3, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertTrue(parentGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client1));
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
        assertFalse(parentGroup.contains(client3));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertEquals(3, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.contains(client1));
        assertFalse(parentGroup.add(client1));
        assertFalse(subGroup.add(client1));
    }
    
    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client1));
        
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.add(client3);
        
        subGroup.add(client2);
        
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.contains(client1));
        assertFalse(parentGroup.remove(client1));
        assertFalse(subGroup.remove(client1));
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        assertFalse(parentGroup.subGroups.contains(newSubGroup));
        assertTrue(parentGroup.subGroups.contains(subGroup));
    }
    
    @Test
    public void testAddClientHandler() {
        assertNull(parentGroup.handler);
        
        parentGroup.addClientHandler(handler);
        
        assertSame(handler, parentGroup.handler);
    }

}
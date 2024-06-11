package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class ClientGroupTest {
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    private ClientHandler handler;
    
    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client = new Client();
        handler = new ClientHandler();
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        parentGroup.add(client);
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageSent(client, "message"));
    }
    
    @Test
    public void testNotifyMessageReceived() {
        parentGroup.add(client);
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageReceived(client, "message"));
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        parentGroup.add(client);
        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyExceptionCaught(client, new Throwable()));
    }
    
    @Test
    public void testIterator() {
        parentGroup.add(client);
        assertEquals(client, parentGroup.iterator().next());
    }
    
    @Test
    public void testContains() {
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }
    
    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client));
    }
    
    @Test
    public void testRemove() {
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
    }
    
    @Test
    public void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
    }
    
    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}
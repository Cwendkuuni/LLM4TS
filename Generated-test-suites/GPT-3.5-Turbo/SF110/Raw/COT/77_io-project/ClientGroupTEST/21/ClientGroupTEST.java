import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.util.Iterator;
import java.util.Collections;

public class ClientGroupTest {
    
    @Test
    public void testAdd() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        boolean result = group.add(client);
        assertTrue(result);
        assertTrue(group.contains(client));
    }
    
    @Test
    public void testRemove() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        group.add(client);
        boolean result = group.remove(client);
        assertTrue(result);
        assertFalse(group.contains(client));
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
        ClientHandler handler = mock(ClientHandler.class);
        group.addClientHandler(handler);
        assertEquals(handler, group.getHandler());
    }
    
    @Test
    public void testNotifyMessageSent() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        Object message = new Object();
        assertFalse(group.notifyMessageSent(client, message));
        group.add(client);
        assertFalse(group.notifyMessageSent(client, message));
        
        ClientGroup subGroup = group.createSubGroup();
        Client clientInSubGroup = mock(Client.class);
        subGroup.add(clientInSubGroup);
        assertTrue(group.notifyMessageSent(clientInSubGroup, message));
        verify(handler).messageSent(clientInSubGroup, message);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        Object message = new Object();
        assertFalse(group.notifyMessageReceived(client, message));
        group.add(client);
        assertFalse(group.notifyMessageReceived(client, message));
        
        ClientGroup subGroup = group.createSubGroup();
        Client clientInSubGroup = mock(Client.class);
        subGroup.add(clientInSubGroup);
        assertTrue(group.notifyMessageReceived(clientInSubGroup, message));
        verify(handler).messageReceived(clientInSubGroup, message);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        Throwable cause = mock(Throwable.class);
        assertFalse(group.notifyExceptionCaught(client, cause));
        group.add(client);
        assertFalse(group.notifyExceptionCaught(client, cause));
        
        ClientGroup subGroup = group.createSubGroup();
        Client clientInSubGroup = mock(Client.class);
        subGroup.add(clientInSubGroup);
        assertTrue(group.notifyExceptionCaught(clientInSubGroup, cause));
        verify(handler).exceptionCaught(clientInSubGroup, cause);
    }
    
    @Test
    public void testContains() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        assertFalse(group.contains(client));
        group.add(client);
        assertTrue(group.contains(client));
    }
    
    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        assertEquals(0, group.size());
        Client client1 = mock(Client.class);
        group.add(client1);
        assertEquals(1, group.size());
        Client client2 = mock(Client.class);
        group.add(client2);
        assertEquals(2, group.size());
        group.remove(client1);
        assertEquals(1, group.size());
    }
    
    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
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
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        assertEquals(parentGroup, group.parentGroup());
    }
    
    @Test
    public void testAdd_ClientNotInParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = mock(Client.class);
        boolean result = group.add(client);
        assertFalse(result);
        assertFalse(group.contains(client));
    }
    
    @Test
    public void testRemove_ClientInSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = mock(Client.class);
        group.add(client);
        ClientGroup subGroup = group.createSubGroup();
        Client clientInSubGroup = mock(Client.class);
        subGroup.add(clientInSubGroup);
        boolean result = group.remove(clientInSubGroup);
        assertTrue(result);
        assertFalse(subGroup.contains(clientInSubGroup));
        assertFalse(group.contains(clientInSubGroup));
    }
    
    @Test
    public void testCreateSubGroup_ParentGroupIsNull() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();
        assertNotNull(subGroup);
        assertNull(subGroup.parentGroup());
    }
    
    @Test
    public void testAddClientHandler_HandlerAlreadySet() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler1 = mock(ClientHandler.class);
        ClientHandler handler2 = mock(ClientHandler.class);
        group.addClientHandler(handler1);
        group.addClientHandler(handler2);
        assertEquals(handler2, group.getHandler());
    }
    
    @Test
    public void testNotifyMessageSent_HandlerIsNull() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        Object message = new Object();
        assertFalse(group.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageReceived_HandlerIsNull() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        Object message = new Object();
        assertFalse(group.notifyMessageReceived(client, message));
    }
    
    @Test
    public void testNotifyExceptionCaught_HandlerIsNull() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);
        Throwable cause = mock(Throwable.class);
        assertFalse(group.notifyExceptionCaught(client, cause));
    }
}
package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;

public class ClientGroupTest {
    
    private Client mockClient;
    private ClientGroup mockParentGroup;
    private ClientHandler mockHandler;
    private ClientGroup clientGroup;
    
    @Before
    public void setup() {
        mockClient = mock(Client.class);
        mockParentGroup = mock(ClientGroup.class);
        mockHandler = mock(ClientHandler.class);
        
        clientGroup = new ClientGroup(mockParentGroup);
        clientGroup.addClientHandler(mockHandler);
        clientGroup.add(mockClient);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(mockParentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageSent(mockClient, message));
        verify(mockHandler).messageSent(mockClient, message);
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(mockClient);
        assertTrue(clientGroup.notifyMessageSent(mockClient, message));
        verify(mockHandler, times(1)).messageSent(mockClient, message);
        verify(mockHandler, times(1)).messageSent(mockClient, message);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageReceived(mockClient, message));
        verify(mockHandler).messageReceived(mockClient, message);
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(mockClient);
        assertTrue(clientGroup.notifyMessageReceived(mockClient, message));
        verify(mockHandler, times(1)).messageReceived(mockClient, message);
        verify(mockHandler, times(1)).messageReceived(mockClient, message);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Throwable cause = new Throwable();
        assertTrue(clientGroup.notifyExceptionCaught(mockClient, cause));
        verify(mockHandler).exceptionCaught(mockClient, cause);
        
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(mockClient);
        assertTrue(clientGroup.notifyExceptionCaught(mockClient, cause));
        verify(mockHandler, times(1)).exceptionCaught(mockClient, cause);
        verify(mockHandler, times(1)).exceptionCaught(mockClient, cause);
    }
    
    @Test
    public void testIterator() {
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(mockClient, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testContains() {
        assertTrue(clientGroup.contains(mockClient));
        assertFalse(clientGroup.contains(mock(Client.class)));
    }
    
    @Test
    public void testSize() {
        assertEquals(1, clientGroup.size());
        clientGroup.add(mock(Client.class));
        assertEquals(2, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client = mock(Client.class);
        when(mockParentGroup.contains(client)).thenReturn(true);
        
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
        verify(mockHandler).clientAdded(client);
        
        when(mockParentGroup.contains(client)).thenReturn(false);
        assertFalse(clientGroup.add(client));
        assertFalse(clientGroup.contains(client));
        verify(mockHandler, never()).clientAdded(client);
    }
    
    @Test
    public void testRemove() {
        when(mockParentGroup.contains(mockClient)).thenReturn(true);
        clientGroup.remove(mockClient);
        assertFalse(clientGroup.contains(mockClient));
        verify(mockHandler).clientRemoved(mockClient);
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertTrue(clientGroup.iterator().hasNext());
        assertFalse(subGroup.iterator().hasNext());
        assertTrue(clientGroup.contains(mockClient));
        assertFalse(subGroup.contains(mockClient));
    }
    
    @Test
    public void testAddClientHandler() {
        clientGroup.addClientHandler(mockHandler);
        verify(mockHandler, times(2)).clientAdded(mockClient);
    }
}
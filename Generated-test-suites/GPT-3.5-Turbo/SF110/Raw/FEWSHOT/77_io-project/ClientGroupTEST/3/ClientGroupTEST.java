package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {
    private ClientGroup clientGroup;
    private ClientGroup parentGroup;
    private ClientHandler handlerMock;
    
    @Before
    public void setup() {
        parentGroup = mock(ClientGroup.class);
        handlerMock = mock(ClientHandler.class);
        clientGroup = new ClientGroup(parentGroup);
        clientGroup.addClientHandler(handlerMock);
    }
    
    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        Client client = mock(Client.class);
        Object message = new Object();
        
        assertFalse(clientGroup.notifyMessageSent(client, message));
        
        when(parentGroup.contains(client)).thenReturn(true);
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        verify(handlerMock, times(1)).messageSent(client, message);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        Client client = mock(Client.class);
        Object message = new Object();
        
        assertFalse(clientGroup.notifyMessageReceived(client, message));
        
        when(parentGroup.contains(client)).thenReturn(true);
        assertTrue(clientGroup.notifyMessageReceived(client, message));
        
        verify(handlerMock, times(1)).messageReceived(client, message);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        Client client = mock(Client.class);
        Throwable cause = new Throwable();
        
        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
        
        when(parentGroup.contains(client)).thenReturn(true);
        assertTrue(clientGroup.notifyExceptionCaught(client, cause));
        
        verify(handlerMock, times(1)).exceptionCaught(client, cause);
    }
    
    @Test
    public void testIterator() {
        assertTrue(clientGroup.iterator().hasNext());
    }
    
    @Test
    public void testContains() {
        Client client = mock(Client.class);
        
        assertFalse(clientGroup.contains(client));
        
        when(parentGroup.contains(client)).thenReturn(true);
        assertTrue(clientGroup.contains(client));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
    }
    
    @Test
    public void testAdd() {
        Client client = mock(Client.class);
        
        assertFalse(clientGroup.add(client));
        
        when(parentGroup.contains(client)).thenReturn(true);
        assertTrue(clientGroup.add(client));
        
        verify(handlerMock, times(1)).clientAdded(client);
    }
    
    @Test
    public void testRemove() {
        Client client = mock(Client.class);
        
        assertFalse(clientGroup.remove(client));
        
        when(clientGroup.contains(client)).thenReturn(true);
        assertTrue(clientGroup.remove(client));
        
        verify(handlerMock, times(1)).clientRemoved(client);
    }
    
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        
        assertNotNull(subGroup);
    }
    
    @Test
    public void testAddClientHandler() {
        assertEquals(handlerMock, clientGroup.handler);
    }
}
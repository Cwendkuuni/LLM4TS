import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class HandlerTest {
    
    Handler handler = new Handler();
    
    @Test
    public void testAddConnection() {
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        handler.addConnection(mockEndpoint);
        
        // Verify that the connection endpoint is added to the clients vector
        Assert.assertEquals(1, handler.clients.size());
        
        // Verify that the connection endpoint is connected and a UserEvent is sent to all existing clients
        verify(mockEndpoint, times(1)).isConnected();
        verify(mockEndpoint, times(1)).send(any(UserEvent.class));
    }
    
    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        handler.addConnection(mockEndpoint);
        
        // Verify that the connection endpoint is removed from the clients vector and a UserEvent is sent to all remaining clients
        handler.removeConnection(mockEndpoint);
        Assert.assertEquals(0, handler.clients.size());
        verify(mockEndpoint, times(1)).send(any(UserEvent.class));
    }
    
    @Test
    public void testHandleInput_StringCommandDice() {
        InputEvent mockEvent = mock(InputEvent.class);
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        when(mockEvent.getData()).thenReturn("/dice 6");
        
        handler.handleInput(mockEvent);
        
        // Verify that the rollDice method is called with the correct parameters and the expected UserEvent is sent to all clients
        verify(mockEndpoint, times(1)).send("HandlerName rolled 1d6 --> 3");
        verify(mockEndpoint, times(1)).send("HandlerName rolled 1d6+4 --> 7");
        verify(mockEndpoint, never()).send(anyString());
    }
    
    @Test
    public void testHandleInput_StringCommandWhisper() {
        InputEvent mockEvent = mock(InputEvent.class);
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        when(mockEvent.getData()).thenReturn("/whisper User1 hello!");
        
        handler.handleInput(mockEvent);
        
        // Verify that the whisper method is called with the correct parameters and the expected messages are sent to the appropriate clients
        verify(mockEndpoint, times(1)).send("HandlerName whispers: hello!");
        verify(mockEndpoint, times(1)).send("You whisper to User1: hello!");
        verify(mockEndpoint, never()).send(anyString());
    }
    
    @Test
    public void testHandleInput_StringCommandMe() {
        InputEvent mockEvent = mock(InputEvent.class);
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        when(mockEvent.getData()).thenReturn("/me is here!");
        
        handler.handleInput(mockEvent);
        
        // Verify that the sendAllObj method is called with the correct parameters and the expected message is sent to all clients
        verify(mockEndpoint, times(1)).send("*HandlerName: is here!");
        verify(mockEndpoint, never()).send(anyString());
    }
    
    // ... Additional test methods for remaining methods in Handler class
    
    @Test
    public void testSendHeros() {
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        handler.addConnection(mockEndpoint);
        
        // Verify that the send method is called with the correct parameters and the expected Hero objects are sent to the client
        verify(mockEndpoint, times(1)).send(any(Vector.class));
    }
    
    @Test
    public void testIsGmConnected() {
        // Add a client with name "Gamemaster" to clients vector
        
        // Verify that the isGmConnected method returns true
        
        // Remove the client with name "Gamemaster" from clients vector
        
        // Verify that the isGmConnected method returns false
    }
    
    @Test
    public void testSendOneSystemMsg() {
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        
        // Verify that the send method is called with the correct parameters
        
    }
    
    @Test
    public void testSendAllSystemMsg() {
        // Add multiple clients to clients vector
        
        // Verify that the send method is called with the correct parameters for each client
        
    }
    
    @Test
    public void testSendAllObj() {
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        handler.addConnection(mockEndpoint);
        
        // Verify that the send method is called with the correct parameters and the expected object is sent to all clients
        
    }
    
    @Test
    public void testGetName() {
        ConnectionEndpoint mockEndpoint = mock(ConnectionEndpoint.class);
        
        // Add a client with a specific name to clients vector
        
        // Verify that the getName method returns the correct name for the client
        
        // Remove the client from clients vector
        
        // Verify that the getName method returns null
        
    }
}
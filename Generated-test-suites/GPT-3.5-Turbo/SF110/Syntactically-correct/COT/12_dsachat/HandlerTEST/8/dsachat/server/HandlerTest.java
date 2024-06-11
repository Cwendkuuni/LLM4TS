import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.ConnectionEndpoint;
import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint mockConnection;
    
    @Before
    public void setUp() {
        handler = new Handler();
        mockConnection = mock(ConnectionEndpoint.class);
    }
    
    // Test addConnection() method
    @Test
    public void testAddConnection() {
        handler.addConnection(mockConnection);
        
        assertEquals(1, handler.getClients().size());
        assertEquals(mockConnection, handler.getClients().get(0).ce);
        assertEquals("", handler.getClients().get(0).name);
    }
    
    // Test removeConnection() method
    @Test
    public void testRemoveConnection() {
        handler.addConnection(mockConnection);
        
        handler.removeConnection(mockConnection);
        
        assertEquals(0, handler.getClients().size());
    }
    
    // Test handleInput() method
    @Test
    public void testHandleInputForString() {
        String input = "Hello";
        
        InputEvent mockEvent = mock(InputEvent.class);
        when(mockEvent.getData()).thenReturn(input);
        when(mockEvent.getSource()).thenReturn(mockConnection);
        
        handler.handleInput(mockEvent);
        
        verify(mockConnection).send("Hello");
    }

    @Test
    public void testHandleInputForChallenge() throws IOException {
        Challenge challenge = new Challenge();
        
        InputEvent mockEvent = mock(InputEvent.class);
        when(mockEvent.getData()).thenReturn(challenge);
        when(mockEvent.getSource()).thenReturn(mockConnection);
        
        handler.handleInput(mockEvent);
        
        verify(mockConnection).send(challenge.roll());
    }
    
    @Test
    public void testHandleInputForChallReq() throws IOException {
        ChallReq challReq = new ChallReq();
        handler.addConnection(mockConnection);
        
        InputEvent mockEvent = mock(InputEvent.class);
        when(mockEvent.getData()).thenReturn(challReq);
        when(mockEvent.getSource()).thenReturn(mockConnection);
        when(mockConnection.getClientPort()).thenReturn(1234);
        
        handler.handleInput(mockEvent);
        
        verify(mockConnection).send(challReq);
    }
    
    @Test
    public void testHandleInputForHero() throws IOException {
        Hero hero = new Hero();
        handler.addConnection(mockConnection);
        
        InputEvent mockEvent = mock(InputEvent.class);
        when(mockEvent.getData()).thenReturn(hero);
        when(mockEvent.getSource()).thenReturn(mockConnection);
        when(mockConnection.getClientPort()).thenReturn(1234);
        
        handler.handleInput(mockEvent);
        
        verify(mockConnection).send("Welcome " + hero.getName());
    }
    
    @Test
    public void testHandleInputForUserEventWithoutGmConnected() throws IOException {
        UserEvent userEvent = new UserEvent();
        
        InputEvent mockEvent = mock(InputEvent.class);
        when(mockEvent.getData()).thenReturn(userEvent);
        when(mockEvent.getSource()).thenReturn(mockConnection);
        
        handler.handleInput(mockEvent);
        
        verify(mockConnection).send("Welcome " + userEvent.getName());
    }
    
    // Other test cases for additional methods can be added here
    
    // Helper method to extract clients
    private Vector<Entry> getClients() {
        return handler.clients;
    }

}
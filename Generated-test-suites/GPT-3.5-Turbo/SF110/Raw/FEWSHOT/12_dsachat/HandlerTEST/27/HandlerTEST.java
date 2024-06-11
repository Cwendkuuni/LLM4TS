package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;

import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;
import dsachat.share.InputEvent;
import dsachat.share.UserEvent;

public class HandlerTest {
    
    private static Handler handler;
    private static InputEvent inputEventMock;
    private static ConnectionEndpoint connectionEndpointMock;
    
    @BeforeClass
    public static void setup() {
        handler = new Handler();
        inputEventMock = mock(InputEvent.class);
        connectionEndpointMock = mock(ConnectionEndpoint.class);
    }
    
    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpointMock);
        assertEquals(1, handler.clients.size());
    }
    
    @Test
    public void testRemoveConnection() {
        handler.removeConnection(connectionEndpointMock);
        assertEquals(0, handler.clients.size());
    }
    
    @Test
    public void testHandleInput_String_starts_with_slash_d() {
        String message = "/dice 6";
        when(inputEventMock.getData()).thenReturn(message);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_String_starts_with_slash_w() {
        String message = "/whisper hello world";
        when(inputEventMock.getData()).thenReturn(message);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_String_starts_with_me() {
        String message = "me";
        when(inputEventMock.getData()).thenReturn(message);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_String_starts_with_slash_getheros() {
        String message = "/getheros";
        when(inputEventMock.getData()).thenReturn(message);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_Object_Challenge_class() {
        Challenge challenge = new Challenge();
        when(inputEventMock.getData()).thenReturn(challenge);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_Object_ChallReq_class() {
        ChallReq challReq = new ChallReq();
        when(inputEventMock.getData()).thenReturn(challReq);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_Object_Hero_class() {
        Hero hero = new Hero();
        when(inputEventMock.getData()).thenReturn(hero);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_Object_UserEvent_class_no_GmConnected() {
        UserEvent userEvent = new UserEvent();
        when(inputEventMock.getData()).thenReturn(userEvent);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testHandleInput_Object_UserEvent_class_GmConnected() {
        UserEvent userEvent = new UserEvent();
        handler.clients.add(new Entry(connectionEndpointMock, "", null));
        when(inputEventMock.getData()).thenReturn(userEvent);
        when(inputEventMock.getSource()).thenReturn(connectionEndpointMock);
        
        handler.handleInput(inputEventMock);
        
        // verify your expected results
    }
    
    @Test
    public void testSendHeros() {
        handler.clients.add(new Entry(connectionEndpointMock, "", new Hero()));
        
        handler.sendHeros(connectionEndpointMock);
        
        // verify your expected results
    }
    
    // Add more test cases for the remaining methods
    
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;

import java.io.IOException;

public class HandlerTest {
    private Handler handler;
    private ConnectionEndpoint connection;

    @Before
    public void setUp() {
        handler = new Handler();
        connection = mock(ConnectionEndpoint.class);
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connection);

        assertEquals(1, handler.clients.size());
        assertEquals(connection, handler.clients.get(0).ce);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connection);

        handler.removeConnection(connection);

        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputWithString() {
        InputEvent inputEvent = new InputEvent("Test message", connection);
        handler.handleInput(inputEvent);
    }

    @Test
    public void testHandleInputWithChallenge() throws IOException {
        Challenge challenge = mock(Challenge.class);

        InputEvent inputEvent = new InputEvent(challenge, connection);
        handler.handleInput(inputEvent);

        verify(connection, times(1)).send(anyString());
    }

    @Test
    public void testHandleInputWithChallReq() throws IOException {
        ChallReq challReq = mock(ChallReq.class);

        InputEvent inputEvent = new InputEvent(challReq, connection);
        handler.handleInput(inputEvent);

        // Verify handling of ChallReq
    }

    @Test
    public void testHandleInputWithHero() throws IOException {
        Hero hero = mock(Hero.class);

        InputEvent inputEvent = new InputEvent(hero, connection);
        handler.handleInput(inputEvent);

        // Verify handling of Hero
    }

    @Test
    public void testHandleInputWithUserEvent() throws IOException {
        UserEvent userEvent = mock(UserEvent.class);

        InputEvent inputEvent = new InputEvent(userEvent, connection);
        handler.handleInput(inputEvent);

        // Verify handling of UserEvent
    }

    @Test
    public void testSendHeros() throws IOException {
        ConnectionEndpoint con = mock(ConnectionEndpoint.class);

        handler.addConnection(con);

        handler.sendHeros(connection);

        verify(con, times(1)).send(any());
    }

    @Test
    public void testIsGmConnected() {
        ConnectionEndpoint gmCon = mock(ConnectionEndpoint.class);
        ConnectionEndpoint otherCon = mock(ConnectionEndpoint.class);
        
        handler.addConnection(gmCon);
        handler.addConnection(otherCon);

        assertTrue(handler.isGmConnected());
    }

    // Add more test methods to test other methods in Handler class

}
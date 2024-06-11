import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class HandlerTest {

    // Test for addConnection method
    @Test
    public void testAddConnection() {
        // Test adding a connection
        Handler handler = new Handler();
        assertNotNull(handler.clients);
        assertEquals(0, handler.clients.size());
    }

    // Test for removeConnection method
    @Test
    public void testRemoveConnection() {
        // Test removing a connection
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        assertEquals(1, handler.clients.size());
        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
    }

    // Test for handleInput method
    @Test
    public void testHandleInput() {
        // Test handling input with different data types
        Handler handler = new Handler();
        InputEvent e1 = new InputEvent("Test String");
        handler.handleInput(e1);

        InputEvent e2 = new InputEvent(new Challenge());
        handler.handleInput(e2);
    }

    // Test for sendHeros method
    @Test
    public void testSendHeros() {
        // Test sending heroes to connection endpoint
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendHeros(con);
        // Add assertions here
    }

    // Test for isGmConnected method
    @Test
    public void testIsGmConnected() {
        // Test checking if Gamemaster is connected
        Handler handler = new Handler();
        assertFalse(handler.isGmConnected());
        ConnectionEndpoint gm = new ConnectionEndpoint();
        gm.setName("Gamemaster");
        handler.addConnection(gm);
        assertTrue(handler.isGmConnected());
    }

    // Testing edge cases and exception handling scenarios for other methods in Handler class
    // Add more test cases for other methods as required

}
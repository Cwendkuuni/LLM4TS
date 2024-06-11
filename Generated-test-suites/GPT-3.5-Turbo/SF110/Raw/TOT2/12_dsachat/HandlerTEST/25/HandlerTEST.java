import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connection1;
    private ConnectionEndpoint connection2;

    @Before
    public void setUp() {
        handler = new Handler();
        connection1 = Mockito.mock(ConnectionEndpoint.class);
        connection2 = Mockito.mock(ConnectionEndpoint.class);
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connection1);
        assertNotNull(handler.clients);
        assertEquals(1, handler.clients.size());
        assertTrue(handler.clients.get(0).ce == connection1);
    }

    @Test
    public void testAddConnectionWithException() {
        Mockito.when(connection1.isConnected()).thenThrow(new IOException());
        handler.addConnection(connection1);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        handler.removeConnection(connection1);
        assertEquals(1, handler.clients.size());
        assertTrue(handler.clients.get(0).ce == connection2);
    }

    @Test
    public void testHandleInput() {
        // Test cases for handleInput method can be added here
    }

    // Add more test methods for other public methods in Handler class

}
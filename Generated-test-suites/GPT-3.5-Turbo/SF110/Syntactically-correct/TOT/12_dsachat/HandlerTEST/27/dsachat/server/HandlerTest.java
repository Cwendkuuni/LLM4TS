import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class HandlerTest {
    
    private Handler handler;
    
    @Before
    public void setUp() {
        handler = new Handler();
    }

    @Test
    public void addConnection_basicInput_successful() {
        ConnectionEndpoint connection = new ConnectionEndpoint();
        
        handler.addConnection(connection);
        
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void addConnection_multipleConnections_successful() {
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        
        assertEquals(2, handler.clients.size());
    }

    @Test
    public void addConnection_alreadyConnected_skipped() {
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        handler.addConnection(connection1);
        
        assertEquals(2, handler.clients.size());
    }

    @Test
    public void removeConnection_basicInput_successful() {
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        
        handler.removeConnection(connection);
        
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void removeConnection_multipleConnections_successful() {
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        
        handler.removeConnection(connection1);
        
        assertEquals(1, handler.clients.size());
        assertEquals(connection2.getClientPort(), handler.clients.get(0).ce.getClientPort());
    }

    @Test
    public void removeConnection_notFound_skipped() {
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        
        handler.removeConnection(connection2);
        
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void handleInput_stringInput_startsWithSlash_rollDiceMethodCalled() {
        InputEvent inputEvent = new InputEvent(new Handler(), "/dice 2d6+4");
        
        handler.handleInput(inputEvent);
        
        // Assert that rollDice() method was called with the correct parameters
        // (other assertions can be added depending on what the method is expected to do)
    }
    
    // More test cases for the remaining methods of the Handler class
    
}
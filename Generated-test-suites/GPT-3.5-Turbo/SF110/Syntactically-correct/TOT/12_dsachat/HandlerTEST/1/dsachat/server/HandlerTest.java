import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HandlerTest {

    private Handler handler;

    @Before
    public void setUp() {
        handler = new Handler();
    }

    // Test cases for addConnection() method

    @Test
    public void testAddConnection_NormalCase() {
        // Given
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();

        // When
        handler.addConnection(connectionEndpoint);

        // Then
        assertEquals(1, handler.clients.size());
        assertEquals(connectionEndpoint, handler.clients.get(0).ce);
    }

    @Test
    public void testAddConnection_NoExistingClients() {
        // Given
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();

        // When
        handler.addConnection(connectionEndpoint);

        // Then
        assertEquals("", handler.clients.get(0).name);
    }

    @Test
    public void testAddConnection_SendUserEvent() throws InterruptedException {
        // Given
        ConnectionEndpoint connectionEndpoint1 = new ConnectionEndpoint();
        ConnectionEndpoint connectionEndpoint2 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        handler.clients.get(0).name = "User1";

        // When
        new Thread(() -> handler.addConnection(connectionEndpoint2)).start();
        Thread.sleep(100); // Wait for connectionEndpoint2 to connect

        // Then
        assertEquals(2, handler.clients.size());
        assertEquals("User1", connectionEndpoint2.getLastSent()); // UserEvent should be sent to connectionEndpoint2 with name "User1"
    }

    // Test cases for removeConnection() method


    // Test cases for handleInput() method


    // Test cases for sendHeros() method


    // Test cases for isGmConnected() method


    // Test cases for sendOneSystemMsg() method


    // Test cases for sendAllSystemMsg() method


    // Test cases for whisper() method


    // Test cases for rollDice() method


    // Test cases for sendToOneStr() method


    // Test cases for updateHero() method


    // Test cases for sendAllStr() method


    // Test cases for sendAllObj() method


    // Test cases for getName() method


}
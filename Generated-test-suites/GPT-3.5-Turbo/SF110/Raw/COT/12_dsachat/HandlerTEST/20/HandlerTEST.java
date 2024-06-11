import org.junit.*;
import static org.junit.Assert.*;

public class HandlerTest {
    // Test addConnection method
    @Test
    public void testAddConnection() {
        // Test adding a connection
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        // Assert that the connection is added to the clients list
        assertEquals(1, handler.clients.size());
    }
    
    // Test removeConnection method
    @Test
    public void testRemoveConnection() {
        // Test removing a connection
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        handler.removeConnection(connection);
        // Assert that the connection is removed from the clients list
        assertEquals(0, handler.clients.size());
    }
    
    // Test handleInput method
    @Test
    public void testHandleInput() {
        // Test handling input with string object
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        String input = "Test message";
        InputEvent event = new InputEvent(input, connection);
        handler.handleInput(event);
        // Assert that the message is sent to all clients
        assertEquals(input, connection.getLastSentMessage());
    }
    
    // Test sendHeros method
    @Test
    public void testSendHeros() {
        // Test sending heros to a connection
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        Hero hero = new Hero();
        Entry entry = new Entry();
        entry.ce = connection;
        entry.hero = hero;
        handler.clients.add(entry);
        handler.sendHeros(connection);
        // Assert that the hero is sent to the connection
        assertEquals(hero, connection.getLastSentObject());
    }
    
    // Test isGmConnected method
    @Test
    public void testIsGmConnected() {
        // Test if gm is connected
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        handler.clients.elementAt(0).name = "Gamemaster";
        boolean isConnected = handler.isGmConnected();
        // Assert that the gm is connected
        assertTrue(isConnected);
    }
    
    // Test sendOneSystemMsg method
    @Test
    public void testSendOneSystemMsg() {
        // Test sending a system message to a connection
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        String message = "System message";
        handler.sendOneSystemMsg(connection, message);
        // Assert that the message is sent to the connection
        assertEquals(message, connection.getLastSentMessage());
    }
    
    // Test sendAllSystemMsg method
    @Test
    public void testSendAllSystemMsg() {
        // Test sending a system message to all connections
        Handler handler = new Handler();
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        String message = "System message";
        handler.sendAllSystemMsg(message);
        // Assert that the message is sent to all connections
        assertEquals(message, connection1.getLastSentMessage());
        assertEquals(message, connection2.getLastSentMessage());
    }
    
    // Test whisper method
    @Test
    public void testWhisper() {
        // Test whispering to a client
        Handler handler = new Handler();
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        String command = "user1 Hello";
        handler.whisper(connection1, command);
        // Assert that the whisper message is sent to the target client
        assertEquals("user1 whispers: Hello", connection2.getLastSentMessage());
        // Assert that the user receives a confirmation message
        assertEquals("You whisper to user1: Hello", connection1.getLastSentMessage());
    }
    
    // Test rollDice method
    @Test
    public void testRollDice() {
        // Test rolling dice with modifier
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        String command = "2d6+5";
        handler.rollDice(connection, command);
        // Assert that the roll result is sent to all clients
        assertNotNull(connection.getLastSentMessage());
    }
    
    // Test sendToOneStr method
    @Test
    public void testSendToOneStr() {
        // Test sending a message to a specific client
        Handler handler = new Handler();
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        String target = "user1";
        String message = "Hello";
        handler.sendToOneStr(connection1, target, message);
        // Assert that the message is sent to the target client
        assertEquals("user1: Hello", connection2.getLastSentMessage());
    }
    
    // Test updateHero method
    @Test
    public void testUpdateHero() {
        // Test updating a hero for a client
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        Hero hero = new Hero();
        handler.updateHero(connection, hero);
        // Assert that the hero is updated for the client
        assertNotNull(handler.clients.elementAt(0).hero);
    }
    
    // Test sendAllStr method
    @Test
    public void testSendAllStr() {
        // Test sending a message to all clients
        Handler handler = new Handler();
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        String message = "Hello";
        handler.sendAllStr(connection1, message);
        // Assert that the message is sent to all clients
        assertEquals("user1: Hello", connection1.getLastSentMessage());
        assertEquals("user2: Hello", connection2.getLastSentMessage());
    }
    
    // Test sendAllObj method
    @Test
    public void testSendAllObj() {
        // Test sending an object to all clients
        Handler handler = new Handler();
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        Object object = new Object();
        handler.sendAllObj(object);
        // Assert that the object is sent to all clients
        assertEquals(object, connection1.getLastSentObject());
        assertEquals(object, connection2.getLastSentObject());
    }
    
    // Test getName method
    @Test
    public void testGetName() {
        // Test getting the name of a client
        Handler handler = new Handler();
        ConnectionEndpoint connection1 = new ConnectionEndpoint();
        ConnectionEndpoint connection2 = new ConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        String name = "user1";
        handler.clients.elementAt(0).name = name;
        String returnedName = handler.getName(connection1);
        // Assert that the correct name is returned
        assertEquals(name, returnedName);
    }
}
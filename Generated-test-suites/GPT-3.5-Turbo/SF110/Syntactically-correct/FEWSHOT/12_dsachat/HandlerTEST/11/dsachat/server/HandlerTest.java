package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {
    
    private Handler handler;
    private ConnectionEndpoint connection;
    
    @Before
    public void setUp() {
        handler = new Handler();
        connection = new ConnectionEndpoint();
    }
    
    @Test
    public void testAddConnection() {
        handler.addConnection(connection);
        
        Vector<Handler.Entry> clients = handler.getClients();
        assertEquals(1, clients.size());
        
        Handler.Entry entry = clients.get(0);
        assertEquals(connection, entry.ce);
        assertEquals("", entry.name);
    }
    
    @Test
    public void testRemoveConnection() {
        handler.addConnection(connection);
        
        handler.removeConnection(connection);
        
        Vector<Handler.Entry> clients = handler.getClients();
        assertEquals(0, clients.size());
    }
    
    @Test
    public void testHandleInput() {
        // test with String input
        handler.handleInput(new InputEvent(connection, "Hello World!"));
        
        // add a client with a name
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        handler.getClients().add(entry);
        
        // test with UserEvent input
        handler.handleInput(new InputEvent(connection, new UserEvent(true, "Client 1")));
    }
    
    @Test
    public void testSendHeros() {
        // add a client with a Hero object
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        entry.hero = new Hero();
        handler.getClients().add(entry);
        
        handler.sendHeros(connection);
    }
    
    @Test
    public void testIsGmConnected() {
        // add a client with name "Gamemaster"
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Gamemaster";
        handler.getClients().add(entry);
        
        boolean gmConnected = handler.isGmConnected();
        assertEquals(true, gmConnected);
    }
    
    @Test
    public void testWhisper() {
        // add a client with name "Client 1"
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        handler.getClients().add(entry);
        
        handler.whisper(connection, "Client 1 Hello");
    }
    
    @Test
    public void testRollDice() {
        // test case with sum
        handler.rollDice(connection, "2w6+5");
        
        // test case without sum
        handler.rollDice(connection, "2w6");
    }
    
    @Test
    public void testSendToOneStr() {
        // add a client with name "Client 1"
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        handler.getClients().add(entry);
        
        handler.sendToOneStr(connection, "Client 1", "Hello");
    }
    
    @Test
    public void testUpdateHero() {
        // add a client with a Hero object
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        entry.hero = new Hero();
        handler.getClients().add(entry);
        
        String name = handler.updateHero(connection, entry.hero);
        assertNull(name);
    }
    
    @Test
    public void testSendAllStr() {
        // add a client with name "Client 1"
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        handler.getClients().add(entry);
        
        handler.sendAllStr(connection, "Hello");
    }
    
    @Test
    public void testSendAllObj() {
        handler.sendAllObj(new Object());
    }
    
    @Test
    public void testGetName() {
        // add a client with name "Client 1"
        Handler.Entry entry = new Handler.Entry();
        entry.ce = connection;
        entry.name = "Client 1";
        handler.getClients().add(entry);
        
        String name = handler.getName(connection);
        assertEquals("Client 1", name);
    }
}
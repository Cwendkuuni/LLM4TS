import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Vector;

public class HandlerTest {

    private Handler handler;

    @Before
    public void setUp() {
        handler = new Handler();
    }

    @Test
    public void testAddConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        Vector<Entry> clients = handler.getClients();
        assertEquals(1, clients.size());
        assertEquals(cl, clients.get(0).ce);
        assertEquals("", clients.get(0).name);
    }

    @Test
    public void testAddConnectionWhenAlreadyConnected() {
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        Vector<Entry> clients = handler.getClients();
        assertEquals(2, clients.size());
        assertEquals(cl1, clients.get(0).ce);
        assertEquals(cl2, clients.get(1).ce);
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.removeConnection(cl);
        assertTrue(handler.getClients().isEmpty());
    }

    @Test
    public void testRemoveConnectionWithMultipleConnections() {
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.removeConnection(cl2);
        Vector<Entry> clients = handler.getClients();
        assertEquals(1, clients.size());
        assertEquals(cl1, clients.get(0).ce);
    }

    @Test
    public void testRemoveConnectionWithNonExistingConnection() {
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        handler.addConnection(cl1);
        handler.removeConnection(cl2);
        Vector<Entry> clients = handler.getClients();
        assertEquals(1, clients.size());
        assertEquals(cl1, clients.get(0).ce);
    }

    @Test
    public void testHandleInputWithStringInput() {
        InputEvent e = new InputEvent();
        e.setData("Hello");
        ConnectionEndpoint con = new ConnectionEndpoint();
        e.setSource(con);
        handler.handleInput(e); // No assertions, only test for execution without errors
        // ... add more test cases for different commands and string inputs
    }

    @Test
    public void testHandleInputWithChallengeInput() {
        // ... add test cases for handling Challenge objects in e.getData()
    }
    
    @Test
    public void testHandleInputWithChallReqInput() {
        // ... add test cases for handling ChallReq objects in e.getData()
    }

    @Test
    public void testHandleInputWithHeroInput() {
        // ... add test cases for handling Hero objects in e.getData()
    }

    @Test
    public void testHandleInputWithUserEventInputWhenNoGamemasterConnected() {
        // ... add test cases for handling UserEvent objects in e.getData() when no Gamemaster is connected
    }
    
    @Test
    public void testHandleInputWithUserEventInputWhenGamemasterConnected() {
        // ... add test cases for handling UserEvent objects in e.getData() when Gamemaster is already connected
    }

    @Test
    public void testSendHeros() {
        // ... add test cases for testing sendHeros() method
    }

    @Test
    public void testIsGmConnected() {
        // ... add test cases for testing isGmConnected() method
    }

    @Test
    public void testSendOneSystemMsg() {
        // ... add test cases for testing sendOneSystemMsg() method
    }

    @Test
    public void testSendAllSystemMsg() {
        // ... add test cases for testing sendAllSystemMsg() method
    }

    @Test
    public void testWhisper() {
        // ... add test cases for testing whisper() method
    }

    @Test
    public void testRollDice() {
        // ... add test cases for testing rollDice() method
    }

    @Test
    public void testSendToOneStr() {
        // ... add test cases for testing sendToOneStr() method
    }

    @Test
    public void testUpdateHero() {
        // ... add test cases for testing updateHero() method
    }

    @Test
    public void testSendAllStr() {
        // ... add test cases for testing sendAllStr() method
    }

    @Test
    public void testSendAllObj() {
        // ... add test cases for testing sendAllObj() method
    }

    @Test
    public void testGetName() {
        // ... add test cases for testing getName() method
    }

}
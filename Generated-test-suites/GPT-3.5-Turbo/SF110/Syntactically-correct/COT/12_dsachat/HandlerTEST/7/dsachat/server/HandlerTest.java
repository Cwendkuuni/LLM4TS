
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class HandlerTest {

    //addConnection() test case
    @Test
    public void testAddConnection() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        assertEquals(1, handler.clients.size());
    }

    //removeConnection() test case
    @Test
    public void testRemoveConnection() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
    }

    //handleInput() test case
    @Test
    public void testHandleInput() {
        Handler handler = new Handler();
        InputEvent e = new InputEvent();
        handler.handleInput(e);
        // add assertions here
    }

    //sendHeros() test case
    @Test
    public void testSendHeros() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        cl.connect();
        handler.addConnection(cl);
        handler.clients.elementAt(0).hero = new Hero();
        handler.sendHeros(cl);
        // add assertions here
    }

    //isGmConnected() test case
    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        assertFalse(handler.isGmConnected());
    }

    //sendOneSystemMsg() test case
    @Test
    public void testSendOneSystemMsg() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        cl.connect();
        handler.addConnection(cl);
        handler.sendOneSystemMsg(cl, "Test message");
        // add assertions here
    }

    //sendAllSystemMsg() test case
    @Test
    public void testSendAllSystemMsg() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        cl1.connect();
        cl2.connect();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.sendAllSystemMsg("Test message");
        // add assertions here
    }

    //whisper() test case
    @Test
    public void testWhisper() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        cl1.connect();
        cl2.connect();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.clients.elementAt(0).name = "User1";
        handler.clients.elementAt(1).name = "User2";
        handler.whisper(cl1, "User2 Hello");
        // add assertions here
    }

    //rollDice() test case
    @Test
    public void testRollDice() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        cl.connect();
        handler.addConnection(cl);
        handler.rollDice(cl, "1w6");
        // add assertions here
    }

    //sendToOneStr() test case
    @Test
    public void testSendToOneStr() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        cl1.connect();
        cl2.connect();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.clients.elementAt(0).name = "User1";
        handler.clients.elementAt(1).name = "User2";
        handler.sendToOneStr(cl1, "User2", "Hello");
        // add assertions here
    }

    //updateHero() test case
    @Test
    public void testUpdateHero() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        cl.connect();
        handler.addConnection(cl);
        Hero h = new Hero();
        String name = handler.updateHero(cl, h);
        // add assertions here
    }

    //sendAllStr() test case
    @Test
    public void testSendAllStr() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        cl1.connect();
        cl2.connect();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.clients.elementAt(0).name = "User1";
        handler.clients.elementAt(1).name = "User2";
        handler.sendAllStr(cl1, "Hello");
        // add assertions here
    }

    //sendAllObj() test case
    @Test
    public void testSendAllObj() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        cl1.connect();
        cl2.connect();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.sendAllObj("Hello");
        // add assertions here
    }

    //getName() test case
    @Test
    public void testGetName() throws IOException {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        cl.connect();
        handler.addConnection(cl);
        String name = handler.getName(cl);
        //add assertions here
    }
}


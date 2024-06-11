
package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        assertEquals(1, handler.clients.size());

        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputWithString() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), "test message");
        handler.handleInput(e);

        // Add assertions here
    }

    @Test
    public void testHandleInputWithChallenge() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new Challenge());
        handler.handleInput(e);

        // Add assertions here
    }

    @Test
    public void testHandleInputWithChallReq() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new ChallReq());
        handler.handleInput(e);

        // Add assertions here
    }

    @Test
    public void testHandleInputWithHero() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new Hero());
        handler.handleInput(e);

        // Add assertions here
    }

    @Test
    public void testHandleInputWithUserEvent() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new UserEvent());
        handler.handleInput(e);

        // Add assertions here
    }

    @Test
    public void testSendHeros() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        // Add hero objects to clients vector
        // ...

        handler.sendHeros(con);

        // Add assertions here
    }

    @Test
    public void testIsGmConnected() {
        // Add Gamemaster to clients vector
        // ...

        boolean result = handler.isGmConnected();
        assertEquals(true, result);
    }

    @Test
    public void testSendOneSystemMsg() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String string = "test message";

        handler.sendOneSystemMsg(con, string);

        // Add assertions here
    }

    @Test
    public void testSendAllSystemMsg() {
        String string = "test message";

        handler.sendAllSystemMsg(string);

        // Add assertions here
    }

    @Test
    public void testWhisper() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "test command";

        handler.whisper(con, cmd);

        // Add assertions here
    }

    @Test
    public void testRollDiceWithModifier() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "1w6+4";

        handler.rollDice(con, cmd);

        // Add assertions here
    }

    @Test
    public void testRollDiceWithoutModifier() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "2w8";

        handler.rollDice(con, cmd);

        // Add assertions here
    }

    @Test
    public void testSendToOneStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String to = "test user";
        String send = "test message";

        handler.sendToOneStr(con, to, send);

        // Add assertions here
    }

    @Test
    public void testUpdateHero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero h = new Hero();

        String result = handler.updateHero(con, h);
        assertNotNull(result);

        // Add assertions here
    }

    @Test
    public void testSendAllStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String string = "test message";

        handler.sendAllStr(con, string);

        // Add assertions here
    }

    @Test
    public void testSendAllObj() {
        Object o = new Object();

        handler.sendAllObj(o);

        // Add assertions here
    }

    @Test
    public void testGetName() {
        ConnectionEndpoint con = new ConnectionEndpoint();

        String result = handler.getName(con);
        assertNull(result);

        // Add assertions here
    }
}

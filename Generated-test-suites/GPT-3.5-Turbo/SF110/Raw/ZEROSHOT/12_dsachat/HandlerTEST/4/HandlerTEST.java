package dsachat.server;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        assertEquals(handler.getClients().size(), 1);
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.removeConnection(cl);
        assertEquals(handler.getClients().size(), 0);
    }

    @Test
    public void testHandleInputStringCommandRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        InputEvent e = new InputEvent(this, "/dice 6");
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testHandleInputStringCommandWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        InputEvent e = new InputEvent(this, "/whisper user message");
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testHandleInputStringCommandMe() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        InputEvent e = new InputEvent(this, "Hello");
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testHandleInputStringCommandGetHeros() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        InputEvent e = new InputEvent(this, "/getheros");
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testHandleInputChallenge() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        Challenge challenge = new Challenge();
        InputEvent e = new InputEvent(this, challenge);
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testHandleInputChallReq() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        ChallReq challReq = new ChallReq();
        InputEvent e = new InputEvent(this, challReq);
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testHandleInputHero() {
       Handler handler = new Handler();
       ConnectionEndpoint con = new ConnectionEndpoint();
       handler.addConnection(con);
       Hero hero = new Hero();
       InputEvent e = new InputEvent(this, hero);
       handler.handleInput(e);
       // assert some conditions
    }

    @Test
    public void testHandleInputUserEvent() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        UserEvent userEvent = new UserEvent(false, "user");
        InputEvent e = new InputEvent(this, userEvent);
        handler.handleInput(e);
        // assert some conditions
    }

    @Test
    public void testSendHeros() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendHeros(con);
        // assert some conditions
    }

    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        boolean result = handler.isGmConnected();
        assertFalse(result);
    }

    @Test
    public void testSendOneSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendOneSystemMsg(con, "Message");
        // assert some conditions
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendAllSystemMsg("Message");
        // assert some conditions
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.whisper(con, "user message");
        // assert some conditions
    }

    @Test
    public void testRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.rollDice(con, "2d6+3");
        // assert some conditions
    }

    @Test
    public void testSendToOneStr() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendToOneStr(con, "user", "Message");
        // assert some conditions
    }

    @Test
    public void testUpdateHero() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        Hero hero = new Hero();
        String result = handler.updateHero(con, hero);
        assertNull(result);
    }

    @Test
    public void testSendAllStr() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendAllStr(con, "Message");
        // assert some conditions
    }

    @Test
    public void testSendAllObj() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendAllObj("Message");
        // assert some conditions
    }

    @Test
    public void testGetName() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String result = handler.getName(con);
        assertNull(result);
    }

}
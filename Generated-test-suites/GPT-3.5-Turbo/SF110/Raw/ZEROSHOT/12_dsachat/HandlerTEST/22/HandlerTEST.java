import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

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
        Entry entry = handler.clients.elementAt(0);
        assertEquals("", entry.name);
        assertEquals(cl, entry.ce);
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Entry entry = new Entry();
        entry.ce = cl;
        entry.name = "Test";
        handler.clients.addElement(entry);
        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
        UserEvent newCon = new UserEvent(true, "Test");
        handler.sendAllObj(newCon);
    }

    @Test
    public void testHandleInput_StringDiceCommand() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        InputEvent e = new InputEvent("/dice 6", con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInput_StringWhisperCommand() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        InputEvent e = new InputEvent("/whisper user Hello", con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInput_StringMeCommand() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        InputEvent e = new InputEvent("message", con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInput_StringGetherosCommand() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        InputEvent e = new InputEvent("/getheros", con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInput_Challenge() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Challenge challenge = new Challenge();
        InputEvent e = new InputEvent(challenge, con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInput_ChallReq() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        ChallReq challReq = new ChallReq();
        InputEvent e = new InputEvent(challReq, con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInput_Hero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero hero = new Hero();
        InputEvent e = new InputEvent(hero, con);
        handler.handleInput(e);
        assertEquals(null, handler.getName(con));
    }

    @Test
    public void testHandleInput_UserEvent() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        UserEvent userEvent = new UserEvent(false, "Test");
        InputEvent e = new InputEvent(userEvent, con);
        handler.handleInput(e);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testSendHeros() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.clients.addElement(handler.new Entry());
        handler.sendHeros(con);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testIsGmConnected() {
        assertFalse(handler.isGmConnected());
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.clients.addElement(handler.new Entry());
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String string = "Test";
        handler.sendOneSystemMsg(con, string);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testSendAllSystemMsg() {
        String string = "Test";
        handler.sendAllSystemMsg(string);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testWhisper() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String cmd = "user Hello";
        handler.whisper(con, cmd);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRollDice_Addition() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "3d6+5";
        handler.rollDice(con, cmd);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testRollDice_NoAddition() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "2d10";
        handler.rollDice(con, cmd);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testSendToOneStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String to = "Test";
        String send = "Hello";
        handler.sendToOneStr(con, to, send);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testUpdateHero_NullName() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero hero = new Hero();
        String result = handler.updateHero(con, hero);
        assertNull(result);
    }

    @Test
    public void testUpdateHero_NameNotNull() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        Hero hero = new Hero();
        String result = handler.updateHero(con, hero);
        assertEquals(null, result);
    }

    @Test
    public void testSendAllStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String string = "Test";
        handler.sendAllStr(con, string);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testSendAllObj() {
        String string = "Test";
        handler.sendAllObj(string);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testGetName() {
        assertNull(handler.getName(null));
        ConnectionEndpoint con = new ConnectionEndpoint();
        assertNull(handler.getName(con));
        handler.clients.addElement(handler.new Entry());
        assertEquals(null, handler.getName(con));
    }
}
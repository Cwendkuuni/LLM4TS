package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import dsachat.share.hero.*;
import dsachat.share.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {
    private Handler handler;
    private List<Entry> clients;
    
    @Before
    public void setUp() {
        handler = new Handler();
        clients = new ArrayList<Entry>();
    }
    
    @Test
    public void testAddConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        Entry entry = new Entry();
        entry.ce = cl;
        entry.name = "";
        assertEquals(entry, clients.get(0));
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        cl.setClientPort(1234);
        Entry entry1 = new Entry();
        Entry entry2 = new Entry();
        entry1.ce = cl;
        entry1.name = "client1";
        entry2.ce = new ConnectionEndpoint();
        entry2.name = "client2";
        clients.add(entry1);
        clients.add(entry2);
        handler.removeConnection(cl);
        assertEquals(1, clients.size());
        assertEquals("client2", clients.get(0).name);
    }

    @Test
    public void testHandleInputString() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), "message");
        handler.handleInput(e);
        assertNull(handler.handleInput.getInputEvent());

        e = new InputEvent(new ConnectionEndpoint(), "/command");
        e.setData("message");
        handler.handleInput(e);
        assertNull(handler.handleInput.getInputEvent());
    }

    @Test
    public void testHandleInputChallenge() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new Challenge());
        handler.handleInput(e);
        assertNull(handler.handleInput.getInputEvent());
    }

    @Test
    public void testHandleInputChallReq() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new ChallReq());
        handler.handleInput(e);
        assertNull(handler.handleInput.getInputEvent());
    }

    @Test
    public void testHandleInputHero() {
        InputEvent e = new InputEvent(new ConnectionEndpoint(), new Hero());
        handler.handleInput(e);
        assertNull(handler.handleInput.getInputEvent());
    }

    @Test
    public void testHandleInputUserEvent_noGmConnected() {
        UserEvent userEvent = new UserEvent(false, "test");
        InputEvent e = new InputEvent(new ConnectionEndpoint(), userEvent);
        handler.handleInput(e);
        assertEquals(userEvent, handler.handleInput.getInputEvent());
    }

    @Test
    public void testHandleInputUserEvent_gmConnected() {
        UserEvent userEvent = new UserEvent(false, "test");
        clients.add(new Entry());
        clients.get(0).name = "Gamemaster";
        InputEvent e = new InputEvent(new ConnectionEndpoint(), userEvent);
        handler.handleInput(e);
        assertNotNull(handler.handleInput.getInputEvent());
    }

    @Test
    public void testSendHeros() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        clients.add(new Entry());
        clients.get(0).hero = new Hero();
        handler.sendHeros(con);
        assertNotNull(con.send());
    }

    @Test
    public void testIsGmConnected() {
        clients.add(new Entry());
        clients.get(0).name = "Gamemaster";
        boolean result = handler.isGmConnected();
        assertTrue(result);
    }

    @Test
    public void testWhisper() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        clients.add(new Entry());
        clients.get(0).name = "client1";
        clients.add(new Entry());
        clients.get(1).name = "client2";
        handler.whisper(con, "client2 message");
        assertNotNull(clients.get(1).ce.send());
        assertNotNull(con.send());
    }

    @Test
    public void testRollDice_matchPlus() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.rollDice(con, "2w6+3");
        assertNotNull(con.send());
    }

    @Test
    public void testRollDice_noMatchPlus() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.rollDice(con, "2w6");
        assertNotNull(con.send());
    }

    @Test
    public void testSendToOneStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        clients.add(new Entry());
        clients.get(0).name = "client2";
        handler.sendToOneStr(con, "client2", "message");
        assertNotNull(clients.get(0).ce.send());
    }

    @Test
    public void testUpdateHero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero h = new Hero();
        clients.add(new Entry());
        clients.get(0).ce = con;
        String result = handler.updateHero(con, h);
        assertNull(result);
    }

    @Test
    public void testSendAllStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        clients.add(new Entry());
        handler.sendAllStr(con, "message");
        assertNotNull(clients.get(0).ce.send());
    }

    @Test
    public void testSendAllObj() {
        clients.add(new Entry());
        handler.sendAllObj(new Object());
        assertNotNull(clients.get(0).ce.send());
    }

    @Test
    public void testGetName() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        clients.add(new Entry());
        clients.get(0).ce = con;
        String result = handler.getName(con);
        assertNotNull(result);
    }
}
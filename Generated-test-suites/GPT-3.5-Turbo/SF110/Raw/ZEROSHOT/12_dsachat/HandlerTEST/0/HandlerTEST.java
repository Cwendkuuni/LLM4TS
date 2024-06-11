package dsachat.server;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

    Handler handler;

    @Before
    public void setUp() {
        handler = new Handler();
    }

    @Test
    public void testAddConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        // check if a new Entry is added to the clients vector
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.removeConnection(cl);
        // check if the Entry is removed from the clients vector
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput() {
        // create InputEvent
        InputEvent e = new InputEvent();
        // set data as String
        e.setData("Test");
        // create ConnectionEndpoint
        ConnectionEndpoint con = new ConnectionEndpoint();
        // set source as ConnectionEndpoint
        e.setSource(con);
        
        // test handleInput method
        
    }

    @Test
    public void testSendHeros() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        // create Hero object
        Hero hero = new Hero();
        handler.sendHeros(con);
        // check if the Hero object is sent to the connection endpoint
    }

    @Test
    public void testIsGmConnected() {
        // add a gamemaster to the clients vector
        Entry entry = new Entry();
        entry.name = "Gamemaster";
        handler.clients.add(entry);
        // check if the gamemaster is connected
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String string = "System Message";
        handler.sendOneSystemMsg(con, string);
        // check if the system message is sent to the connection endpoint
    }

    @Test
    public void testSendAllSystemMsg() {
        // create multiple connection endpoints
        ConnectionEndpoint con1 = new ConnectionEndpoint();
        ConnectionEndpoint con2 = new ConnectionEndpoint();
        ConnectionEndpoint con3 = new ConnectionEndpoint();
        handler.addConnection(con1);
        handler.addConnection(con2);
        handler.addConnection(con3);
        String string = "System Message";
        handler.sendAllSystemMsg(string);
        // check if the system message is sent to all connection endpoints
    }

    @Test
    public void testWhisper() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String cmd = "User Message";
        handler.whisper(con, cmd);
        // check if the user message is sent as a whisper to the specific user
        // check if the user message is sent as a system message to the sender
    }

    @Test
    public void testRollDice() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String cmd = "1d6+1";
        handler.rollDice(con, cmd);
        // check if the roll result is sent as a system message to all connection endpoints
    }

    @Test
    public void testSendToOneStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String to = "Receiver";
        String send = "Message";
        handler.sendToOneStr(con, to, send);
        // check if the message is sent to the specific receiver
    }

    @Test
    public void testUpdateHero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        Hero hero = new Hero();
        handler.updateHero(con, hero);
        // check if the hero is updated and associated with the connection endpoint
        // check if the hero's name is set as the client's name
    }

    @Test
    public void testSendAllStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        String string = "Message";
        handler.sendAllStr(con, string);
        // check if the message is sent as a system message to all connection endpoints
    }

    @Test
    public void testSendAllObj() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        Object o = new Object();
        handler.sendAllObj(o);
        // check if the object is sent to all connection endpoints
    }

    @Test
    public void testGetName() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.clients.elementAt(0).name = "Test";
        assertEquals("Test", handler.getName(con));
    }
}
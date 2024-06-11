package dsachat.server;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.Hero;
import dsachat.share.InputEvent;
import dsachat.share.UserEvent;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput_String() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String msg = "Hello world";
        InputEvent e = new InputEvent(con, msg);
        handler.handleInput(e);
        // check if the message was sent to all clients
    }

    @Test
    public void testHandleInput_Challenge() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Challenge challenge = new Challenge();
        InputEvent e = new InputEvent(con, challenge);
        handler.handleInput(e);
        // check if the challenge was sent to all clients or one specific client
    }

    @Test
    public void testHandleInput_ChallReq() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        ChallReq challReq = new ChallReq();
        InputEvent e = new InputEvent(con, challReq);
        handler.handleInput(e);
        // check if the ChallReq was sent to the correct client
    }

    @Test
    public void testHandleInput_Hero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero hero = new Hero();
        InputEvent e = new InputEvent(con, hero);
        handler.handleInput(e);
        // check if the hero is updated correctly or if a new connection is added
    }

    @Test
    public void testHandleInput_UserEvent() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        UserEvent userEvent = new UserEvent();
        InputEvent e = new InputEvent(con, userEvent);
        handler.handleInput(e);
        // check if the user event is handled correctly, like updating the name of a client or adding a Gamemaster
    }

    @Test
    public void testSendHeros() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        handler.addConnection(con);
        handler.sendHeros(con);
        // check if the heroes are sent correctly to the client
    }

    @Test
    public void testIsGmConnected() {
        // test with no Gamemaster connection
        assertEquals(false, handler.isGmConnected());
        // test with Gamemaster connection
        ConnectionEndpoint gmCon = new ConnectionEndpoint();
        gmCon.setClientPort(1234); // set the client port to identify Gamemaster
        handler.addConnection(gmCon);
        assertEquals(true, handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String msg = "Hello world";
        String expected = "Hello world";
        handler.sendOneSystemMsg(con, msg);
        // check if the message is sent to the correct client
    }

    @Test
    public void testSendAllSystemMsg() {
        String msg = "Hello world";
        handler.sendAllSystemMsg(msg);
        // check if the message is sent to all clients
    }

    @Test
    public void testWhisper() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "Player2 Hello";
        handler.addConnection(con);
        handler.addConnection(new ConnectionEndpoint("Player2"));
        handler.whisper(con, cmd);
        // check if the message is sent to the correct client
    }

    @Test
    public void testRollDice() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String cmd = "2w6+3";
        handler.rollDice(con, cmd);
        // check if the result is calculated correctly and sent to all clients
    }

    @Test
    public void testSendToOneStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String to = "Player2";
        String send = "Hello from Player1";
        handler.addConnection(con);
        handler.addConnection(new ConnectionEndpoint("Player2"));
        handler.sendToOneStr(con, to, send);
        // check if the message is sent to the correct client
    }

    @Test
    public void testUpdateHero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero hero = new Hero("Player1");
        handler.addConnection(con);
        String expected = "Player1";
        String result = handler.updateHero(con, hero);
        assertEquals(expected, result);
        // check if the hero is updated correctly and if the client name is returned
    }

    @Test
    public void testSendAllStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String string = "Hello world";
        handler.addConnection(con);
        handler.sendAllStr(con, string);
        // check if the message is sent to all clients except the sender
    }

    @Test
    public void testSendAllObj() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Object o = new Object();
        handler.addConnection(con);
        handler.sendAllObj(o);
        // check if the object is sent to all clients
    }

    @Test
    public void testGetName() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        String expected = null;
        String result = handler.getName(con);
        assertEquals(expected, result);

        ConnectionEndpoint playerCon = new ConnectionEndpoint();
        playerCon.setClientPort(1234);
        String name = "Player1";
        handler.addConnection(playerCon);
        handler.clients.get(0).name = name;
        expected = name;
        result = handler.getName(playerCon);
        assertEquals(expected, result);
        // check if the correct name is returned for the given connection
    }
}
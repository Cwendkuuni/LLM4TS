package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {
    private Handler handler;

    @Before
    public void setup() {
        handler = new Handler();
    }

    @Test
    public void testAddConnection() {
        // Test empty vector
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        handler.addConnection(cl1);
        assertEquals(1, handler.clients.size());
        assertEquals(cl1, handler.clients.elementAt(0).ce);
        assertEquals("", handler.clients.elementAt(0).name);

        // Test vector with elements
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        ConnectionEndpoint cl3 = new ConnectionEndpoint();
        handler.clients.addElement(new Entry());
        handler.clients.elementAt(0).name = "client1";
        handler.clients.elementAt(0).ce = cl2;

        handler.clients.addElement(new Entry());
        handler.clients.elementAt(1).name = "client2";
        handler.clients.elementAt(1).ce = cl3;

        handler.addConnection(cl1);

        assertEquals(3, handler.clients.size());
        assertEquals(cl2, handler.clients.elementAt(0).ce);
        assertEquals(cl3, handler.clients.elementAt(1).ce);
        assertEquals(cl1, handler.clients.elementAt(2).ce);
    }

    @Test
    public void testRemoveConnection() {
        // Test vector with multiple elements
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        handler.clients.addElement(new Entry());
        handler.clients.elementAt(0).name = "client1";
        handler.clients.elementAt(0).ce = new ConnectionEndpoint();

        handler.clients.addElement(new Entry());
        handler.clients.elementAt(1).name = "client2";
        handler.clients.elementAt(1).ce = cl1;

        handler.clients.addElement(new Entry());
        handler.clients.elementAt(2).name = "client3";
        handler.clients.elementAt(2).ce = new ConnectionEndpoint();

        handler.removeConnection(cl1);

        assertEquals(2, handler.clients.size());
        assertEquals("client1", handler.clients.elementAt(0).name);
        assertEquals("client3", handler.clients.elementAt(1).name);
    }

    @Test
    public void testHandleInput_string() {
        ConnectionEndpoint con = new ConnectionEndpoint();

        // Test command match dice
        InputEvent e1 = new InputEvent("/dice 1d6+3", con);
        handler.handleInput(e1);
        assertEquals("1d6+3", handler.getClientName(0));
        assertNull(handler.getClientName(1));

        // Test command match whisper
        InputEvent e2 = new InputEvent("/whisper user message", con);
        handler.handleInput(e2);
        assertEquals("user", handler.getClientName(0));
        assertNull(handler.getClientName(1));

        // Test command match me
        InputEvent e3 = new InputEvent("/me action", con);
        handler.handleInput(e3);
        assertEquals("action", handler.getClientName(0));
        assertNull(handler.getClientName(1));

        // Test unfamiliar command
        InputEvent e4 = new InputEvent("/command arg", con);
        handler.handleInput(e4);
        assertEquals("/command arg", handler.getClientName(0));
        assertNull(handler.getClientName(1));
    }

    @Test
    public void testSendHeros() {
        ConnectionEndpoint con1 = new ConnectionEndpoint();
        ConnectionEndpoint con2 = new ConnectionEndpoint();
        ConnectionEndpoint con3 = new ConnectionEndpoint();
        handler.clients.addElement(new Entry());
        handler.clients.elementAt(0).hero = new Hero("hero1");
        handler.clients.elementAt(0).ce = con1;

        handler.clients.addElement(new Entry());
        handler.clients.elementAt(1).hero = new Hero("hero2");
        handler.clients.elementAt(1).ce = con2;

        handler.clients.addElement(new Entry());

        try {
            handler.sendHeros(con3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Vector<Hero> expected = new Vector<Hero>();
        expected.addElement(new Hero("hero1"));
        expected.addElement(new Hero("hero2"));

        assertEquals(expected, con3.getSentObjects());
    }

    @Test
    public void testUpdateHero() {
        ConnectionEndpoint con1 = new ConnectionEndpoint();
        ConnectionEndpoint con2 = new ConnectionEndpoint();

        Hero hero1 = new Hero("hero1");
        Hero hero2 = new Hero("hero2");

        handler.addConnection(con1);
        handler.updateHero(con1, hero1);
        assertEquals("hero1", handler.getClientName(0));

        handler.addConnection(con2);
        handler.updateHero(con2, hero2);
        assertEquals("hero2", handler.getClientName(1));
    }
}
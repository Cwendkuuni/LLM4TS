import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
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
        Vector<Entry> expected = new Vector<Entry>();
        Entry n = new Entry();
        n.ce = cl;
        n.name = "";
        expected.addElement(n);
        assertEquals(expected, handler.clients);
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Entry n = new Entry();
        n.ce = cl;
        n.name = "test";
        handler.clients.addElement(n);
        handler.removeConnection(cl);
        Vector<Entry> expected = new Vector<Entry>();
        assertEquals(expected, handler.clients);
    }

    @Test
    public void testHandleInput() {
        Handler handler = new Handler();
        InputEvent e = new InputEvent(new Object(), new ConnectionEndpoint());
        handler.handleInput(e);
        /* 
        Test handleInput for different input events if necessary
        */
    }

    @Test
    public void testSendHeros() {
        Handler handler = new Handler();
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero h1 = new Hero("hero1");
        Hero h2 = new Hero("hero2");
        Entry e1 = new Entry();
        e1.ce = new ConnectionEndpoint();
        e1.ce.send(h1);
        Entry e2 = new Entry();
        e2.ce = new ConnectionEndpoint();
        e2.ce.send(h2);
        handler.clients.addElement(e1);
        handler.clients.addElement(e2);
        handler.sendHeros(con);
        Vector<Hero> expected = new Vector<Hero>();
        expected.addElement(h1);
        expected.addElement(h2);
        assertEquals(expected, con.sentData);
    }
    
    /* Add additional test methods for the remaining methods in Handler class */
}
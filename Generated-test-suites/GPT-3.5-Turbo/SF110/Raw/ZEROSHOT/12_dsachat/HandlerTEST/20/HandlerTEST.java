import org.junit.Test;
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
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput() {
        Handler handler = new Handler();
        InputEvent e = new InputEvent();
        handler.handleInput(e);
        // add your assertions here
    }

    @Test
    public void testSendHeros() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.clients.elementAt(0).hero = new Hero();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(handler.clients.elementAt(0).hero);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<Hero> vh = new Vector<Hero>();
        vh.addElement(handler.clients.elementAt(0).hero);
        assertEquals(vh, ois.readObject());
    }

    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        handler.addConnection(new ConnectionEndpoint());
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl.setStringWriter(new PrintWriter(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cl.send("test");
        assertTrue(baos.toString().contains("test"));
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl1.setStringWriter(new PrintWriter(baos));
            cl2.setStringWriter(new PrintWriter(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.sendAllSystemMsg("test");
        assertTrue(baos.toString().contains("test"));
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl1.setStringWriter(new PrintWriter(baos));
            cl2.setStringWriter(new PrintWriter(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.clients.elementAt(1).name = "Client2";
        handler.whisper(cl1, "Client2 test message");
        assertTrue(baos.toString().contains("Client1 whispers: test message"));
        assertTrue(baos.toString().contains("You whisper to Client2: test message"));
    }

    @Test
    public void testRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl.setStringWriter(new PrintWriter(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.addConnection(cl);
        handler.rollDice(cl, "2d6");
        assertTrue(baos.toString().matches("Client1 rolls 2d6 --> (\\d+)(\\s(\\d+))+"));
    }

    @Test
    public void testSendToOneStr() {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl1.setStringWriter(new PrintWriter(baos));
            cl2.setStringWriter(new PrintWriter(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.sendToOneStr(cl1, "Client2", "test message");
        assertTrue(baos.toString().contains("Client1: test message"));
    }

    @Test
    public void testUpdateHero() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Hero hero = new Hero();
        handler.addConnection(cl);
        handler.updateHero(cl, hero);
        assertEquals(hero, handler.clients.elementAt(0).hero);
    }

    @Test
    public void testSendAllStr() {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl1.setStringWriter(new PrintWriter(baos));
            cl2.setStringWriter(new PrintWriter(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        handler.sendAllStr(cl1, "test message");
        assertTrue(baos.toString().contains("Client1: test message"));
        assertTrue(baos.toString().contains("Client2: test message"));
    }

    @Test
    public void testSendAllObj() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            cl.setObjectOutputStream(new ObjectOutputStream(baos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.addConnection(cl);
        handler.sendAllObj(new UserEvent(false, "test"));
        assertTrue(baos.toString().contains("test"));
    }

    @Test
    public void testGetName() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        assertEquals("Client1", handler.getName(cl));
    }

}
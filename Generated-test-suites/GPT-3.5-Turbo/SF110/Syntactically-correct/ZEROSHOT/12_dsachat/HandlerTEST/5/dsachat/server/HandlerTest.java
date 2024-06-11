import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.util.regex.*;

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
    public void testHandleInput() {
        Handler.InputEvent e = new Handler.InputEvent(new Challenge());
        handler.handleInput(e);
        
        Handler.InputEvent e2 = new Handler.InputEvent(new ChallReq());
        handler.handleInput(e2);
        
        Handler.InputEvent e3 = new Handler.InputEvent(new Hero());
        handler.handleInput(e3);
        
        Handler.InputEvent e4 = new Handler.InputEvent(new UserEvent());
        handler.handleInput(e4);
    }
    
    @Test
    public void testSendHeros() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Handler.Entry entry = new Handler.Entry();
        entry.hero = new Hero();
        handler.clients.add(entry);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.sendHeros(con);
        
        String expectedOutput = "[Hero@";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testIsGmConnected() {
        Handler.Entry entry = new Handler.Entry();
        entry.name = "Gamemaster";
        handler.clients.add(entry);
        
        assertTrue(handler.isGmConnected());
    }
    
    @Test
    public void testSendOneSystemMsg() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.sendOneSystemMsg(con, "hello");
        
        assertEquals("hello\n", outContent.toString());
    }
    
    @Test
    public void testSendAllSystemMsg() {
        Handler.Entry entry = new Handler.Entry();
        entry.ce = new ConnectionEndpoint();
        handler.clients.add(entry);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.sendAllSystemMsg("hello");
        
        assertEquals("hello\n", outContent.toString());
    }
    
    @Test
    public void testWhisper() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Handler.Entry entry = new Handler.Entry();
        entry.name = "Bob";
        entry.ce = new ConnectionEndpoint();
        handler.clients.add(entry);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.whisper(con, "Bob hello");
        
        assertTrue(outContent.toString().contains("You whisper to Bob: hello"));
    }
    
    @Test
    public void testRollDice() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.rollDice(con, "2d6+1");
        handler.rollDice(con, "3d10");
        
        assertTrue(outContent.toString().contains("rolled 2d6+1 --> "));
        assertTrue(outContent.toString().contains("rolls 3d10 --> "));
    }
    
    @Test
    public void testSendToOneStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Handler.Entry entry = new Handler.Entry();
        entry.name = "Bob";
        entry.ce = new ConnectionEndpoint();
        handler.clients.add(entry);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.sendToOneStr(con, "Bob", "hello");
        
        assertEquals("hello\n", outContent.toString());
    }
    
    @Test
    public void testUpdateHero() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Hero h = new Hero();
        
        handler.addConnection(con);
        
        String name = handler.updateHero(con, h);
        
        assertEquals(h.getName(), name);
    }
    
    @Test
    public void testSendAllStr() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Handler.Entry entry = new Handler.Entry();
        entry.ce = new ConnectionEndpoint();
        handler.clients.add(entry);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        handler.sendAllStr(con, "Hello");
        
        assertEquals("Hello\n", outContent.toString());
    }
    
    @Test
    public void testSendAllObj() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Handler.Entry entry = new Handler.Entry();
        entry.ce = new ConnectionEndpoint();
        handler.clients.add(entry);
        
        handler.sendAllObj(new UserEvent());
        
        assertEquals(1, entry.ce.sentObjects.size());
    }
    
    @Test
    public void testGetName() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        Handler.Entry entry = new Handler.Entry();
        entry.ce = new ConnectionEndpoint();
        entry.name = "Bob";
        handler.clients.add(entry);
        
        String name = handler.getName(con);
        
        assertEquals("Bob", name);
    }
}
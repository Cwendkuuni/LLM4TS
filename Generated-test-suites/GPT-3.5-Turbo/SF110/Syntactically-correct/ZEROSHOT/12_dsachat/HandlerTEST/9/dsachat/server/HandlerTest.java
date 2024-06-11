import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import dsachat.server.*;
import java.io.IOException;

@RunWith(JUnit4.class)
public class HandlerTest {
    Handler handler;
    ConnectionEndpoint connection;
    
    @Before
    public void setUp() {
        handler = new Handler();
        connection = new ConnectionEndpoint();
    }
    
    @After
    public void tearDown() {
        handler = null;
        connection = null;
    }
    
    @Test
    public void testAddConnection() {
        handler.addConnection(connection);
        assertEquals(1, handler.getClients().size());
    }
    
    @Test
    public void testRemoveConnection() {
        handler.addConnection(connection);
        handler.removeConnection(connection);
        assertEquals(0, handler.getClients().size());
    }
    
    @Test
    public void testHandleInput() {
        InputEvent event = new InputEvent("Test Input");
        handler.handleInput(event);
        //Add assertions
    }
    
    @Test
    public void testSendHeros() {
        //Add test case
    }
    
    @Test
    public void testIsGmConnected() {
        //Add test case
    }
    
    @Test
    public void testSendOneSystemMsg() {
        //Add test case
    }
    
    @Test
    public void testSendAllSystemMsg() {
        //Add test case
    }
    
    @Test
    public void testWhisper() {
        //Add test case
    }
    
    @Test
    public void testRollDice() {
        //Add test case
    }
    
    @Test
    public void testSendToOneStr() {
        //Add test case
    }
    
    @Test
    public void testUpdateHero() {
        //Add test case
    }
    
    @Test
    public void testSendAllStr() {
        //Add test case
    }
    
    @Test
    public void testSendAllObj() {
        //Add test case
    }
    
    @Test
    public void testGetName() {
        //Add test case
    }
}
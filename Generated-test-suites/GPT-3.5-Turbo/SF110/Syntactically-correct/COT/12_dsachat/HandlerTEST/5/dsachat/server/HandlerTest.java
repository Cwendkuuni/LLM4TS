import org.junit.Test;
import static org.junit.Assert.*;

public class HandlerTest {
    
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        
        handler.addConnection(cl);
        // Assert the size of clients vector should be 1
        assertEquals(1, handler.clients.size());
        
        // Assert the client name should be empty
        assertEquals("", handler.clients.get(0).name);
    }
    
    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        
        handler.removeConnection(cl1);
        // Assert the size of clients vector should be 1 after removing cl1
        assertEquals(1, handler.clients.size());
        
        // Assert the client name should be empty after removing cl1
        assertEquals("", handler.clients.get(0).name);
        
        handler.removeConnection(cl2);
        // Assert the size of clients vector should be 0 after removing cl2
        assertEquals(0, handler.clients.size());
    }
    
    // Additionally, test cases for handleInput, sendHeros, isGmConnected, sendOneSystemMsg, sendAllSystemMsg, whisper, rollDice, sendToOneStr, updateHero, sendAllStr, sendAllObj, and getName methods can be written
    
}
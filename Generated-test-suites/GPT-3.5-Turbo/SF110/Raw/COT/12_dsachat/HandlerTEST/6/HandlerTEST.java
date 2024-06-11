
import static org.junit.Assert.*;
import org.junit.Test;

public class HandlerTest {
    
    @Test
    public void testAddConnection() {
        // Test addConnection method
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        
        // Check if client entry is added
        assertEquals(1, handler.clients.size());
    }
    
    @Test
    public void testRemoveConnection() {
        // Test removeConnection method
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        
        // Remove the connection
        handler.removeConnection(cl);
        
        // Check if client entry is removed
        assertEquals(0, handler.clients.size());
    }
    
    @Test
    public void testHandleInput_StringCommand() {
        // Test handleInput method with a String command
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        
        // Prepare input event with a String command
        String command = "/say Hello World!";
        InputEvent inputEvent = new InputEvent(command, cl);
        handler.handleInput(inputEvent);
        
        // Check if the message is sent to all clients
        assertEquals("*: Hello World!", cl.getLastSentMessage());
    }
    
    @Test
    public void testHandleInput_Challenge() {
        // Test handleInput method with a Challenge object
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        
        // Prepare input event with a Challenge object
        Challenge challenge = new Challenge("roll", "player1", false);
        InputEvent inputEvent = new InputEvent(challenge, cl);
        handler.handleInput(inputEvent);
        
        // Check if the challenge is sent to all clients
        assertEquals("player1 rolled roll --> ", cl.getLastSentMessage());
    }
    
    @Test
    public void testHandleInput_ChallReq() {
        // Test handleInput method with a ChallReq object
        Handler handler = new Handler();
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        handler.addConnection(cl1);
        handler.addConnection(cl2);
        
        // Prepare input event with a ChallReq object
        ChallReq challReq = new ChallReq("player2", "roll", false);
        InputEvent inputEvent = new InputEvent(challReq, cl1);
        handler.handleInput(inputEvent);
        
        // Check if the ChallReq is sent to the correct client
        assertEquals(challReq, cl2.getLastSentObject());
    }
    
}

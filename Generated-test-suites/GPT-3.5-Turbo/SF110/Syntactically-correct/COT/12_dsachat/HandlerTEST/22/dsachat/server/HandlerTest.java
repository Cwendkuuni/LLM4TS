
import org.junit.Test;
import java.io.IOException;

public class HandlerTest {
    
    @Test
    public void testAddConnection() throws IOException {
        // Test functionality of addConnection() method
        
        // Test case 1: Adding a connection when clients vector is empty
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        Handler handler1 = new Handler();
        handler1.addConnection(cl1);
        
        // Test case 2: Adding a connection when clients vector is not empty
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        Handler handler2 = new Handler();
        ConnectionEndpoint cl3 = new ConnectionEndpoint();
        handler2.addConnection(cl2);
        handler2.addConnection(cl3);
    }
    
    @Test
    public void testRemoveConnection() throws IOException {
        // Test functionality of removeConnection() method
        
        // Test case 1: Removing a connection 
        ConnectionEndpoint cl1 = new ConnectionEndpoint();
        Handler handler1 = new Handler();
        handler1.addConnection(cl1);
        handler1.removeConnection(cl1);
        
        // Test case 2: Removing a connection that does not exist
        ConnectionEndpoint cl2 = new ConnectionEndpoint();
        Handler handler2 = new Handler();
        handler2.removeConnection(cl2);
    }
    
    @Test
    public void testHandleInput() {
        // Test functionality of handleInput() method
        
        // Test case 1: Handling input with object of String class
        InputEvent e1 = new InputEvent(new String("Test"), new ConnectionEndpoint());
        Handler handler1 = new Handler();
        handler1.handleInput(e1);
        
        // Test case 2: Handling input with object of Challenge class
        InputEvent e2 = new InputEvent(new Challenge(), new ConnectionEndpoint());
        Handler handler2 = new Handler();
        handler2.handleInput(e2);
        
        // Test case 3: Handling input with object of ChallReq class
        InputEvent e3 = new InputEvent(new ChallReq(), new ConnectionEndpoint());
        Handler handler3 = new Handler();
        handler3.handleInput(e3);
        
        // Test case 4: Handling input with object of Hero class
        InputEvent e4 = new InputEvent(new Hero(), new ConnectionEndpoint());
        Handler handler4 = new Handler();
        handler4.handleInput(e4);
        
        // Test case 5: Handling input with object of UserEvent class and when no Gamemaster is connected
        InputEvent e5 = new InputEvent(new UserEvent(), new ConnectionEndpoint());
        Handler handler5 = new Handler();
        handler5.handleInput(e5);
        
        // Test case 6: Handling input with object of UserEvent class and when a Gamemaster is already connected
        InputEvent e6 = new InputEvent(new UserEvent(), new ConnectionEndpoint());
        Handler handler6 = new Handler();
        handler6.addConnection(new ConnectionEndpoint());
        handler6.handleInput(e6);
    }
    
    @Test
    public void testSendHeros() throws IOException {
        // Test functionality of sendHeros() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: No Heroes are added to the clients vector
        handler.sendHeros(cl);
        
        // Test case 2: Heroes are added to the clients vector
        handler.addConnection(cl);
        handler.sendHeros(cl);
    }
    
    @Test
    public void testIsGmConnected() {
        // Test functionality of isGmConnected() method
        
        // Test case 1: Gamemaster is not connected
        Handler handler1 = new Handler();
        assert(handler1.isGmConnected() == false);
        
        // Test case 2: Gamemaster is connected
        Handler handler2 = new Handler();
        handler2.addConnection(new ConnectionEndpoint());
        assert(handler2.isGmConnected() == true);
    }
    
    @Test
    public void testSendOneSystemMsg() throws IOException {
        // Test functionality of sendOneSystemMsg() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Sending a system message to a connection
        handler.sendOneSystemMsg(cl, "Test");   
    }
    
    @Test
    public void testSendAllSystemMsg() throws IOException {
        // Test functionality of sendAllSystemMsg() method
        
        Handler handler = new Handler();
        
        // Test case 1: Sending a system message to all connections
        handler.sendAllSystemMsg("Test");
    }
    
    @Test
    public void testWhisper() throws IOException {
        // Test functionality of whisper() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Whispering to a client by name
        handler.addConnection(cl);
        handler.whisper(cl, "Client1 Test");
    }
    
    @Test
    public void testRollDice() throws IOException {
        // Test functionality of rollDice() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Rolling dice with modifiers
        handler.addConnection(cl);
        handler.rollDice(cl, "2d6+2");
        
        // Test case 2: Rolling dice without modifiers
        handler.rollDice(cl, "3d10");
    }
    
    @Test
    public void testSendToOneStr() throws IOException {
        // Test functionality of sendToOneStr() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Sending a message to a specific client
        handler.addConnection(cl);
        handler.sendToOneStr(cl, "Client1", "Test");
    }
    
    @Test
    public void testUpdateHero() throws IOException {
        // Test functionality of updateHero() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Updating a hero's information
        handler.addConnection(cl);
        handler.updateHero(cl, new Hero());
        
        // Test case 2: Updating a hero's information with new name
        handler.updateHero(cl, new Hero());
    }
    
    @Test
    public void testSendAllStr() throws IOException {
        // Test functionality of sendAllStr() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Sending a message to all clients
        handler.addConnection(cl);
        handler.sendAllStr(cl, "Test");
    }
    
    @Test
    public void testSendAllObj() throws IOException {
        // Test functionality of sendAllObj() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Sending an object to all clients
        handler.addConnection(cl);
        handler.sendAllObj(new String("Test"));
    }
    
    @Test
    public void testGetName() throws IOException {
        // Test functionality of getName() method
        
        ConnectionEndpoint cl = new ConnectionEndpoint();
        Handler handler = new Handler();
        
        // Test case 1: Getting name of a client
        handler.addConnection(cl);
        handler.getName(cl);
        
    }
}


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.share.hero.*;
import java.io.IOException;
import dsachat.server.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint ce1;
    private ConnectionEndpoint ce2;
    private ConnectionEndpoint ce3;

    @Before
    public void setUp() {
        handler = new Handler();
        ce1 = new ConnectionEndpoint("Client 1");
        ce2 = new ConnectionEndpoint("Client 2");
        ce3 = new ConnectionEndpoint("Client 3");
    }

    // Test addConnection method
    
    @Test
    public void testAddConnection() {
        handler.addConnection(ce1);
        assertEquals(1, handler.getClients().size());
    }
    
    @Test
    public void testAddConnectionWithConnectedClient() {
        // It should wait for the client to be connected before adding it
        new Thread(() -> {
          try {
            Thread.sleep(500);
            ce1.setConnected(true);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }).start();
        
        handler.addConnection(ce1);
        assertEquals(1, handler.getClients().size());
    }
    
    // Test removeConnection method
    
    @Test
    public void testRemoveConnection() {
        handler.addConnection(ce1);
        handler.removeConnection(ce1);
        assertEquals(0, handler.getClients().size());
    }
    
    @Test
    public void testRemoveConnectionWithExistingConnection() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        handler.addConnection(ce3);
        handler.removeConnection(ce2);
        assertEquals(2, handler.getClients().size());
    }
    
    @Test
    public void testRemoveConnectionWithNonExistingConnection() {
        handler.addConnection(ce1);
        handler.removeConnection(ce2);
        assertEquals(1, handler.getClients().size());
    }
    
    // Test handleInput method
    
    @Test
    public void testHandleInputWithStringInput() {
        try {
            ce1.send("Test message");
            handler.addConnection(ce1);
            Thread.sleep(500);
            handler.handleInput(new InputEvent("Test message", ce1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testHandleInputWithChallengeInput() {
        try {
            Challenge challenge = new Challenge("Player 1");
            ce1.send(challenge);
            handler.addConnection(ce1);
            Thread.sleep(500);
            handler.handleInput(new InputEvent(challenge, ce1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testHandleInputWithChallReqInput() {
        try {
            ChallReq challReq = new ChallReq("Player 2");
            ce1.send(challReq);
            handler.addConnection(ce1);
            Thread.sleep(500);
            handler.handleInput(new InputEvent(challReq, ce1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testHandleInputWithHeroInput() {
        try {
            Hero hero = new Hero("Player 3");
            ce1.send(hero);
            handler.addConnection(ce1);
            Thread.sleep(500);
            handler.handleInput(new InputEvent(hero, ce1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testHandleInputWithUserEventInputAndGMNotConnected() {
        try {
            UserEvent userEvent = new UserEvent(true, "Player 4");
            ce1.send(userEvent);
            handler.addConnection(ce1);
            Thread.sleep(500);
            handler.handleInput(new InputEvent(userEvent, ce1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Additional tests
    
    @Test
    public void testSendHeros() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        handler.addConnection(ce3);
        
        Hero hero = new Hero("Player 1");
        ce1.setHero(hero);
        
        Hero hero2 = new Hero("Player 2");
        ce2.setHero(hero2);
        
        Hero hero3 = new Hero("Player 3");
        ce3.setHero(hero3);
        
        try {
            handler.sendHeros(ce1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsGmConnectedWithGmConnected() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        
        ce2.setName("Gamemaster");
        
        assertTrue(handler.isGmConnected());
    }
    
    @Test
    public void testIsGmConnectedWithoutGmConnected() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        
        assertFalse(handler.isGmConnected());
    }
    
    @Test
    public void testWhisper() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        handler.addConnection(ce3);
        
        try {
            handler.whisper(ce1, "Player 2 Hello, how are you?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testRollDiceWithModifier() {
        handler.rollDice(ce1, "1w6+2");
    }
    
    @Test
    public void testRollDiceWithoutModifier() {
        handler.rollDice(ce1, "2w10");
    }
    
    @Test
    public void testSendToOneStr() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        
        try {
            handler.sendToOneStr(ce1, "Player 2", "Hello!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdateHeroWithNewConnection() {
        handler.addConnection(ce1);
        Hero hero = new Hero("Player 1");
        
        handler.updateHero(ce1, hero);
        assertEquals(hero, ce1.getHero());
        assertEquals("Player 1", handler.getName(ce1));
    }
    
    @Test
    public void testUpdateHeroWithExistingConnection() {
        handler.addConnection(ce1);
        
        Hero hero = new Hero("Player 1");
        ce1.setHero(hero);
        
        handler.updateHero(ce1, hero);
        assertNull(handler.getName(ce1));
    }
    
    @Test
    public void testSendAllStr() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        
        try {
            handler.sendAllStr(ce1, "Hello!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSendAllObj() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        
        try {
            handler.sendAllObj("Hello!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetName() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        
        assertEquals("Client 1", handler.getName(ce1));
    }

}
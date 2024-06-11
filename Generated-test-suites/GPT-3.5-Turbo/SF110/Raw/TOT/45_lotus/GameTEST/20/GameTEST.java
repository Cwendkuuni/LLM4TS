import static org.junit.Assert.*;
import org.junit.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        
        Player result = Game.getPlayingPlayer();
        
        assertEquals(result, Game.player1);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        
        Player result = Game.getNonPlayingPlayer();
        
        assertEquals(result, Game.player2);
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        
        Player result = Game.getOtherPlayer(Game.player1);
        
        assertEquals(result, Game.player2);
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Check if player1 and player2 are initialized correctly
        assertEquals(Game.player1, new Player(IP1, n1, deck1));
        assertEquals(Game.player2, new Player(IP2, n2, deck2));
        
        // Check if the init method is called for IP1 and IP2
        assertTrue(IP1.initCalled);
        assertTrue(IP2.initCalled);
        
        // Check if playingPlayer is set to 1 and currentPhase is set to UntapPhase
        assertEquals(Game.playingPlayer, 1);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Check if the owner and zone are correctly set for cards in deck1 and deck2
        for (Card c : deck1) {
            assertEquals(c.owner, Game.player1);
            assertEquals(c.zone, Game.player1.library);
        }
        for (Card c : deck2) {
            assertEquals(c.owner, Game.player2);
            assertEquals(c.zone, Game.player2.library);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = new Stack();
        
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        
        player1.letPlayerSpeakCalled = false;
        player2.letPlayerSpeakCalled = false;
        
        Game.givePriorityToCurrentPlayer();
        
        // Check if letPlayerSpeak method is called for player1 and player2
        assertTrue(player1.letPlayerSpeakCalled);
        assertTrue(player2.letPlayerSpeakCalled);
        
        // Check if stack is resolved if it is not empty
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        
        // Check if stack.resolveLast() is called
        assertFalse(Game.stack.containsCard());
    }
}
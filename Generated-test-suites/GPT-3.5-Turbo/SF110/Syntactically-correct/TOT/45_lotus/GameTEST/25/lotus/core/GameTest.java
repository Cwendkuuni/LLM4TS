import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        Game.playingPlayer = 1;
        Game.player1 = new Player();
        Game.player2 = new Player();
        
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        
        assertEquals(Game.player2, Game.getNonPlayingPlayer(Game.player1));
        assertEquals(Game.player1, Game.getNonPlayingPlayer(Game.player2));
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
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
        
        assertEquals(IP1, Game.player1.userInterface);
        assertEquals(n1, Game.player1.name);
        assertEquals(deck1, Game.player1.deck);
        
        assertEquals(IP2, Game.player2.userInterface);
        assertEquals(n2, Game.player2.name);
        assertEquals(deck2, Game.player2.deck);
        
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = new Stack();
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        Game.player1 = new Player(IP1);
        Game.player2 = new Player(IP2);
        
        // Mocking player's turn-based actions
        Game.player1.playerSpeakResult = false;
        Game.player2.playerSpeakResult = false;
        
        Game.givePriorityToCurrentPlayer();
        
        assertEquals(true, Game.player1.playerSpeakCalled);
        assertEquals(true, Game.player2.playerSpeakCalled);
        
        assertEquals(true, Game.stack.resolveLastCalled);
        
        assertEquals(false, Game.stack.empty());
        assertTrue(Game.stack.resolveLastCalled);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        
        assertEquals("Player1", Game.player1.getPlayerName());
        assertEquals("Player2", Game.player2.getPlayerName());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Edge case
        Game.stack.insert(new Card());
        Game.givePriorityToCurrentPlayer();
        
        // Error scenario
        Game.stack.insert(new Card());
        Game.stack.insert(new Card());
        Game.stack.insert(new Card());
        Game.givePriorityToCurrentPlayer();
    }
}
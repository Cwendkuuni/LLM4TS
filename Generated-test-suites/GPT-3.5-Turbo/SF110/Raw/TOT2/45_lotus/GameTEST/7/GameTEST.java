import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical Use Case
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge Case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical Use Case
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge Case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        // Typical Use Case
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.currentPhase);
        assertNotNull(Game.stack);
        
        // Error Scenario
        Game.init(null, new UserInterface(), "ErrorPlayer1", "ErrorPlayer2", new CardCollection(), new CardCollection());
        assertNull(Game.player1);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical Use Case
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Game.givePriorityToCurrentPlayer();
        
        // Edge Case
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
    }

}
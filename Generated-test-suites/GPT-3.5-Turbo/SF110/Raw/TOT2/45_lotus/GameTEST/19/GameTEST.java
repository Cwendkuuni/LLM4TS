import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test for playing player equal to player1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test for playing player equal to player2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test for non playing player when playing player is player1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test for non playing player when playing player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test for getting other player when input player is player1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Test for getting other player when input player is player2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Test initialization of players, decks, and current phase
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test priority given to current player
        // Not easy to test as it involves player interactions
        // Can be further extended with mock objects for player interactions
    }
}
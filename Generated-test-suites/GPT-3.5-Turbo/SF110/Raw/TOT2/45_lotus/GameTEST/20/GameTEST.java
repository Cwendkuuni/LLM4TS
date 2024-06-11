import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Testing when the playing player is player1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Testing when the playing player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Testing when the current player is player1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Testing when the current player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        // Test initializing the game with player names and decks
        // Add deck1, deck2, user interfaces and names to test initialization
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test giving priority to the current player and resolving the stack
        // Add stack operations to test the method functionality
    }
}
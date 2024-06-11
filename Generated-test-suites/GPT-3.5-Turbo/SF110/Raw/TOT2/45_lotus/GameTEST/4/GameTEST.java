import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical Use Case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Sample edge cases
        try {
            Game.init(null, null, "", "", null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Error Scenario
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
    
    // More test methods for other methods in the Game class can be added here
    
}
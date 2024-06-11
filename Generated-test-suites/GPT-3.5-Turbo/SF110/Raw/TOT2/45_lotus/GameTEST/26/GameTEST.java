import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
  
    @Test
    public void testGetPlayingPlayer() {
        // Test when playing player is player1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test when playing player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test with player1 as argument
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test with player2 as argument
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test with player1 as argument
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test with player2 as argument
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Test method functionality
        // Add specific edge cases and exception handling scenarios here
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test method functionality
        // Add specific edge cases and exception handling scenarios here
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player1;
        assertEquals(expectedPlayer, Game.getPlayingPlayer());

        // Edge case
        Game.playingPlayer = 2;
        expectedPlayer = Game.player2;
        assertEquals(expectedPlayer, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player2;
        assertEquals(expectedPlayer, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        expectedPlayer = Game.player1;
        assertEquals(expectedPlayer, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Player inputPlayer = Game.player1;
        Player expectedPlayer = Game.player2;
        assertEquals(expectedPlayer, Game.getOtherPlayer(inputPlayer));

        // Edge case
        inputPlayer = Game.player2;
        expectedPlayer = Game.player1;
        assertEquals(expectedPlayer, Game.getOtherPlayer(inputPlayer));
    }

    // Other test methods for init, givePriorityToCurrentPlayer can be similarly implemented

}
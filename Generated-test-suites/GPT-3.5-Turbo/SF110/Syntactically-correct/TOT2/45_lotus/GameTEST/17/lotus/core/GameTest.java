package lotus.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test for player1 being the playing player
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test for player2 being the playing player
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test for player1 as the current player
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test for player2 as the current player
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test for player1 as input
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test for player2 as input
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    // Additional test cases can be added for other methods in Game class

}
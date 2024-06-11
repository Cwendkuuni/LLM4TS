package lotus.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Edge case
        Game.playingPlayer = 0;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Edge case
        Game.playingPlayer = 0;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Edge case
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // TODO: Add test cases for init method
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Add test cases for givePriorityToCurrentPlayer method
    }
}
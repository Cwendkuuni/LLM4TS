package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // test with player1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // test with player2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // TODO: write test for init() method
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: write test for givePriorityToCurrentPlayer() method
    }
}
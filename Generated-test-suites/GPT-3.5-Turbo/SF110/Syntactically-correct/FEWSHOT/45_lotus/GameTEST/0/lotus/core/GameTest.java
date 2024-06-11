package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test with player1 as input
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test with player2 as input
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Test initialization with all parameters
        Player player1 = new Player(null, "Player1", null);
        Player player2 = new Player(null, "Player2", null);
        Game.init(null, null, "Player1", "Player2", null, null);
        assertEquals(player1, Game.player1);
        assertEquals(player2, Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);

        // Test initialization without decks
        Game.init(null, null, "Player1", "Player2", null, null);
        assertNull(Game.player1.deck);
        assertNull(Game.player2.deck);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test with empty stack
        assertFalse(Game.stack.empty());
        Game.stack.pop();
        Game.givePriorityToCurrentPlayer();

        // Test with non-empty stack
        Game.stack.push(null);
        assertTrue(Game.stack.empty());
        Game.givePriorityToCurrentPlayer();
    }
}
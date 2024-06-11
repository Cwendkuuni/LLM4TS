package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        // Test with player1 as the playing player
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test with player2 as the playing player
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test with player1 as the current player
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test with player2 as the current player
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Test initialization

        // Create mock objects for UserInterface and CardCollection
        UserInterface mockUI1 = new UserInterface();
        UserInterface mockUI2 = new UserInterface();
        CardCollection mockDeck1 = new CardCollection();
        CardCollection mockDeck2 = new CardCollection();

        // Call the init method
        Game.init(mockUI1, mockUI2, "Player1", "Player2", mockDeck1, mockDeck2);

        // Check if the players and currentPhase are correctly initialized
        assertEquals(mockUI1, Game.player1.getUserInterface());
        assertEquals(mockUI2, Game.player2.getUserInterface());
        assertEquals("Player1", Game.player1.getName());
        assertEquals("Player2", Game.player2.getName());
        assertEquals(mockDeck1, Game.player1.getDeck());
        assertEquals(mockDeck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        assertEquals(Game.player1, Game.getPlayingPlayer());
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Implement this test case
    }
}
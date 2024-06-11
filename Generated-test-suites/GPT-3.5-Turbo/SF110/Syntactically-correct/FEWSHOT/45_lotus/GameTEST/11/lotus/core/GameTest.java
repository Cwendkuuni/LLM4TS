package lotus.core;

import org.junit.Test;
import static org.junit.Assert.*;

import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test Case 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test Case 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test Case 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test Case 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test Case 1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test Case 2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Create mock UserInterfaces and CardCollections
        UserInterface mockUI1 = new UserInterface();
        UserInterface mockUI2 = new UserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        // Perform init
        Game.init(mockUI1, mockUI2, name1, name2, deck1, deck2);

        // Test Case 1
        assertEquals(Game.player1, Game.player1);
        assertEquals(mockUI1, Game.player1.getInterface());
        assertEquals(name1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());

        // Test Case 2
        assertEquals(Game.player2, Game.player2);
        assertEquals(mockUI2, Game.player2.getInterface());
        assertEquals(name2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Create mock players
        Player mockPlayer1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Player mockPlayer2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = mockPlayer1;
        Game.player2 = mockPlayer2;

        // Set playing player
        Game.playingPlayer = 1;

        // Perform givePriorityToCurrentPlayer
        Game.givePriorityToCurrentPlayer();

        // Test Case 1
        assertTrue(Game.stack.empty());
    }
}
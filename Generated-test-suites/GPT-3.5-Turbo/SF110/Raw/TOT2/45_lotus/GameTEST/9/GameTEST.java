package lotus.core;

import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        Player player1 = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(player1));
        
        // Edge case
        Player player2 = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        
        // Edge case
        // Test with empty decks
        CardCollection emptyDeck = new CardCollection();
        Game.init(IP1, IP2, n1, n2, emptyDeck, emptyDeck);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        Game.stack = new Stack();
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        Game.givePriorityToCurrentPlayer();
        
        // Test with empty stack
        Game.stack = new Stack();
        Game.givePriorityToCurrentPlayer();
    }

}
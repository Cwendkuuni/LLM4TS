import org.junit.*;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expected1 = Game.player1;
        Player actual1 = Game.getPlayingPlayer();
        assertEquals(expected1, actual1);
        
        // Test when playingPlayer is not 1 (2)
        Game.playingPlayer = 2;
        Player expected2 = Game.player2;
        Player actual2 = Game.getPlayingPlayer();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expected1 = Game.player2;
        Player actual1 = Game.getNonPlayingPlayer();
        assertEquals(expected1, actual1);
        
        // Test when playingPlayer is not 1 (2)
        Game.playingPlayer = 2;
        Player expected2 = Game.player1;
        Player actual2 = Game.getNonPlayingPlayer();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testGetOtherPlayer() {
        // Test when given player is player1
        Player p1 = Game.player1;
        Player expected1 = Game.player2;
        Player actual1 = Game.getOtherPlayer(p1);
        assertEquals(expected1, actual1);
        
        // Test when given player is player2
        Player p2 = Game.player2;
        Player expected2 = Game.player1;
        Player actual2 = Game.getOtherPlayer(p2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testInit() {
        // Create test inputs
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "name1";
        String n2 = "name2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Perform initialization
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Test that player1 and player2 are created correctly
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        
        // Test that player1 and player2 have correct interfaces
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        
        // Test that player1 and player2 have correct decks
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Test that playingPlayer is set to 1
        int expectedPlayingPlayer = 1;
        int actualPlayingPlayer = Game.playingPlayer;
        assertEquals(expectedPlayingPlayer, actualPlayingPlayer);
        
        // Test that currentPhase is set to an instance of UntapPhase
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Test that all cards in deck1 belong to player1 and are in player1's library zone
        for (Card c : deck1) {
            assertTrue(c.owner == Game.player1);
            assertTrue(c.zone == Game.player1.library);
        }
        
        // Test that all cards in deck2 belong to player2 and are in player2's library zone
        for (Card c : deck2) {
            assertTrue(c.owner == Game.player2);
            assertTrue(c.zone == Game.player2.library);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // To be tested in integration test with resolving stack
    }
}
package lotus.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        expectedPlayer = Game.player2;
        actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player2;
        Player actualPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        expectedPlayer = Game.player1;
        actualPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test when input Player is player1
        Player inputPlayer = Game.player1;
        Player expectedPlayer = Game.player2;
        Player actualPlayer = Game.getOtherPlayer(inputPlayer);
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test when input Player is player2
        inputPlayer = Game.player2;
        expectedPlayer = Game.player1;
        actualPlayer = Game.getOtherPlayer(inputPlayer);
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testInit() {
        // Create test card collections
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Create test user interfaces
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        
        // Create test player names
        String n1 = "Player 1";
        String n2 = "Player 2";
        
        // Call the init() method
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Assert that player1 and player2 are initialized
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        
        // Assert that player1 and player2's interfaces are initialized
        assertEquals(IP1, Game.player1.userInterface);
        assertEquals(IP2, Game.player2.userInterface);
        
        // Assert that playingPlayer is set to 1
        assertEquals(1, Game.playingPlayer);
        
        // Assert that currentPhase is initialized as UntapPhase
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Assert that the owner and zone of cards in deck1 are set correctly
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        
        // Assert that the owner and zone of cards in deck2 are set correctly
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Write test cases for givePriorityToCurrentPlayer()
    }
}
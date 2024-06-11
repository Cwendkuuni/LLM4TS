import static org.junit.Assert.*;
import org.junit.*;

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
        // Test with player1 as the non-playing player
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
        
        // Test with player2 as the non-playing player
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test with player1 as the input player
        Player inputPlayer = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(inputPlayer));
        
        // Test with player2 as the input player
        inputPlayer = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(inputPlayer));
    }
    
    @Test
    public void testInit() {
        // Create dummy objects for UserInterface, CardCollection, and String
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        String n1 = "Player1";
        String n2 = "Player2";
        
        // Call init method
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Check if player1 and player2 are initialized correctly
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(IP2, Game.player2.getInterface());
        
        // Check if playingPlayer and currentPhase are set correctly
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Check if cards in deck1 are assigned to player1 correctly
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.getLibrary(), c.getZone());
        }
        
        // Check if cards in deck2 are assigned to player2 correctly
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.getLibrary(), c.getZone());
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Implement test for this method
    }
}
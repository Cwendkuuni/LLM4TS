
import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {
    
    // Test getPlayingPlayer method
    @Test
    public void testGetPlayingPlayer() {
        // Test with playingPlayer = 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test with playingPlayer = 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    // Test getNonPlayingPlayer method
    @Test
    public void testGetNonPlayingPlayer() {
        // Create mock players
        Player mockPlayer1 = new Player();
        Player mockPlayer2 = new Player();
        Game.player1 = mockPlayer1;
        Game.player2 = mockPlayer2;
        
        // Test with playingPlayer = 1
        Game.playingPlayer = 1;
        assertEquals(mockPlayer2, Game.getNonPlayingPlayer());
        
        // Test with playingPlayer = 2
        Game.playingPlayer = 2;
        assertEquals(mockPlayer1, Game.getNonPlayingPlayer());
    }
    
    // Test getOtherPlayer method
    @Test
    public void testGetOtherPlayer() {
        // Create mock players
        Player mockPlayer1 = new Player();
        Player mockPlayer2 = new Player();
        Game.player1 = mockPlayer1;
        Game.player2 = mockPlayer2;
        
        // Test with player1
        assertEquals(mockPlayer2, Game.getOtherPlayer(mockPlayer1));
        
        // Test with player2
        assertEquals(mockPlayer1, Game.getOtherPlayer(mockPlayer2));
    }
    
    // Test init method
    @Test
    public void testInit() {
        // Create mock user interfaces and decks
        UserInterface mockUI1 = new UserInterface();
        UserInterface mockUI2 = new UserInterface();
        CardCollection mockDeck1 = new CardCollection();
        CardCollection mockDeck2 = new CardCollection();
        
        // Test init method
        Game.init(mockUI1, mockUI2, "Player 1", "Player 2", mockDeck1, mockDeck2);
        
        // Check if players and decks are initialized correctly
        assertEquals(mockUI1, Game.player1.getUI());
        assertEquals(mockUI2, Game.player2.getUI());
        assertEquals("Player 1", Game.player1.getName());
        assertEquals("Player 2", Game.player2.getName());
        assertEquals(mockDeck1, Game.player1.getDeck());
        assertEquals(mockDeck2, Game.player2.getDeck());
        
        // Check if playingPlayer and currentPhase are initialized correctly
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Check if cards in decks are assigned correct owners and zones
        for (Card c : mockDeck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        for (Card c : mockDeck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }
    
    // Test givePriorityToCurrentPlayer method
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Create mock players
        Player mockPlayer1 = new Player();
        Player mockPlayer2 = new Player();
        Game.player1 = mockPlayer1;
        Game.player2 = mockPlayer2;
        
        // Create mock stack
        Stack mockStack = new Stack();
        Game.stack = mockStack;
        
        // Test givePriorityToCurrentPlayer method
        Game.givePriorityToCurrentPlayer();
        
        // Assert that the stack is empty
        assertTrue(mockStack.empty());
    }
}
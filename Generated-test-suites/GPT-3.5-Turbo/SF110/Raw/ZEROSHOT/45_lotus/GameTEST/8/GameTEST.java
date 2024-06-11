import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test when playingPlayer is not 1
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
        
        // Test when playingPlayer is not 1
        Game.playingPlayer = 2;
        expectedPlayer = Game.player1;
        actualPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test when p is player1
        Player p = Game.player1;
        Player expectedPlayer = Game.player2;
        Player actualPlayer = Game.getOtherPlayer(p);
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test when p is not player1
        p = Game.player2;
        expectedPlayer = Game.player1;
        actualPlayer = Game.getOtherPlayer(p);
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testInit() {
        // Create mock objects for UserInterface and CardCollection
        UserInterface mockUI1 = new MockUserInterface();
        UserInterface mockUI2 = new MockUserInterface();
        CardCollection mockDeck1 = new MockCardCollection();
        CardCollection mockDeck2 = new MockCardCollection();
        
        String n1 = "Player 1";
        String n2 = "Player 2";
        
        // Call the init method
        Game.init(mockUI1, mockUI2, n1, n2, mockDeck1, mockDeck2);
        
        // Check if player1 and player2 are created and initialized correctly
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(mockUI1, Game.player1.getUserInterface());
        assertEquals(mockUI2, Game.player2.getUserInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(mockDeck1, Game.player1.getDeck());
        assertEquals(mockDeck2, Game.player2.getDeck());
        
        // Check if playingPlayer is set to 1 and currentPhase is an instance of UntapPhase
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Check if all cards in deck1 have their owner set to player1 and zone set to player1.library
        for (Card c : mockDeck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        
        // Check if all cards in deck2 have their owner set to player2 and zone set to player2.library
        for (Card c : mockDeck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test when stack is empty
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        // Make sure the method does not throw any exceptions
        
        // Test when stack is not empty
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        // Make sure the method does not throw any exceptions
    }

}
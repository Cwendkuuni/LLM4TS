import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Before
    public void setUp() {
        // Initialize necessary objects for testing
    }
    
    // Test cases for getPlayingPlayer()
    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        // Set playing player to player1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        // Set playing player to player2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    // Test cases for getNonPlayingPlayer()
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        // Set playing player to player1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        // Set playing player to player2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    // Test cases for getOtherPlayer(Player p)
    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        // Set p as player1
        Player p = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(p));
        
        // Edge case
        // Set p as player2
        p = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(p));
        
        // Error scenario
        // Set p as null
        p = null;
        try {
            Game.getOtherPlayer(p);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    // Test cases for init(UserInterface IP1, UserInterface IP2, String n1, String n2, CardCollection deck1, CardCollection deck2)
    @Test
    public void testInit() {
        // Typical use case
        // Set up the necessary objects for testing
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Verify if the initialization is done correctly
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }
    
    // Test cases for givePriorityToCurrentPlayer()
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        // Set up the necessary objects for testing
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        
        // Verify if the last element in the stack is resolved
        assertTrue(Game.stack.empty());
        
        // Edge case
        // Set up the necessary objects for testing
        Game.playingPlayer = 2;
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        
        // Verify if all elements in the stack are resolved
        assertTrue(Game.stack.empty());
        
        // Error scenario
        // Set up the necessary objects for testing
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(null);
        Game.stack.push(new Card());
        
        try {
            Game.givePriorityToCurrentPlayer();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
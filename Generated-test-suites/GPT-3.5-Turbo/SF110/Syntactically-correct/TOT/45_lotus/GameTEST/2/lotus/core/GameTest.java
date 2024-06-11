import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Player expectedPlayingPlayer = Game.player1;
        Player actualPlayingPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayingPlayer, actualPlayingPlayer);
        
        // Edge case
        Game.playingPlayer = 2;
        expectedPlayingPlayer = Game.player2;
        actualPlayingPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayingPlayer, actualPlayingPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Player expectedNonPlayingPlayer = Game.player2;
        Player actualNonPlayingPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedNonPlayingPlayer, actualNonPlayingPlayer);
        
        // Edge case
        Player temp = Game.player1;
        Game.player1 = Game.player2;
        Game.player2 = temp;
        
        expectedNonPlayingPlayer = Game.player1;
        actualNonPlayingPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedNonPlayingPlayer, actualNonPlayingPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Player testPlayer = Game.player1;
        Player expectedOtherPlayer = Game.player2;
        Player actualOtherPlayer = Game.getOtherPlayer(testPlayer);
        assertEquals(expectedOtherPlayer, actualOtherPlayer);
        
        // Edge case
        testPlayer = Game.player2;
        expectedOtherPlayer = Game.player1;
        actualOtherPlayer = Game.getOtherPlayer(testPlayer);
        assertEquals(expectedOtherPlayer, actualOtherPlayer);
    }
    
    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new MockUserInterface();
        UserInterface IP2 = new MockUserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, IP1.getPlayer());
        assertEquals(Game.player2, IP2.getPlayer());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        MockUserInterface IP1 = new MockUserInterface();
        MockUserInterface IP2 = new MockUserInterface();
        Game.init(IP1, IP2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
        
        // Set up the stack for testing
        Card card1 = new Card();
        Card card2 = new Card();
        Game.stack.push(card1);
        Game.stack.push(card2);
        
        Game.givePriorityToCurrentPlayer();
        
        // Check that resolving the last card on the stack sets the next player's priority
        assertEquals(2, Game.playingPlayer);
        assertEquals(card2, Game.stack.getLastResolved());
        
        // Check that the method continues recursively until the stack is empty
        Game.stack.pop(); // Remove card2
        Game.stack.push(card1);
        
        Game.givePriorityToCurrentPlayer();
        
        assertEquals(1, Game.playingPlayer);
        assertEquals(card1, Game.stack.getLastResolved());
    }
}
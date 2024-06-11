import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Typical case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Typical case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Typical case
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
        
        // Edge case: passing null
        assertNull(Game.getOtherPlayer(null));
    }
    
    @Test
    public void testInit() {
        // Typical case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Edge case: passing null values
        Game.init(null, null, "", "", null, null);
        assertNull(Game.player1.getUserInterface());
        assertNull(Game.player2.getUserInterface());
        assertEquals("", Game.player1.getName());
        assertEquals("", Game.player2.getName());
        assertNull(Game.player1.getDeck());
        assertNull(Game.player2.getDeck());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical case: Stack is empty
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Typical case: Stack has one element
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Error scenario: Stack has several elements
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Edge case: Stack is null
        Game.stack = null;
        Game.givePriorityToCurrentPlayer();
        assertNull(Game.stack);
    }
}
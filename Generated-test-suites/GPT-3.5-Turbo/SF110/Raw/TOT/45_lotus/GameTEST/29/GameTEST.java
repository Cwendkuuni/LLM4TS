import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Game.player1 = new Player(null, "John", null);
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case: playingPlayer = 2
        Game.playingPlayer = 2;
        Game.player2 = new Player(null, "Jane", null);
        assertEquals(Game.player2, Game.getPlayingPlayer());
        
        // Error scenario: playingPlayer = 0
        Game.playingPlayer = 0;
        assertNull(Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(null, "John", null);
        Game.player2 = new Player(null, "Jane", null);
        
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case: playingPlayer = 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
        
        // Error scenario: playingPlayer = 0
        Game.playingPlayer = 0;
        assertNull(Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(null, "John", null);
        Game.player2 = new Player(null, "Jane", null);
        
        // Typical use case: p is player1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Typical use case: p is player2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
        
        // Error scenario: p is null
        assertNull(Game.getOtherPlayer(null)); 
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = null;
        UserInterface IP2 = null;
        String n1 = "John";
        String n2 = "Jane";
        CardCollection deck1 = null;
        CardCollection deck2 = null;
        
        // Typical use case
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.player1.IP);
        assertNotNull(Game.player2.IP);
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        
        // Error scenario: IP1 and IP2 are null
        Game.init(null, null, n1, n2, deck1, deck2);
        assertNull(Game.player1);
        assertNull(Game.player2);
        
        // Error scenario: n1 or n2 is empty
        Game.init(IP1, IP2, "", n2, deck1, deck2);
        assertNull(Game.player1);
        assertNull(Game.player2);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Edge case: stack is empty
        Game.stack.clear();
        Game.playingPlayer = 1;
        Game.player1 = new Player(null, "John", null);
        assertFalse(Game.stack.empty());
        
        // Typical use case
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Error scenario: stack has unresolved actions
        Game.stack.clear();
        Game.stack.push(new Action());
        Game.givePriorityToCurrentPlayer();
        assertFalse(Game.stack.empty());
    }
}
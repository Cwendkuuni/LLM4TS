import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testInit() {
        // Typical use case
        UserInterface ip1 = new UserInterface();
        UserInterface ip2 = new UserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(ip1, ip2, name1, name2, deck1, deck2);
        
        assertEquals(name1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(name2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Error scenario - Empty stack
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        assertEquals(0, Game.stack.size());
        
        // Edge case - Recursive call
        Game.stack.add(new Card("Test Card", Game.player1));
        Game.givePriorityToCurrentPlayer();
        assertEquals(0, Game.stack.size());
    }
}
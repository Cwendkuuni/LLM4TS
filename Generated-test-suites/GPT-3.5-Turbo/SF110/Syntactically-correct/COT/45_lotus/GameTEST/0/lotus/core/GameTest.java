import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test case for Game.playingPlayer = 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test case for Game.playingPlayer = 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test case for Game.playingPlayer = 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test case for Game.playingPlayer = 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test case for input Player p = Game.player1
        Player p = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(p));
        
        // Test case for input Player p = Game.player2
        p = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(p));
    }
    
    @Test
    public void testInit() {
        // Test case for initializing Game
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Add assertions to check if the Game initialization is done correctly
        assertEquals(Game.player1, IP1.player);
        assertEquals(Game.player2, IP2.player);
        assertEquals(1, Game.playingPlayer);
        assertEquals(new UntapPhase(), Game.currentPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test case for an empty stack
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        
        // Add assertions to check if the Game.stack is empty
        
        // Test case for a non-empty stack
        // Create a mock stack and add it to Game.stack
        
        // Test case for resolving the last item in the stack
        
    }
}
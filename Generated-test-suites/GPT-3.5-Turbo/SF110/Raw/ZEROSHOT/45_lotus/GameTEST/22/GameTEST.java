import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.Game;

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
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test with player1 as input
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Test with player2 as input
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Initialization code goes here
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test when stack is empty
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        // Test any assertions or verifications here
        
        // Test when stack is not empty
        // Add test data to stack
        Game.stack.push(new SomeStackObject());
        Game.givePriorityToCurrentPlayer();
        // Test any assertions or verifications here
    }
}
package lotus.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Test initialization of players and decks
        // Add test cases for different scenarios
        
        // Test phase initialization
        // Add test cases for different scenarios
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test priority assignment to players
        // Add test cases for different scenarios
    }
    
    // Add more test cases for edge cases and error scenarios
    
}
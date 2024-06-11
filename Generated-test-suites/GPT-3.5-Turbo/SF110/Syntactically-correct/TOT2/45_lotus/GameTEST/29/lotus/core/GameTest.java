package lotus.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical Case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical Case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical Case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Edge Case: Empty Player Interface
        // Exception Handling: Null Pointer
        assertThrows(NullPointerException.class, () -> {
            Game.init(null, null, "", "", new CardCollection(), new CardCollection());
        });
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical Case
        // Edge Case: Empty Stack
        Game.givePriorityToCurrentPlayer();
    }
}
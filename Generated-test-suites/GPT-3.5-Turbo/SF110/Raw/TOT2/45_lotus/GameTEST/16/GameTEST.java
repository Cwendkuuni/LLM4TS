import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // typical use case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // edge case
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // typical use case
        UserInterface ui1 = new UserInterface();
        UserInterface ui2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(ui1, ui2, "Player1", "Player2", deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertTrue(Game.playingPlayer == 1);
        assertNotNull(Game.currentPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // typical use case
        // unable to test fully without mock objects
    }
}
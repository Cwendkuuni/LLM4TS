import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // typical use case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
    }
    
    @Test
    public void testInit() {
        // typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(Game.player1, Game.getPlayingPlayer());
        assertNotNull(Game.stack);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // typical use case
        Game.givePriorityToCurrentPlayer();
        // additional test cases can be added for edge cases and error scenarios
    }
}
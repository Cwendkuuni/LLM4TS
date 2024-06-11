
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Edge case
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.stack);
        assertNotNull(Game.currentPhase);
        
        // Edge case - null parameters
        try {
            Game.init(null, null, null, null, null, null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.stack = new Stack();
        
        // Assume letPlayerSpeak() returns true in Player class
        assertTrue(Game.stack.empty());
        Game.givePriorityToCurrentPlayer();
        
        // Edge case - stack not empty
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
}

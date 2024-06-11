import org.junit.Test;
import static org.junit.Assert.*;

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
        Game.player1 = new Player();
        Game.player2 = new Player();
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
        Player testPlayer = new Player();
        assertEquals(Game.player1, Game.getOtherPlayer(testPlayer));
    }

    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, Game.getPlayingPlayer());
        assertNotNull(Game.player1.library);
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        assertNotNull(Game.player2.library);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        Game.stack.push(new Card());
        // Add implementation for player speaking and stack resolving
        
        // Edge case
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        // Add implementation for handling empty stack
    }
}
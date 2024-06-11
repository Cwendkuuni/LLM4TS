import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Basic functionality test
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Basic functionality test
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Basic functionality test
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
    }
    
    @Test
    public void testInit() {
        // Basic functionality test
        UserInterface IP1 = new UserInterfaceImpl();
        UserInterface IP2 = new UserInterfaceImpl();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(Game.player1, IP1.getPlayer());
        assertEquals(Game.player2, IP2.getPlayer());
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Edge case: Player 1's priority
        Game.playingPlayer = 1;
        Game.stack = new Stack();
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        // TODO: Add assertion for expected behavior
        
        // Edge case: Player 2's priority
        Game.playingPlayer = 2;
        Game.stack = new Stack();
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        // TODO: Add assertion for expected behavior
        
        // Edge case: Stack is not empty
        Game.playingPlayer = 1;
        Game.stack = new Stack();
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        // TODO: Add assertion for expected behavior
    }
}
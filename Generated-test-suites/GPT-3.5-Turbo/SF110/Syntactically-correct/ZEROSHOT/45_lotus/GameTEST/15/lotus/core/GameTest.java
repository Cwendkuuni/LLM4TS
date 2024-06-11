import static org.junit.Assert.*;
import org.junit.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playing player is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test when playing player is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playing player is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test when playing player is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test when input player is player 1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Test when input player is player 2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Test initialization
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test priority when stack is empty
        Game.stack.clear();
        Game.playingPlayer = 1;
        Game.givePriorityToCurrentPlayer();
        assertEquals(1, Game.playingPlayer);
        
        // Test priority resolution when stack is not empty
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.stack.empty());
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        // Test when p is player1
        Player p = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(p));
        
        // Test when p is player2
        p = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(p));
    }
    
    @Test
    public void testInit() {
        // TODO: Implement unit test for init() method
        
        // example:
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "John";
        String n2 = "Jane";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(IP1, Game.player1.getUI());
        assertEquals(IP2, Game.player2.getUI());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        
        // Additional assertions for other attributes
        
        assertNotNull(Game.currentPhase);
        
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Implement unit test for givePriorityToCurrentPlayer() method
    }
}
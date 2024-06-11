
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test if the playing player is player1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test if the playing player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test if the non-playing player is player2 (when playing player is player1)
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test if the non-playing player is player1 (when playing player is player2)
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test if p is player1, then the other player is player2
        Player p = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(p));
        
        // Test if p is player2, then the other player is player1
        p = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(p));
    }
    
    @Test
    public void testInit() {
        // Test if the players and their decks are initialized correctly
        
        // Set up input interfaces and deck collections
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Check if players are initialized correctly
        assertEquals(IP1, Game.player1.getInputInterface());
        assertEquals(IP2, Game.player2.getInputInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        
        // Check if decks are initialized correctly
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
        
        // Check if playing player and current phase are set correctly
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Write test cases for the givePriorityToCurrentPlayer() method
    }
}

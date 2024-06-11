import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    
    @Before
    public void setUp() {
        // Set up any necessary objects or variables before running tests
    }
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Player expected = Game.player1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
        
        // Edge case
        Game.playingPlayer = 2;
        expected = Game.player2;
        actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Player expected = Game.player2;
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
        
        // Edge case
        Game.playingPlayer = 2;
        expected = Game.player1;
        actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Player p = Game.player1;
        Player expected = Game.player2;
        Player actual = Game.getOtherPlayer(p);
        assertEquals(expected, actual);
        
        p = Game.player2;
        expected = Game.player1;
        actual = Game.getOtherPlayer(p);
        assertEquals(expected, actual);
        
        // Error scenario
        p = null;
        expected = null;
        actual = Game.getOtherPlayer(p);
        assertEquals(expected, actual);
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
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());
        
        assertEquals(IP2, Game.player2.getInterface());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());
        
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
        // Typical use case
        Game.playingPlayer = 1;
        Game.stack.push(new Card());
        
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Edge case
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Error scenario
        Game.stack.push(new Card());
        Game.stack.push(null);
        
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
}
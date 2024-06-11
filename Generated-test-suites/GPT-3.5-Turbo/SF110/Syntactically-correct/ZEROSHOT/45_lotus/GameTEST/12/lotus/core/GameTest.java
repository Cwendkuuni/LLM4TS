import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    private UserInterface IP1;
    private UserInterface IP2;
    private String n1;
    private String n2;
    private CardCollection deck1;
    private CardCollection deck2;
    
    @Before
    public void setUp() {
        // Initialize variables for testing
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        n1 = "Player1";
        n2 = "Player2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }
    
    @Test
    public void testGetPlayingPlayer() {
        // Test case 1
        Game.playingPlayer = 1;
        Game.player1 = new Player(IP1, "Player1", deck1);
        Player result1 = Game.getPlayingPlayer();
        assertEquals(Game.player1, result1);
        
        // Test case 2
        Game.playingPlayer = 2;
        Game.player2 = new Player(IP2, "Player2", deck2);
        Player result2 = Game.getPlayingPlayer();
        assertEquals(Game.player2, result2);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test case 1
        Game.player1 = new Player(IP1, "Player1", deck1);
        Game.player2 = new Player(IP2, "Player2", deck2);
        Player p = Game.player1;
        Player result1 = Game.getNonPlayingPlayer(p);
        assertEquals(Game.player2, result1);
        
        // Test case 2
        p = Game.player2;
        Player result2 = Game.getNonPlayingPlayer(p);
        assertEquals(Game.player1, result2);
    }
    
    @Test
    public void testInit() {
        // Test case 1
        Game.player1 = null;
        Game.player2 = null;
        assertNull(Game.player1);
        assertNull(Game.player2);
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Test case 2
        Game.player1 = null;
        Game.player2 = null;
        assertNull(Game.player1);
        assertNull(Game.player2);
        
        Game.init(IP1, IP2, "Player1", "Player2", new CardCollection(), new CardCollection());
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test case 1
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Test case 2
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
}
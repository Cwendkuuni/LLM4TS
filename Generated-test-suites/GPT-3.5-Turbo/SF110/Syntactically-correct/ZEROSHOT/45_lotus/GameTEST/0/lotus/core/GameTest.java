import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {
    
    @Before
    public void setUp() {
        // Set up initial values for testing
        UserInterface ui1 = new UserInterface();
        UserInterface ui2 = new UserInterface();
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ui1, ui2, name1, name2, deck1, deck2);
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Player expectedPlayingPlayer = Game.player1;
        assertEquals(expectedPlayingPlayer, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player expectedNonPlayingPlayer = Game.player2;
        assertEquals(expectedNonPlayingPlayer, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player = Game.player1;
        Player expectedOtherPlayer = Game.player2;
        assertEquals(expectedOtherPlayer, Game.getOtherPlayer(player));
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Add test cases for the givePriorityToCurrentPlayer method
        // ...
    }
    
    @After
    public void tearDown() {
        // Clean up after testing
        Game.player1 = null;
        Game.player2 = null;
        Game.currentPhase = null;
    }
}
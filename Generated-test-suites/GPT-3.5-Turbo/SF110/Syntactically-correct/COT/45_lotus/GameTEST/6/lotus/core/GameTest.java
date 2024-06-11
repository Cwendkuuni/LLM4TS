import org.junit.Before;
import org.junit.Test;

public class GameTest {
    
    private UserInterface IP1;
    private UserInterface IP2;
    private String n1;
    private String n2;
    private CardCollection deck1;
    private CardCollection deck2;
    
    @Before
    public void setUp() {
        // Initialize necessary variables for testing
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        n1 = "Player 1";
        n2 = "Player 2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        // Add cards to deck1 and deck2
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Player player = Game.getPlayingPlayer();
        // Assertions for testing the functionality of Game.getPlayingPlayer()
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player player = Game.getNonPlayingPlayer();
        // Assertions for testing the functionality of Game.getNonPlayingPlayer()
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player = new Player();
        Player otherPlayer = Game.getOtherPlayer(player);
        // Assertions for testing the functionality of Game.getOtherPlayer()
    }
    
    @Test
    public void testInit() {
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        // Assertions for testing the functionality of Game.init()
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Set up necessary conditions before calling givePriorityToCurrentPlayer()
        Game.givePriorityToCurrentPlayer();
        // Assertions for testing the functionality of Game.givePriorityToCurrentPlayer()
    }
}
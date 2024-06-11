import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    @Before
    public void setup() {
        // Initialize necessary objects for testing
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Add test data to deck1 and deck2
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Player playingPlayer = Game.getPlayingPlayer();
        
        // Test logic here
        
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        
        // Test logic here
        
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player = new Player();
        Player otherPlayer = Game.getOtherPlayer(player);
        
        // Test logic here
        
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test logic here
        
    }

}
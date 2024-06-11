import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        Player playingPlayer = Game.getPlayingPlayer();
        assertNotNull(playingPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        assertNotNull(nonPlayingPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        
        Player otherPlayer = Game.getOtherPlayer(player1);
        assertEquals(player2, otherPlayer);
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.currentPhase);
        assertNotNull(Game.stack);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.givePriorityToCurrentPlayer();
        // No assertion as this method has side effects
    }
    
}
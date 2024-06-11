import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        Game.givePriorityToCurrentPlayer();
        
        // add more specific test cases here
    }
}
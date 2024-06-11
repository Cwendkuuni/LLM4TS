import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player playingPlayer = Game.getPlayingPlayer();
        Player expectedPlayer = Game.player1;
        assertEquals(expectedPlayer, playingPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        Player expectedPlayer = Game.player2;
        assertEquals(expectedPlayer, nonPlayingPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player currentPlayer = Game.player1;
        Player otherPlayer = Game.getOtherPlayer(currentPlayer);
        Player expectedPlayer = Game.player2;
        assertEquals(expectedPlayer, otherPlayer);
    }
    
    @Test
    public void testInit() {
        // Test initialization with valid inputs
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Assert initialized players
        assertEquals(n1, Game.player1.getName());
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(deck1, Game.player1.getDeck());
        
        assertEquals(n2, Game.player2.getName());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Assert current phase
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Create test case for givePriorityToCurrentPlayer method
        // Test handling of priority with different game scenarios
    }
}
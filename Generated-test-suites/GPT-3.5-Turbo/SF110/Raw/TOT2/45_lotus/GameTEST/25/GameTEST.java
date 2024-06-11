import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
        Game.player1 = player1;
        Game.playingPlayer = 1;
        
        assertEquals(player1, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
        Player player2 = new Player(mock(UserInterface.class), "Player2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        
        assertEquals(player2, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
        Player player2 = new Player(mock(UserInterface.class), "Player2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        
        assertEquals(player2, Game.getOtherPlayer(player1));
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        
        assertEquals("Player1", Game.player1.getName());
        assertEquals("Player2", Game.player2.getName());
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(IP2, Game.player2.getInterface());
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test for the method givePriorityToCurrentPlayer
        // Need to mock Player objects and test the method's functionality
    }
}
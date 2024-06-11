import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test getNonPlayingPlayer for player1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test getNonPlayingPlayer for player2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test getOtherPlayer for player1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Test getOtherPlayer for player2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Test initialization of players, decks, and current phase
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(IP1, Game.player1.getUI());
        assertEquals(IP2, Game.player2.getUI());
        assertEquals(Game.currentPhase.getClass(), UntapPhase.class);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test giving priority when both players can speak
        Game.playingPlayer = 1;
        Game.givePriorityToCurrentPlayer();
        // Add more test cases for different scenarios related to giving priority
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when Game.playingPlayer == 1
        // Create test objects
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Initialize Game
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Test the method
        Player expected = Game.player1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
        
        // Test when Game.playingPlayer != 1
        // Change Game.playingPlayer
        Game.playingPlayer = 2;
        
        // Test the method
        expected = Game.player2;
        actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Create test objects
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Initialize Game
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Test the method
        Player expected = Game.player2;
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Create test objects
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Initialize Game
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Test when given Game.player1 as argument
        // Test the method
        Player expected = Game.player2;
        Player actual = Game.getOtherPlayer(Game.player1);
        assertEquals(expected, actual);
        
        // Test when given Game.player2 as argument
        // Test the method
        expected = Game.player1;
        actual = Game.getOtherPlayer(Game.player2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInit() {
        // Create test objects
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Test the method
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Assert that Game.player1 and Game.player2 are correctly initialized
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Assert that IP1 and IP2 are correctly initialized
        assertEquals(Game.player1, IP1.getInitPlayer());
        assertEquals(Game.player2, IP2.getInitPlayer());
        
        // Assert that Game.playingPlayer is correctly initialized
        assertEquals(1, Game.playingPlayer);
        
        // Assert that Game.currentPhase is correctly initialized
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Assert that the cards in deck1 and deck2 are correctly initialized
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Implement test for givePriorityToCurrentPlayer() method
    }
}
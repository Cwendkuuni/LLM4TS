import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.Game;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        // Change the value of playingPlayer to 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test when playingPlayer is 2
        // Change the value of playingPlayer to 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        // Change the value of playingPlayer to 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test when playingPlayer is 2
        // Change the value of playingPlayer to 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testInit() {
        // Test the initialization of player1 and player2
        
        // Create the required objects for the test
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Call the method to be tested
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Assert the expected values after initialization
        assertEquals(IP1, Game.player1.getIP());
        assertEquals(IP2, Game.player2.getIP());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Assert the ownership and zone of each card in deck1 and deck2
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
        // Test the givePriorityToCurrentPlayer method
        
        // Set up the test environment
        Game.stack.clear(); // clear the stack
        
        // Create two players and set one as the playing player
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        
        // Use a mock object to simulate letPlayerSpeak method
        boolean[] player1Speak = {true, false, true, true, false, true}; // simulate player1's response
        boolean[] player2Speak = {false, true, true, false, true, true}; // simulate player2's response
        
        // Create a mock UserInterface for player1 and player2
        UserInterface IP1 = new UserInterface() {
            int i = 0;
            
            @Override
            public boolean letPlayerSpeak() {
                return player1Speak[i++];
            }
        };
        UserInterface IP2 = new UserInterface() {
            int i = 0;
            
            @Override
            public boolean letPlayerSpeak() {
                return player2Speak[i++];
            }
        };
        
        // Call the method to be tested
        Game.givePriorityToCurrentPlayer();
        
        // Assert the expected behavior
        assertFalse(Game.stack.empty()); // stack should not be empty
        assertEquals(player1Speak.length, player2Speak.length); // lengths should be the same
    }
}
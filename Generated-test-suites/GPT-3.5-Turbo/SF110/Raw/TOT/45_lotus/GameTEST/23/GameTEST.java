import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    
    @Before
    public void setUp() {
        // Set up test environment
    }
    
    @Test
    public void testGetPlayingPlayer() {
        // Test case for when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test case for when playingPlayer is 2
        Game.playingPlayer = 2;
        expectedPlayer = Game.player2;
        actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test case for when the playingPlayer is player1
        Game.playingPlayer = 1;
        Player expectedPlayer = Game.player2;
        Player actualPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test case for when the playingPlayer is player2
        Game.playingPlayer = 2;
        expectedPlayer = Game.player1;
        actualPlayer = Game.getNonPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test case for when the input player is player1
        Player inputPlayer = Game.player1;
        Player expectedPlayer = Game.player2;
        Player actualPlayer = Game.getOtherPlayer(inputPlayer);
        assertEquals(expectedPlayer, actualPlayer);
        
        // Test case for when the input player is player2
        inputPlayer = Game.player2;
        expectedPlayer = Game.player1;
        actualPlayer = Game.getOtherPlayer(inputPlayer);
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    public void testInit() {
        // Test typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Player expectedPlayer1 = new Player(IP1, n1, deck1);
        Player actualPlayer1 = Game.player1;
        assertEquals(expectedPlayer1, actualPlayer1);
        Player expectedPlayer2 = new Player(IP2, n2, deck2);
        Player actualPlayer2 = Game.player2;
        assertEquals(expectedPlayer2, actualPlayer2);
        
        // Test error scenarios
        // Test case for null input interfaces
        IP1 = null;
        IP2 = new UserInterface();
        String expectedExceptionMessage = "Initialization failed: UserInterface is null.";
        try {
            Game.init(IP1, IP2, n1, n2, deck1, deck2);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
        
        // Test case for null input player names
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        n1 = null;
        n2 = "Player 2";
        expectedExceptionMessage = "Initialization failed: Player name is null.";
        try {
            Game.init(IP1, IP2, n1, n2, deck1, deck2);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
        
        // Test case for null input card collections
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        n1 = "Player 1";
        n2 = "Player 2";
        deck1 = null;
        deck2 = new CardCollection();
        expectedExceptionMessage = "Initialization failed: Card collection is null.";
        try {
            Game.init(IP1, IP2, n1, n2, deck1, deck2);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test typical use case
        // TODO: Implement test case
        
        // Test edge case
        // TODO: Implement test case
        
        // Test error scenarios
        // TODO: Implement test case(s)
    }
}
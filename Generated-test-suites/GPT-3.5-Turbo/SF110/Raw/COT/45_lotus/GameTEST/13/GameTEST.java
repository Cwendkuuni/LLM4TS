import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test case for Game.getPlayingPlayer()
        // Implementation of the test case depends on the specific Player class implementation
        // Verify that the correct player is returned based on the playingPlayer variable
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test case for Game.getNonPlayingPlayer()
        // Implementation of the test case depends on the specific Player class implementation
        // Verify that the correct player is returned based on the playingPlayer variable
    }

    @Test
    public void testGetOtherPlayer() {
        // Test case for Game.getOtherPlayer(Player p)
        // Implementation of the test case depends on the specific Player class implementation
        // Verify that the correct player is returned based on the provided Player object
    }

    @Test
    public void testInit() {
        // Test case for Game.init(UserInterface IP1, UserInterface IP2, String n1, String n2, CardCollection deck1, CardCollection deck2)
        // Implementation of the test case depends on the specific UserInterface and CardCollection class implementations
        // Verify that the Game object is properly initialized with the provided inputs, including player, deck, and currentPhase
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test case for Game.givePriorityToCurrentPlayer()
        // Implementation of the test case depends on the specific Player class implementation
        // Verify that the priority is correctly given to the current player and resolved stack actions if any
    }
    
    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testInitWithNullValues() {
        // Test case for Game.init(UserInterface IP1, UserInterface IP2, String n1, String n2, CardCollection deck1, CardCollection deck2) with null values
        // Verify that proper exception is thrown when any of the inputs is null
    }

    @Test
    public void testGivePriorityWithEmptyStack() {
        // Test case for Game.givePriorityToCurrentPlayer() with empty stack
        // Verify that no actions are resolved and the method returns without any issues
    }
}


public class GameTest {
    // JUnit test cases
}

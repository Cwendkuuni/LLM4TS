import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // TO DO: Implement test case for doPhase method
    }
    
    @Test
    public void testNextPhase() {
        // TO DO: Implement test case for nextPhase method
    }
    
    @Test
    public void testChangePhase() {
        // TO DO: Implement test case for changePhase method
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testChangePhase_NullInput() {
        Phase p = null;
        try {
            Phase.changePhase(p);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // Exception caught
        }
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;
        Phase.nextPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

}
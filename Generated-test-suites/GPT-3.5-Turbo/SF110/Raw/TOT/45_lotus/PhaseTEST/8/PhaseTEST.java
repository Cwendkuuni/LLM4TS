import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Test for doPhase() method
        // Typical use case
        // Test that the doPhase() method is executed correctly
        // by asserting that the current phase is changed to the desired phase
        
        // Creating a new instance of a class that extends Phase
        Phase phase = new UntapPhase();
        // Changing the current phase
        phase.doPhase();
        // Asserting that the current phase is changed to the desired phase
        assertEquals(Game.currentPhase, phase);
    }

    @Test
    public void testNextPhase() {
        // Test for nextPhase() method
        // Typical use case
        // Test that the nextPhase() method correctly changes the current phase
        // based on the current phase's instance
        
        // Setting the current phase to UntapPhase
        Game.currentPhase = new UntapPhase();
        // Calling nextPhase() to change the phase to UpkeepPhase
        Phase.nextPhase();
        // Asserting that the current phase is changed to UpkeepPhase
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }

    @Test
    public void testNextPhase_TerminalPhase() {
        // Test for nextPhase() method
        // Edge case
        // Test that the nextPhase() method correctly changes the current phase
        // from the last phase to the first phase
        
        // Setting the current phase to PlayerChangePhase
        Game.currentPhase = new PlayerChangePhase();
        // Calling nextPhase() to change the phase to UntapPhase
        Phase.nextPhase();
        // Asserting that the current phase is changed to UntapPhase
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

    @Test
    public void testNextPhase_InvalidPhase() {
        // Test for nextPhase() method
        // Error scenario
        // Test that the nextPhase() method does not change the current phase
        // when an invalid phase is encountered
        
        // Setting the current phase to an instance of an invalid phase
        Game.currentPhase = new InvalidPhase();
        // Calling nextPhase() to change the phase
        Phase.nextPhase();
        // Asserting that the current phase remains unchanged
        assertTrue(Game.currentPhase instanceof InvalidPhase);
    }

}
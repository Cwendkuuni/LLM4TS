import org.junit.*;
import static org.junit.Assert.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // TODO: Write test for doPhase() method
    }
    
    @Test
    public void testNextPhase() {
        // TODO: Write test for nextPhase() method
    }
    
    @Test
    public void testChangePhase() {
        // TODO: Write test for changePhase() method
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUntapPhase_ShouldChangePhaseToUpkeepPhase() {
        // TODO: Write test for nextPhase() when current phase is UntapPhase
        // Edge case: Game.currentPhase is an instance of UntapPhase
        // Expected result: The phase should be changed to UpkeepPhase
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUpkeepPhase_ShouldChangePhaseToDrawPhase() {
        // TODO: Write test for nextPhase() when current phase is UpkeepPhase
        // Edge case: Game.currentPhase is an instance of UpkeepPhase
        // Expected result: The phase should be changed to DrawPhase
    }
    
    // TODO: Write tests for all the other possible cases
    
}
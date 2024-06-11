import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // TODO: Write test case for doPhase method
    }

    @Test
    public void testNextPhase() {
        // TODO: Write test case for nextPhase method
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testNextPhase_WhenCurrentPhaseIsUntapPhase_ShouldChangePhaseToUpkeepPhase() {
        // TODO: Write test case for nextPhase method when current phase is UntapPhase
        // Expected: Game.currentPhase should be an instance of UpkeepPhase
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsUpkeepPhase_ShouldChangePhaseToDrawPhase() {
        // TODO: Write test case for nextPhase method when current phase is UpkeepPhase
        // Expected: Game.currentPhase should be an instance of DrawPhase
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsDrawPhase_ShouldChangePhaseToMain1Phase() {
        // TODO: Write test case for nextPhase method when current phase is DrawPhase
        // Expected: Game.currentPhase should be an instance of Main1Phase
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain1Phase_ShouldChangePhaseToCombatBeginningPhase() {
        // TODO: Write test case for nextPhase method when current phase is Main1Phase
        // Expected: Game.currentPhase should be an instance of CombatBeginningPhase
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatBeginningPhase_ShouldChangePhaseToDeclareAttackersPhase() {
        // TODO: Write test case for nextPhase method when current phase is CombatBeginningPhase
        // Expected: Game.currentPhase should be an instance of DeclareAttackersPhase
    }
    
    // Add more test cases for the remaining phases

}
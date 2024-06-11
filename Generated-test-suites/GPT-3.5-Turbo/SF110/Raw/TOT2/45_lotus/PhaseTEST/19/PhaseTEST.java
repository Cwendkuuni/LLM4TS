import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // To be implemented
    }

    @Test
    public void testNextPhase() {
        // To be implemented
    }

    @Test
    public void testChangePhase() {
        // To be implemented
    }

    @Test
    public void testNextPhaseEdgeCases() {
        // Test when current phase is UntapPhase
        // Test when current phase is UpkeepPhase
        // Test when current phase is DrawPhase
        // Test when current phase is Main1Phase
        // Test when current phase is CombatBeginningPhase
        // Test when current phase is DeclareAttackersPhase
        // Test when current phase is DeclareBlockersPhase
        // Test when current phase is CombatDamagePhase
        // Test when current phase is CombatEndPhase
        // Test when current phase is Main2Phase
    }

    @Test(expected = NullPointerException.class)
    public void testChangePhaseException() {
        Phase.changePhase(null);
    }
}
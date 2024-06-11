import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // Test case for doPhase(). Add your test case here.
        // Typical use case
        Phase phase = new UntapPhase();
        phase.doPhase();
        
        // Edge case
        Phase phase1 = new Main2Phase();
        phase1.doPhase();
        
        // Error scenario
        Phase phase2 = new EndOfTurnPhase();
        phase2.doPhase();
    }
    
    @Test
    public void testNextPhase() {
        // Test case for nextPhase(). Add your test case here.
        // Typical use case
        Phase.nextPhase();
        
        // Edge case
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        
        // Error scenario
        Game.currentPhase = null;
        Phase.nextPhase();
    }
}
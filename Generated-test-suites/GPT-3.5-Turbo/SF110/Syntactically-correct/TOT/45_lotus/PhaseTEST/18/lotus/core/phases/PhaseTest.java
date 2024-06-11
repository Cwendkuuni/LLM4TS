import org.junit.Test;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        Phase phase = new UntapPhase();
        // Test typical use case
        phase.doPhase();
        
        phase = new UpkeepPhase();
        // Test typical use case
        phase.doPhase();
        
        // Test edge case
        phase = new DrawPhase();
        phase.doPhase();
        
        // Test error scenario
        phase = null;
        phase.doPhase();
    }
    
    @Test
    public void testNextPhase() {
        // Test typical use case
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        
        // Test all possible phases
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        
        // Test error scenario
        Game.currentPhase = null;
        Phase.nextPhase();
    }
}
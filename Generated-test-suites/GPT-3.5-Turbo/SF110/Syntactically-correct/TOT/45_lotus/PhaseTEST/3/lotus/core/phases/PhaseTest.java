import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // Test that each concrete phase class performs its intended action
        Phase phase = new UntapPhase();
        phase.doPhase();
        // Assert that the untap phase has been completed
        
        phase = new UpkeepPhase();
        phase.doPhase();
        // Assert that the upkeep phase has been completed
        
        // Repeat the above steps for all other concrete phase classes
        // ...
        
    }
    
    @Test
    public void testNextPhase() {
        // Test that each phase transition is correct
        
        // Test case 1: Current phase is UntapPhase, expect next phase to be UpkeepPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
        
        // Test case 2: Current phase is UpkeepPhase, expect next phase to be DrawPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());
        
        // Repeat the above steps for all other phase transitions
        // ...
        
    }
}
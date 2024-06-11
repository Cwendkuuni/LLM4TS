import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // Test for a typical use case
        Phase phase = new UpkeepPhase();
        phase.doPhase();
        // Assert that the current phase has successfully changed
        assertEquals(phase, Game.currentPhase);
    }
    
    
    @Test
    public void testChangePhase() {
        // Test for a typical use case
        Phase currentPhase = Game.currentPhase;
        
        // Change to a different phase
        Phase newPhase = new DrawPhase();
        Phase.changePhase(newPhase);
        
        // Assert that the Game.currentPhase has changed to the new phase
        assertEquals(newPhase, Game.currentPhase);
        
        // Test for an error scenario
        // Try to change to null phase
        Phase.changePhase(null);
        
        // Assert that the Game.currentPhase remains the same
        assertEquals(newPhase, Game.currentPhase);
    }
    
    
    @Test
    public void testNextPhase() {
        // Test for typical use cases
        // Start from UntapPhase and assert that the next phase is UpkeepPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(new UpkeepPhase(), Game.currentPhase);
        
        // Start from UpkeepPhase and assert that the next phase is DrawPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertEquals(new DrawPhase(), Game.currentPhase);
        
        // Start from DrawPhase and assert that the next phase is Main1Phase
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertEquals(new Main1Phase(), Game.currentPhase);
        
        // Add more similar tests for all phases
        
        // Test for an edge case
        // Start from CleanupPhase and assert that the next phase is PlayerChangePhase
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertEquals(new PlayerChangePhase(), Game.currentPhase);
    }
}
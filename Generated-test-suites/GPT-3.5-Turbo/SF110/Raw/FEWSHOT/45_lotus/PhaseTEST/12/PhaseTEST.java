package lotus.core.phases;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // TODO: Implement test cases for doPhase()
    }
    
    @Test
    public void testChangePhase() {
        // TODO: Implement test cases for changePhase()
    }
    
    @Test
    public void testNextPhase() {
        // Test for changing to UpkeepPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Test for changing to DrawPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Test for changing to Main1Phase
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        // Test for changing to CombatBeginningPhase
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        // TODO: Implement more test cases for nextPhase()
    }
}
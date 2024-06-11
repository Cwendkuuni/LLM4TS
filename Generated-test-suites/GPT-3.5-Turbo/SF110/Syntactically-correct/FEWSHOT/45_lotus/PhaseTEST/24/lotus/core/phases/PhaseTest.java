package lotus.core.phases;

import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
    @Test
    public void testDoPhase() {
        // create a concrete implementation of Phase for testing
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // provide implementation for doPhase method in the test
            }
        };
        
        // add asserts to verify the behavior of doPhase method
        
    }
    
    @Test
    public void testNextPhase_UntapPhase() {
        // initialize the currentPhase to UntapPhase for testing
        Game.currentPhase = new UntapPhase();
        
        // call the nextPhase method
        Phase.nextPhase();
        
        // add asserts to verify the behavior of nextPhase method when currentPhase is UntapPhase
        
    }
    
    // write similar test methods for all other phases
    
}
package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PhaseTest {
    Phase phase;
    
    @Before
    public void setUp() {
        phase = new Phase() {
            @Override
            public void doPhase() {
                // do nothing
            }
        };
    }
    
    @Test
    public void testDoPhase() {
        // test doPhase method
        phase.doPhase();
        
        // assert something
    }
    
    @Test
    public void testChangePhase() {
        Phase p = new Phase() {
            @Override
            public void doPhase() {
                // do nothing
            }
        };
        
        // test if current phase is changed
        Phase.changePhase(p);
        
        // assert something
    }
    
    @Test
    public void testNextPhase() {
        // test if next phase is set correctly
        Phase.nextPhase();
        
        // assert something
    }
}
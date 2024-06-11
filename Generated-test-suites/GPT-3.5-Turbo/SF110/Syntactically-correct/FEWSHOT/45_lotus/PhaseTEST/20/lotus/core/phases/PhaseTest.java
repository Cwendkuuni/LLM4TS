package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;


public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        Phase phase = Mockito.mock(Phase.class, Mockito.CALLS_REAL_METHODS);
                
        phase.doPhase();

        // verify that the changePhase method is called
        verify(phase, times(1)).changePhase(any(Phase.class));
        
        // verify that the resolve method is called
        verify(phase.effect, times(1)).resolve();
    }
    
    @Test
    public void testNextPhase() {
        Phase phase = Mockito.mock(Phase.class, Mockito.CALLS_REAL_METHODS);
        
        // set Game.currentPhase to different phase classes and test the nextPhase method
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertEquals("UpkeepPhase", Game.currentPhase.getClass().getSimpleName());
        
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertEquals("DrawPhase", Game.currentPhase.getClass().getSimpleName());
        
        // repeat for other phase classes...
    }
    
}
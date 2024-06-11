package lotus.core.phases;

import lotus.core.*;
import lotus.core.effect.*;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class PhaseTest {
    @Test
    public void testDoPhase() {
        Phase phase = mock(Phase.class);
        
        phase.doPhase();
        
        verify(phase).doPhase();
    }
    
    @Test
    public void testChangePhase() {
        Phase phase = mock(Phase.class);
        
        Phase.changePhase(phase);
        
        verify(phase).resolve();
    }
    
    @Test
    public void testNextPhase() {
        Game.currentPhase = mock(UntapPhase.class);
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new UpkeepPhase());
    }
}
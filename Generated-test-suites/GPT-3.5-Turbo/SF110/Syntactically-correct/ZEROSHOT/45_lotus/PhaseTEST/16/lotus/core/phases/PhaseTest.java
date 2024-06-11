import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import lotus.core.effect.ChangePhase;
import lotus.core.effect.Game;
import lotus.core.phases.*;

public class PhaseTest {
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUntapPhase_ShouldChangeToUpkeepPhase() {
        Game.currentPhase = mock(UntapPhase.class);
        ChangePhase changePhaseMock = mock(ChangePhase.class);
        when(changePhaseMock.resolve()).thenReturn(true);
        whenNew(ChangePhase.class).withArguments(any(Phase.class)).thenReturn(changePhaseMock);
        
        Phase.nextPhase();
        
        assertEquals(Game.currentPhase.getClass(), UpkeepPhase.class);
        verify(changePhaseMock).resolve();
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUpkeepPhase_ShouldChangeToDrawPhase() {
        Game.currentPhase = mock(UpkeepPhase.class);
        ChangePhase changePhaseMock = mock(ChangePhase.class);
        when(changePhaseMock.resolve()).thenReturn(true);
        whenNew(ChangePhase.class).withArguments(any(Phase.class)).thenReturn(changePhaseMock);
        
        Phase.nextPhase();
        
        assertEquals(Game.currentPhase.getClass(), DrawPhase.class);
        verify(changePhaseMock).resolve();
    }
    
    // Add test methods for all other conditions
    
}
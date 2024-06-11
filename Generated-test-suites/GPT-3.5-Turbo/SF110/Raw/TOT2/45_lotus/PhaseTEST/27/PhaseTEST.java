import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Test doPhase method
        Phase phase = mock(Phase.class);

        doNothing().when(phase).doPhase();

        phase.doPhase();

        verify(phase, times(1)).doPhase();
    }

    @Test
    public void testNextPhase() {
        // Test nextPhase method with typical use case
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        // Test nextPhase method with edge cases
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);

        // Test nextPhase method with error scenarios
        Game.currentPhase = null;
        Phase.nextPhase();
        assertNull(Game.currentPhase);
    }
}
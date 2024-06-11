package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;

public class PhaseTest {
    
    private static final Phase mockPhase = mock(Phase.class);

    @Before
    public void setup() {
        Game.currentPhase = mockPhase;
    }

    @Test
    public void testDoPhase() {
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // implementation details
            }
        };

        phase.doPhase();

        // Verify that the doPhase method is called
        verify(phase).doPhase();
    }

    @Test
    public void testChangePhase() {
        Phase p = mock(Phase.class);
        ChangePhase mockEffect = mock(ChangePhase.class);
        when(mockEffect.resolve()).thenReturn(true); // assumes resolve returns true

        Phase.changePhase(p);

        // Verify that the current phase is set to the new phase
        assertEquals(p, Game.currentPhase);

        // Verify that the ChangePhase effect is resolved
        verify(mockEffect).resolve();
    }

    @Test
    public void testNextPhase() {
        Game.playingPlayer = 1; // assuming playingPlayer is initially 1
        
        Phase.nextPhase();

        // Verify that the current phase is changed according to the logic in nextPhase method
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        assertEquals(2, Game.playingPlayer);
    }
}
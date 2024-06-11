package lotus.core.phases;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import lotus.core.Game;
import lotus.core.phases.*;

public class PhaseTest {

    @Before
    public void setUp() {
        Game.currentPhase = null;
    }

    @Test
    public void testDoPhase() {
        Phase phase = new UntapPhase();
        phase.doPhase();
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhaseEdgeCases() {
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertTrue(Game.playingPlayer == 1);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    private Phase phase;
    
    @Before
    public void setUp() {
        phase = new Phase() {
            @Override
            public void doPhase() {
                // Do nothing
            }
        };
    }
    
    @Test
    public void testChangePhase() {
        // Typical use case - change to UpkeepPhase
        phase.changePhase(new UpkeepPhase());
        assertEquals(Game.currentPhase, new UpkeepPhase());
        
        // Typical use case - change to DrawPhase
        phase.changePhase(new DrawPhase());
        assertEquals(Game.currentPhase, new DrawPhase());
        
        // Edge case - change to CleanupPhase
        phase.changePhase(new CleanupPhase());
        assertEquals(Game.currentPhase, new CleanupPhase());
        
        // Error scenario - change to null phase
        try {
            phase.changePhase(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testNextPhase() {
        // Typical use case - change to next phase after UntapPhase
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertEquals(Game.currentPhase, new UpkeepPhase());
        
        // Typical use case - change to next phase after UpkeepPhase
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertEquals(Game.currentPhase, new DrawPhase());
        
        // Edge case - change to next phase after EndOfTurnPhase
        Game.currentPhase = new EndOfTurnPhase();
        phase.nextPhase();
        assertEquals(Game.currentPhase, new CleanupPhase());
        
        // Error scenario - invalid current phase
        Game.currentPhase = null;
        try {
            phase.nextPhase();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
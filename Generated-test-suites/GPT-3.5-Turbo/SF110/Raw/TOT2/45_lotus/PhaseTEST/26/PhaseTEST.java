import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Create a mock Phase object
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // Do nothing for testing purposes
            }
        };
        
        phase.doPhase();
        // Add assertions here if needed
    }

    @Test
    public void testNextPhase() {
        // Test for typical use case
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), UpkeepPhase.class);
        
        // Add more test cases for edge cases and exceptions
        // Test when Game.currentPhase is an instance of all different phase classes
        
    }
}
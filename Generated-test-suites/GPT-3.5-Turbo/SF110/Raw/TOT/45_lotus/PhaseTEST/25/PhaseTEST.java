import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Test case 1: Typical use case
        Phase phase = new UpkeepPhase();
        phase.doPhase();
        assertEquals("The current phase should be UpkeepPhase", phase, Game.currentPhase);

        // Test case 2: Edge case
        Phase phase2 = new EndOfTurnPhase();
        phase2.doPhase();
        assertEquals("The current phase should be EndOfTurnPhase", phase2, Game.currentPhase);

        // Test case 3: Error scenario
        Phase phase3 = new InvalidPhase();
        try {
            phase3.doPhase();
            fail("An exception should have been thrown");
        } catch (Exception e) {
            assertEquals("The exception message should be correct", "Invalid phase", e.getMessage());
        }
    }

    @Test
    public void testNextPhase() {
        // Test case 1: Typical use case (UntapPhase)
        Phase.nextPhase();
        assertEquals("The next phase should be UpkeepPhase", UpkeepPhase.class, Game.currentPhase.getClass());

        // Test case 2: Typical use case (EndOfTurnPhase)
        Phase.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals("The next phase should be UntapPhase", UntapPhase.class, Game.currentPhase.getClass());

        // Test case 3: Edge case (PlayerChangePhase)
        Phase.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertEquals("The next phase should be UntapPhase", UntapPhase.class, Game.currentPhase.getClass());

        // Test case 4: Error scenario (InvalidPhase)
        Phase.currentPhase = new Main1Phase();
        try {
            Phase.nextPhase();
            fail("An exception should have been thrown");
        } catch (Exception e) {
            assertEquals("The exception message should be correct", "Invalid phase transition", e.getMessage());
        }
    }
}
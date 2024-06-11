import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhaseTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testDoPhase() {
        // Test typical use case
        // Set up
        Phase phase = new UntapPhase();

        // Execute
        phase.doPhase();

        // Verify
        assertEquals("Incorrect current phase", phase, Game.currentPhase);

        // Clean up
        Game.currentPhase = null;
    }

    @Test
    public void testChangePhase() {
        // Test typical use case
        // Set up
        Phase newPhase = new UpkeepPhase();

        // Execute
        Phase.changePhase(newPhase);

        // Verify
        assertEquals("Incorrect current phase", newPhase, Game.currentPhase);

        // Clean up
        Game.currentPhase = null;
    }

    @Test
    public void testNextPhase_UntapPhase() {
        // Set up
        Game.currentPhase = new UntapPhase();

        // Execute
        Phase.nextPhase();

        // Verify
        assertEquals("Incorrect current phase", new UpkeepPhase(), Game.currentPhase);

        // Clean up
        Game.currentPhase = null;
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        // Set up
        Game.currentPhase = new UpkeepPhase();

        // Execute
        Phase.nextPhase();

        // Verify
        assertEquals("Incorrect current phase", new DrawPhase(), Game.currentPhase);

        // Clean up
        Game.currentPhase = null;
    }

    @Test
    public void testNextPhase_DrawPhase() {
        // Set up
        Game.currentPhase = new DrawPhase();

        // Execute
        Phase.nextPhase();

        // Verify
        assertEquals("Incorrect current phase", new Main1Phase(), Game.currentPhase);

        // Clean up
        Game.currentPhase = null;
    }

    // Continue with other test cases for remaining phases

    @Test
    public void testNextPhase_CleanupPhase() {
        // Set up
        Game.currentPhase = new CleanupPhase();

        // Execute
        Phase.nextPhase();

        // Verify
        assertEquals("Incorrect current phase", new PlayerChangePhase(), Game.currentPhase);
        assertEquals("Incorrect playing player", 1, Game.playingPlayer);

        // Clean up
        Game.currentPhase = null;
        Game.playingPlayer = 0;
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        // Set up
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;

        // Execute
        Phase.nextPhase();

        // Verify
        assertEquals("Incorrect current phase", new UntapPhase(), Game.currentPhase);
        assertEquals("Incorrect playing player", 2, Game.playingPlayer);

        // Clean up
        Game.currentPhase = null;
        Game.playingPlayer = 0;
    }

}
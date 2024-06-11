import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class PhaseTest {

    private Phase phase;

    @Before
    public void setUp() {
        phase = new Phase() {
            @Override
            public void doPhase() {
                // Implementation not necessary for testing
            }
        };
    }

    @Test // Test nextPhase() when current phase is UntapPhase
    public void testNextPhase_UntapPhase() {
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }

    @Test // Test nextPhase() when current phase is UpkeepPhase
    public void testNextPhase_UpkeepPhase() {
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }

    // Test nextPhase() for remaining phases similarly for DrawPhase, Main1Phase, CombatBeginningPhase,
    // DeclareAttackersPhase, DeclareBlockersPhase, CombatDamagePhase, CombatEndPhase, Main2Phase,
    // EndOfTurnPhase, CleanupPhase, PlayerChangePhase

    @Test // Test nextPhase() at the end of PlayerChangePhase
    public void testNextPhase_PlayerChangePhase() {
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;
        phase.nextPhase();
        assertTrue(Game.playingPlayer == 2);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

}
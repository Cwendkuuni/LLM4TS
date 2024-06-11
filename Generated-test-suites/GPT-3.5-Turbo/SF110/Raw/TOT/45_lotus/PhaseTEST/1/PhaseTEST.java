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
                // implementation not required for testing
            }
        };
    }

    @Test
    public void testNextPhase_UntapPhaseToUpkeepPhase() {
        Game.currentPhase = new UntapPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhaseToDrawPhase() {
        Game.currentPhase = new UpkeepPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof DrawPhase);
    }

    @Test
    public void testNextPhase_DrawPhaseToMain1Phase() {
        Game.currentPhase = new DrawPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof Main1Phase);
    }

    @Test
    public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
        Game.currentPhase = new Main1Phase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
        Game.currentPhase = new CombatBeginningPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
        Game.currentPhase = new DeclareBlockersPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
        Game.currentPhase = new CombatDamagePhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhaseToMain2Phase() {
        Game.currentPhase = new CombatEndPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof Main2Phase);
    }

    @Test
    public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
        Game.currentPhase = new Main2Phase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
        Game.currentPhase = new EndOfTurnPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }

    @Test
    public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
        Game.currentPhase = new CleanupPhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhaseToUntapPhase() {
        Game.currentPhase = new PlayerChangePhase();

        phase.nextPhase();

        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(2, Game.playingPlayer);
    }
}
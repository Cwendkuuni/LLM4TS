import static org.junit.Assert.*;
import org.junit.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // TODO: Implement test case for doPhase() method
    }

    @Test
    public void testNextPhase() {
        // TODO: Implement test case for nextPhase() method
    }

    @Test
    public void testNextPhase_UntapPhaseToUpkeepPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhaseToDrawPhase() {
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }

    @Test
    public void testNextPhase_DrawPhaseToMain1Phase() {
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }

    @Test
    public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhaseToMain2Phase() {
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }

    @Test
    public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }

    @Test
    public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhaseToUntapPhase() {
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
  
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    // Testing nextPhase method

    // Test nextPhase when currentPhase is UntapPhase
    @Test
    public void testNextPhase1() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }

    // Test nextPhase when currentPhase is UpkeepPhase
    @Test
    public void testNextPhase2() {
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }

    // Test nextPhase when currentPhase is DrawPhase
    @Test
    public void testNextPhase3() {
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }

    // Test nextPhase when currentPhase is Main1Phase
    @Test
    public void testNextPhase4() {
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }

    // Test nextPhase when currentPhase is CombatBeginningPhase
    @Test
    public void testNextPhase5() {
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }

    // Test nextPhase when currentPhase is DeclareAttackersPhase
    @Test
    public void testNextPhase6() {
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }

    // Test nextPhase when currentPhase is DeclareBlockersPhase
    @Test
    public void testNextPhase7() {
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }

    // Test nextPhase when currentPhase is CombatDamagePhase
    @Test
    public void testNextPhase8() {
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }

    // Test nextPhase when currentPhase is CombatEndPhase
    @Test
    public void testNextPhase9() {
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }

    // Test nextPhase when currentPhase is Main2Phase
    @Test
    public void testNextPhase10() {
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }

    // Test nextPhase when currentPhase is EndOfTurnPhase
    @Test
    public void testNextPhase11() {
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }

    // Test nextPhase when currentPhase is CleanupPhase
    @Test
    public void testNextPhase12() {
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }

    // Test nextPhase when currentPhase is PlayerChangePhase
    @Test
    public void testNextPhase13() {
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
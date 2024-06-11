import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Add test cases for the doPhase() method
    }

    @Test
    public void testNextPhase() {
        // Add test cases for the nextPhase() method
        // Test normal case
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        // Test all other cases
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);

        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);

        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);

        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);

        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);

        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);

        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);

        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);

        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);

        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);

        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);

        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
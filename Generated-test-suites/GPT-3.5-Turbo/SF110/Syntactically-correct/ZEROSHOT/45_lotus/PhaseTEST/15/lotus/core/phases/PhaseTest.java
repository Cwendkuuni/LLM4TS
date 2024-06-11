import org.junit.*;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testChangePhase() {
        // Test changePhase method
        // Create instances of each phase
        Phase phase1 = new UntapPhase();
        Phase phase2 = new UpkeepPhase();
        Phase phase3 = new DrawPhase();
        Phase phase4 = new Main1Phase();
        Phase phase5 = new CombatBeginningPhase();
        Phase phase6 = new DeclareAttackersPhase();
        Phase phase7 = new DeclareBlockersPhase();
        Phase phase8 = new CombatDamagePhase();
        Phase phase9 = new CombatEndPhase();
        Phase phase10 = new Main2Phase();
        Phase phase11 = new EndOfTurnPhase();
        Phase phase12 = new CleanupPhase();
        Phase phase13 = new PlayerChangePhase();

        // Change to each phase and check if currentPhase is set correctly
        Phase.changePhase(phase1);
        assertEquals(phase1, Game.currentPhase);

        Phase.changePhase(phase2);
        assertEquals(phase2, Game.currentPhase);

        Phase.changePhase(phase3);
        assertEquals(phase3, Game.currentPhase);

        Phase.changePhase(phase4);
        assertEquals(phase4, Game.currentPhase);

        Phase.changePhase(phase5);
        assertEquals(phase5, Game.currentPhase);

        Phase.changePhase(phase6);
        assertEquals(phase6, Game.currentPhase);

        Phase.changePhase(phase7);
        assertEquals(phase7, Game.currentPhase);

        Phase.changePhase(phase8);
        assertEquals(phase8, Game.currentPhase);

        Phase.changePhase(phase9);
        assertEquals(phase9, Game.currentPhase);

        Phase.changePhase(phase10);
        assertEquals(phase10, Game.currentPhase);

        Phase.changePhase(phase11);
        assertEquals(phase11, Game.currentPhase);

        Phase.changePhase(phase12);
        assertEquals(phase12, Game.currentPhase);

        Phase.changePhase(phase13);
        assertEquals(phase13, Game.currentPhase);
    }

    @Test
    public void testNextPhase() {
        // Test nextPhase method
        // Set currentPhase to each phase and check if nextPhase is set correctly
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(new UpkeepPhase(), Game.currentPhase);

        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertEquals(new DrawPhase(), Game.currentPhase);

        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertEquals(new Main1Phase(), Game.currentPhase);

        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertEquals(new CombatBeginningPhase(), Game.currentPhase);

        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertEquals(new DeclareAttackersPhase(), Game.currentPhase);

        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertEquals(new DeclareBlockersPhase(), Game.currentPhase);

        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertEquals(new CombatDamagePhase(), Game.currentPhase);

        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertEquals(new CombatEndPhase(), Game.currentPhase);

        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertEquals(new Main2Phase(), Game.currentPhase);

        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertEquals(new EndOfTurnPhase(), Game.currentPhase);

        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertEquals(new CleanupPhase(), Game.currentPhase);

        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertEquals(new PlayerChangePhase(), Game.currentPhase);

        // Test playingPlayer variable update
        int initialPlayingPlayer = Game.playingPlayer;
        Phase.nextPhase();
        assertEquals(initialPlayingPlayer % 2 + 1, Game.playingPlayer);
    }
}
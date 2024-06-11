package lotus.core.phases;

import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Test case 1
        Phase phase1 = new UntapPhase();
        phase1.doPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        // Test case 2
        Phase phase2 = new UpkeepPhase();
        phase2.doPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);

        // Test case 3
        Phase phase3 = new DrawPhase();
        phase3.doPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);

        // Test case 4
        Phase phase4 = new Main1Phase();
        phase4.doPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);

        // Test case 5
        Phase phase5 = new CombatBeginningPhase();
        phase5.doPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);

        // Test case 6
        Phase phase6 = new DeclareAttackersPhase();
        phase6.doPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);

        // Test case 7
        Phase phase7 = new DeclareBlockersPhase();
        phase7.doPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);

        // Test case 8
        Phase phase8 = new CombatDamagePhase();
        phase8.doPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);

        // Test case 9
        Phase phase9 = new CombatEndPhase();
        phase9.doPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);

        // Test case 10
        Phase phase10 = new Main2Phase();
        phase10.doPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);

        // Test case 11
        Phase phase11 = new EndOfTurnPhase();
        phase11.doPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);

        // Test case 12
        Phase phase12 = new CleanupPhase();
        phase12.doPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);

        // Test case 13
        Phase phase13 = new PlayerChangePhase();
        phase13.doPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

    @Test
    public void testNextPhase() {
        // Test case 1
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        // Test case 2
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);

        // Test case 3
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);

        // Test case 4
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);

        // Test case 5
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);

        // Test case 6
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);

        // Test case 7
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);

        // Test case 8
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);

        // Test case 9
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);

        // Test case 10
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);

        // Test case 11
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);

        // Test case 12
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        assertEquals(2, Game.playingPlayer);

        // Test case 13
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
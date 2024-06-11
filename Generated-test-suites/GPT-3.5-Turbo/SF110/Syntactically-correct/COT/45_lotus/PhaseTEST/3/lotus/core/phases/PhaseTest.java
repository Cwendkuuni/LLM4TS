
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Since the doPhase() method is abstract and has no implementation, we cannot directly test its functionality.
        // This test will need to be implemented by the subclasses that extend the Phase class.
    }

    @Test
    public void testNextPhase() {
        // Testing the functionality of the nextPhase() method by checking if the phase changes correctly

        // Initialize Game.currentPhase to an instance of UntapPhase
        Game.currentPhase = new UntapPhase();

        // Test the nextPhase() method for each possible phase change

        // Phase changes from UntapPhase to UpkeepPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        // Phase changes from UpkeepPhase to DrawPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);

        // Phase changes from DrawPhase to Main1Phase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);

        // Phase changes from Main1Phase to CombatBeginningPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);

        // Phase changes from CombatBeginningPhase to DeclareAttackersPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);

        // Phase changes from DeclareAttackersPhase to DeclareBlockersPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);

        // Phase changes from DeclareBlockersPhase to CombatDamagePhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);

        // Phase changes from CombatDamagePhase to CombatEndPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);

        // Phase changes from CombatEndPhase to Main2Phase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);

        // Phase changes from Main2Phase to EndOfTurnPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);

        // Phase changes from EndOfTurnPhase to CleanupPhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);

        // Phase changes from CleanupPhase to PlayerChangePhase
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);

        // Phase changes from PlayerChangePhase to UntapPhase, with a change in playingPlayer
        int initialPlayingPlayer = Game.playingPlayer;

        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(Game.playingPlayer, initialPlayingPlayer % 2 + 1);
    }
}



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
                // do nothing in the test implementation
            }
        };
    }

    @Test
    public void testChangePhase() {
        // Test changing phase from UntapPhase to UpkeepPhase
        Game.currentPhase = new UntapPhase();
        phase.changePhase(new UpkeepPhase());
        assertEquals("UpkeepPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from UpkeepPhase to DrawPhase
        Game.currentPhase = new UpkeepPhase();
        phase.changePhase(new DrawPhase());
        assertEquals("DrawPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from DrawPhase to Main1Phase
        Game.currentPhase = new DrawPhase();
        phase.changePhase(new Main1Phase());
        assertEquals("Main1Phase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from Main1Phase to CombatBeginningPhase
        Game.currentPhase = new Main1Phase();
        phase.changePhase(new CombatBeginningPhase());
        assertEquals("CombatBeginningPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from CombatBeginningPhase to DeclareAttackersPhase
        Game.currentPhase = new CombatBeginningPhase();
        phase.changePhase(new DeclareAttackersPhase());
        assertEquals("DeclareAttackersPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from DeclareAttackersPhase to DeclareBlockersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        phase.changePhase(new DeclareBlockersPhase());
        assertEquals("DeclareBlockersPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from DeclareBlockersPhase to CombatDamagePhase
        Game.currentPhase = new DeclareBlockersPhase();
        phase.changePhase(new CombatDamagePhase());
        assertEquals("CombatDamagePhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from CombatDamagePhase to CombatEndPhase
        Game.currentPhase = new CombatDamagePhase();
        phase.changePhase(new CombatEndPhase());
        assertEquals("CombatEndPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from CombatEndPhase to Main2Phase
        Game.currentPhase = new CombatEndPhase();
        phase.changePhase(new Main2Phase());
        assertEquals("Main2Phase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from Main2Phase to EndOfTurnPhase
        Game.currentPhase = new Main2Phase();
        phase.changePhase(new EndOfTurnPhase());
        assertEquals("EndOfTurnPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from EndOfTurnPhase to CleanupPhase
        Game.currentPhase = new EndOfTurnPhase();
        phase.changePhase(new CleanupPhase());
        assertEquals("CleanupPhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from CleanupPhase to PlayerChangePhase
        Game.currentPhase = new CleanupPhase();
        phase.changePhase(new PlayerChangePhase());
        assertEquals("PlayerChangePhase", Game.currentPhase.getClass().getSimpleName());

        // Test changing phase from PlayerChangePhase to UntapPhase
        Game.currentPhase = new PlayerChangePhase();
        phase.changePhase(new UntapPhase());
        assertEquals("UntapPhase", Game.currentPhase.getClass().getSimpleName());
    }

    @Test
    public void testNextPhase() {
        // Test transitioning from UntapPhase to UpkeepPhase
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertEquals("UpkeepPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from UpkeepPhase to DrawPhase
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertEquals("DrawPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from DrawPhase to Main1Phase
        Game.currentPhase = new DrawPhase();
        phase.nextPhase();
        assertEquals("Main1Phase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from Main1Phase to CombatBeginningPhase
        Game.currentPhase = new Main1Phase();
        phase.nextPhase();
        assertEquals("CombatBeginningPhase", Game.currentPhase.getClass().getSimpleName());
        
        // Test transitioning from CombatBeginningPhase to DeclareAttackersPhase
        Game.currentPhase = new CombatBeginningPhase();
        phase.nextPhase();
        assertEquals("DeclareAttackersPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from DeclareAttackersPhase to DeclareBlockersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        phase.nextPhase();
        assertEquals("DeclareBlockersPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from DeclareBlockersPhase to CombatDamagePhase
        Game.currentPhase = new DeclareBlockersPhase();
        phase.nextPhase();
        assertEquals("CombatDamagePhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from CombatDamagePhase to CombatEndPhase
        Game.currentPhase = new CombatDamagePhase();
        phase.nextPhase();
        assertEquals("CombatEndPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from CombatEndPhase to Main2Phase
        Game.currentPhase = new CombatEndPhase();
        phase.nextPhase();
        assertEquals("Main2Phase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from Main2Phase to EndOfTurnPhase
        Game.currentPhase = new Main2Phase();
        phase.nextPhase();
        assertEquals("EndOfTurnPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from EndOfTurnPhase to CleanupPhase
        Game.currentPhase = new EndOfTurnPhase();
        phase.nextPhase();
        assertEquals("CleanupPhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from CleanupPhase to PlayerChangePhase
        Game.currentPhase = new CleanupPhase();
        phase.nextPhase();
        assertEquals("PlayerChangePhase", Game.currentPhase.getClass().getSimpleName());

        // Test transitioning from PlayerChangePhase to UntapPhase
        Game.currentPhase = new PlayerChangePhase();
        phase.nextPhase();
        assertEquals("UntapPhase", Game.currentPhase.getClass().getSimpleName());
    }

    // Additional test cases can be added for boundary conditions and error scenarios

}


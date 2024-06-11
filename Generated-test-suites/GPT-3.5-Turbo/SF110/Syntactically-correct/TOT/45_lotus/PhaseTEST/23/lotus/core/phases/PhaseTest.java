import static org.junit.Assert.*;
import org.junit.Test;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Test case for doPhase method
        
        // Typical use case
        // we can test one of the concrete subclasses of Phase
        Phase phase = new UpkeepPhase();
        phase.doPhase();
        // check if the current phase is the desired phase
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Edge case
        // Test with a concrete subclass that extends Phase but is not included in the conditionals
        // when changing phase in the nextPhase method
        Phase phase2 = new TestPhase();
        phase2.doPhase();
        // check if the current phase is the desired phase
        assertTrue(Game.currentPhase instanceof TestPhase);
        
        // Error scenario
        // Test with a null phase object
        Phase phase3 = null;
        // check for any exceptions thrown
        assertThrows(NullPointerException.class, () -> {
            phase3.doPhase();
        });
    }

    @Test
    public void testNextPhase() {
        // Test case for nextPhase method
        
        // Typical use case
        // Test with each possible phase and check if the current phase is changed correctly
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
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
        
        // Edge case
        // Test with the same phase (UntapPhase) twice in a row
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Error scenario
        // Test with a null currentPhase object
        Game.currentPhase = null;
        // check for any exceptions thrown
        assertThrows(NullPointerException.class, () -> {
            Phase.nextPhase();
        });
    }
    
    // Test class that extends Phase but is not included in the conditionals in the nextPhase method
    private class TestPhase extends Phase {
        @Override
        public void doPhase() {}
    }

}
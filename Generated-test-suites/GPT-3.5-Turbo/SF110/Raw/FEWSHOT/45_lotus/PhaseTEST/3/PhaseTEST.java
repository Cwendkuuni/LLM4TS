package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        // Create a dummy implementation of Phase for testing
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // Custom implementation
            }
        };
        
        // Verify that doPhase() is being called with custom implementation
        phase.doPhase();
        
        // Add additional assertions as needed
    }
    
    @Test
    public void testNextPhase() {
        // Start with an initial phase
        Game.currentPhase = new UntapPhase();
        
        // Verify that nextPhase() changes the phase correctly
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        assertEquals(2, Game.playingPlayer);
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(1, Game.playingPlayer);
        
        // Add additional assertions as needed
    }
}
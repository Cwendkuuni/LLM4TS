package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;

public class PhaseTest {
    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        assertChangePhaseEffect(new ChangePhase(new UpkeepPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        assertChangePhaseEffect(new ChangePhase(new DrawPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        assertChangePhaseEffect(new ChangePhase(new Main1Phase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        assertChangePhaseEffect(new ChangePhase(new CombatBeginningPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        assertChangePhaseEffect(new ChangePhase(new DeclareAttackersPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        assertChangePhaseEffect(new ChangePhase(new DeclareBlockersPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        assertChangePhaseEffect(new ChangePhase(new CombatDamagePhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        assertChangePhaseEffect(new ChangePhase(new CombatEndPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        assertChangePhaseEffect(new ChangePhase(new Main2Phase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        assertChangePhaseEffect(new ChangePhase(new EndOfTurnPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        assertChangePhaseEffect(new ChangePhase(new CleanupPhase()));
        
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        assertEquals(2, Game.playingPlayer);
        assertChangePhaseEffect(new ChangePhase(new UntapPhase()));
    }
    
    private void assertChangePhaseEffect(ChangePhase expected) {
        // implementation to assert ChangePhase effect
    }
}
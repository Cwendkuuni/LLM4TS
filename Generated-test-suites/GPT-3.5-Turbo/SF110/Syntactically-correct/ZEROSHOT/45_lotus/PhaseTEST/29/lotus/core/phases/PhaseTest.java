package lotus.core.phases;

import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testChangePhase() {
        Phase p = new TestPhase();
        Phase.changePhase(p);
        
        // Assert that the current phase is updated
        assertEquals(p, Game.currentPhase);
        
        // Assert that the effect is resolved
        assertTrue(EffectResolver.hasResolved(p));
    }
    
    @Test
    public void testNextPhase() {
        // Test when current phase is UntapPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Test when current phase is UpkeepPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Test when current phase is DrawPhase
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        // Test when current phase is Main1Phase
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        // Test when current phase is CombatBeginningPhase
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        // Test when current phase is DeclareAttackersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        // Test when current phase is DeclareBlockersPhase
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        // Test when current phase is CombatDamagePhase
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        // Test when current phase is CombatEndPhase
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        // Test when current phase is Main2Phase
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        // Test when current phase is EndOfTurnPhase
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        // Test when current phase is CleanupPhase
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        
        // Test when current phase is PlayerChangePhase
        Game.currentPhase = new PlayerChangePhase();
        int initialPlayingPlayer = Game.playingPlayer;
        Phase.nextPhase();
        assertEquals(initialPlayingPlayer % 2 + 1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
    
    private class TestPhase extends Phase {
        public void doPhase() {}
    }
}
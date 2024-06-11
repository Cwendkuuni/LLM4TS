import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {
    
    @Test
    public void testNextPhase() {
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // Test implementation not required
            }
        };
        
        // Test if current phase is UntapPhase
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Test if current phase is UpkeepPhase
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Test if current phase is DrawPhase
        Game.currentPhase = new DrawPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        // Test if current phase is Main1Phase
        Game.currentPhase = new Main1Phase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        // Test if current phase is CombatBeginningPhase
        Game.currentPhase = new CombatBeginningPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        // Test if current phase is DeclareAttackersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        // Test if current phase is DeclareBlockersPhase
        Game.currentPhase = new DeclareBlockersPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        // Test if current phase is CombatDamagePhase
        Game.currentPhase = new CombatDamagePhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        // Test if current phase is CombatEndPhase
        Game.currentPhase = new CombatEndPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        // Test if current phase is Main2Phase
        Game.currentPhase = new Main2Phase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        // Test if current phase is EndOfTurnPhase
        Game.currentPhase = new EndOfTurnPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        // Test if current phase is CleanupPhase
        Game.currentPhase = new CleanupPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        
        // Test if current phase is PlayerChangePhase
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;
        phase.nextPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
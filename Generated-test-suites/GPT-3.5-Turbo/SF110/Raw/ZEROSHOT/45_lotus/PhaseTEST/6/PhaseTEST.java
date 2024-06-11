import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
    
    @Test
    public void testNextPhase() {
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // empty method
            }
        };
        
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        Game.currentPhase = new DrawPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        Game.currentPhase = new Main1Phase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        Game.currentPhase = new CombatBeginningPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        Game.currentPhase = new DeclareAttackersPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        Game.currentPhase = new DeclareBlockersPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        Game.currentPhase = new CombatDamagePhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        Game.currentPhase = new CombatEndPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        Game.currentPhase = new Main2Phase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        Game.currentPhase = new EndOfTurnPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        Game.currentPhase = new CleanupPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        
        Game.currentPhase = new PlayerChangePhase();
        phase.nextPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
    
}
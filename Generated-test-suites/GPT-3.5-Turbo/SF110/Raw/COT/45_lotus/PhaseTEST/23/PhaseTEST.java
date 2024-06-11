
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        Phase phase = mock(Phase.class);
        phase.doPhase();
        verify(phase, times(1)).doPhase();
    }
    
    @Test
    public void testNextPhase() {
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
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}


package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class PhaseTest {
    
    private Phase phase;
    
    @Before
    public void setUp() {
        phase = mock(Phase.class);
    }
    
    @Test
    public void testDoPhase() {
        phase.doPhase();
        verify(phase).doPhase();
    }
    
    @Test
    public void testChangePhase() {
        Phase p = mock(Phase.class);
        ChangePhase effect = mock(ChangePhase.class);
        ArgumentCaptor<Phase> phaseCaptor = ArgumentCaptor.forClass(Phase.class);
        
        when(effect.resolve()).thenReturn(true);
        
        Game.currentPhase = phase;
        when(p.toString()).thenReturn("Phase");
        
        Phase.changePhase(p);
        
        assertEquals(p, Game.currentPhase);
        
        verify(effect).resolve();
        verify(p).toString();
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
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
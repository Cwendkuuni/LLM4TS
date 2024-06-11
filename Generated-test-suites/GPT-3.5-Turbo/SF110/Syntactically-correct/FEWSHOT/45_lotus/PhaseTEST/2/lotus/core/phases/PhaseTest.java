package lotus.core.phases;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PhaseTest {
    
    private Phase phase;
    
    @Before
    public void setUp() {
        phase = mock(Phase.class);
    }
    
    @Test
    public void testDoPhase() {
        phase.doPhase();
        verify(phase, times(1)).doPhase();
    }
    
    @Test
    public void testChangePhase() {
        Phase p = mock(Phase.class);
        Game.currentPhase = p;
        ChangePhase effect = mock(ChangePhase.class);
        when(effect.resolve()).thenReturn(true);
        
        Phase.changePhase(p);
        
        verify(effect, times(1)).resolve();
        verify(effect, times(1)).resolve();
        verify(p, times(1)).doPhase();
    }
    
    @Test
    public void testNextPhase_UntapPhase() {
        Game.currentPhase = mock(UntapPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(UpkeepPhase.class));
    }
    
    @Test
    public void testNextPhase_UpkeepPhase() {
        Game.currentPhase = mock(UpkeepPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(DrawPhase.class));
    }
    
    @Test
    public void testNextPhase_DrawPhase() {
        Game.currentPhase = mock(DrawPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(Main1Phase.class));
    }
    
    @Test
    public void testNextPhase_Main1Phase() {
        Game.currentPhase = mock(Main1Phase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(CombatBeginningPhase.class));
    }
    
    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game.currentPhase = mock(CombatBeginningPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(DeclareAttackersPhase.class));
    }
    
    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game.currentPhase = mock(DeclareAttackersPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(DeclareBlockersPhase.class));
    }
    
    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game.currentPhase = mock(DeclareBlockersPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(CombatDamagePhase.class));
    }
    
    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game.currentPhase = mock(CombatDamagePhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(CombatEndPhase.class));
    }
    
    @Test
    public void testNextPhase_CombatEndPhase() {
        Game.currentPhase = mock(CombatEndPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(Main2Phase.class));
    }
    
    @Test
    public void testNextPhase_Main2Phase() {
        Game.currentPhase = mock(Main2Phase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(EndOfTurnPhase.class));
    }
    
    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game.currentPhase = mock(EndOfTurnPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(CleanupPhase.class));
    }
    
    @Test
    public void testNextPhase_CleanupPhase() {
        Game.currentPhase = mock(CleanupPhase.class);
        Phase.nextPhase();
        verify(Phase.class, times(1)).changePhase(any(PlayerChangePhase.class));
    }
    
    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game.currentPhase = mock(PlayerChangePhase.class);
        Phase.nextPhase();
        verify(Game.class, times(1)).playingPlayer();
        verify(Phase.class, times(1)).changePhase(any(UntapPhase.class));
    }
}
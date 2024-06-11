
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Phase phase;
    
    @Before
    public void setUp() {
        phase = mock(Phase.class);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUntapPhase_ShouldChangePhaseToUpkeepPhase() {
        Game.currentPhase = new UntapPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new UpkeepPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUpkeepPhase_ShouldChangePhaseToDrawPhase() {
        Game.currentPhase = new UpkeepPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new DrawPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsDrawPhase_ShouldChangePhaseToMain1Phase() {
        Game.currentPhase = new DrawPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new Main1Phase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain1Phase_ShouldChangePhaseToCombatBeginningPhase() {
        Game.currentPhase = new Main1Phase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new CombatBeginningPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatBeginningPhase_ShouldChangePhaseToDeclareAttackersPhase() {
        Game.currentPhase = new CombatBeginningPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new DeclareAttackersPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsDeclareAttackersPhase_ShouldChangePhaseToDeclareBlockersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new DeclareBlockersPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsDeclareBlockersPhase_ShouldChangePhaseToCombatDamagePhase() {
        Game.currentPhase = new DeclareBlockersPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new CombatDamagePhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatDamagePhase_ShouldChangePhaseToCombatEndPhase() {
        Game.currentPhase = new CombatDamagePhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new CombatEndPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatEndPhase_ShouldChangePhaseToMain2Phase() {
        Game.currentPhase = new CombatEndPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new Main2Phase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain2Phase_ShouldChangePhaseToEndOfTurnPhase() {
        Game.currentPhase = new Main2Phase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new EndOfTurnPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsEndOfTurnPhase_ShouldChangePhaseToCleanupPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new CleanupPhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCleanupPhase_ShouldChangePhaseToPlayerChangePhase() {
        Game.currentPhase = new CleanupPhase();
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new PlayerChangePhase());
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsPlayerChangePhase_ShouldChangePhaseToUntapPhaseAndUpdatePlayingPlayer() {
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;
        
        Phase.nextPhase();
        
        verify(phase).changePhase(new UntapPhase());
        assertEquals(2, Game.playingPlayer);
    }
}
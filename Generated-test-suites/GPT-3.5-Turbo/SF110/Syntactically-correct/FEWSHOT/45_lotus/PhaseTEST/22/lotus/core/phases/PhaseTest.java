package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import lotus.core.ChangePhase;
import lotus.core.Game;
import lotus.core.Player;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        Phase phase = new UntapPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the UntapPhase
        
        phase = new UpkeepPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the UpkeepPhase
        
        phase = new DrawPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the DrawPhase
        
        phase = new Main1Phase();
        phase.doPhase();
        // Add assertions for the expected behavior of the Main1Phase
        
        phase = new CombatBeginningPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the CombatBeginningPhase
        
        phase = new DeclareAttackersPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the DeclareAttackersPhase
        
        phase = new DeclareBlockersPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the DeclareBlockersPhase
        
        phase = new CombatDamagePhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the CombatDamagePhase
        
        phase = new CombatEndPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the CombatEndPhase
        
        phase = new Main2Phase();
        phase.doPhase();
        // Add assertions for the expected behavior of the Main2Phase
        
        phase = new EndOfTurnPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the EndOfTurnPhase
        
        phase = new CleanupPhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the CleanupPhase
        
        phase = new PlayerChangePhase();
        phase.doPhase();
        // Add assertions for the expected behavior of the PlayerChangePhase
    }
    
    @Test
    public void testChangePhase() {
        Phase p = mock(Phase.class);
        Phase.changePhase(p);
        
        assertEquals(p, Game.currentPhase);
        verify(p).resolve();
    }
    
    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Repeat for all the possible phases
    }
}
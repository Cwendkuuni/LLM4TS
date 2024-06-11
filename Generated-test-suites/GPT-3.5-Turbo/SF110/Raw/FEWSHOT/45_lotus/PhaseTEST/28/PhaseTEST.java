package lotus.core.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {
    
    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertEquals(Main1Phase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertEquals(CombatEndPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertEquals(Main2Phase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertEquals(CleanupPhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
        
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
    }
}
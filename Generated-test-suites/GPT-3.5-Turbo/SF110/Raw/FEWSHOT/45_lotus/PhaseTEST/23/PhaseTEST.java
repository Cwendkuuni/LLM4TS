package lotus.core.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lotus.core.Game;
import lotus.core.GameTest;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.CleanupPhase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.PlayerChangePhase;
import lotus.core.phases.UntapPhase;

public class PhaseTest {
    
    @Test
    public void testDoPhase() {
        Phase phase = new UntapPhase();
        phase.doPhase();
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        
        phase = new UpkeepPhase();
        phase.doPhase();
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
        
        phase = new DrawPhase();
        phase.doPhase();
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());
        
        phase = new Main1Phase();
        phase.doPhase();
        assertEquals(Main1Phase.class, Game.currentPhase.getClass());
        
        phase = new CombatBeginningPhase();
        phase.doPhase();
        assertEquals(CombatBeginningPhase.class, Game.currentPhase.getClass());
        
        phase = new DeclareAttackersPhase();
        phase.doPhase();
        assertEquals(DeclareAttackersPhase.class, Game.currentPhase.getClass());
        
        phase = new DeclareBlockersPhase();
        phase.doPhase();
        assertEquals(DeclareBlockersPhase.class, Game.currentPhase.getClass());
        
        phase = new CombatDamagePhase();
        phase.doPhase();
        assertEquals(CombatDamagePhase.class, Game.currentPhase.getClass());
        
        phase = new CombatEndPhase();
        phase.doPhase();
        assertEquals(CombatEndPhase.class, Game.currentPhase.getClass());
        
        phase = new Main2Phase();
        phase.doPhase();
        assertEquals(Main2Phase.class, Game.currentPhase.getClass());
        
        phase = new EndOfTurnPhase();
        phase.doPhase();
        assertEquals(EndOfTurnPhase.class, Game.currentPhase.getClass());
        
        phase = new CleanupPhase();
        phase.doPhase();
        assertEquals(CleanupPhase.class, Game.currentPhase.getClass());
        
        phase = new PlayerChangePhase();
        phase.doPhase();
        assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
    }
    
    @Test
    public void testNextPhase() {
        GameTest.setTestGame(new Game());
        Game.currentPhase = new UntapPhase();
        Game.playingPlayer = 1;
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
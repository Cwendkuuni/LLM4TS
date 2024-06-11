package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lotus.core.Game;

public class PhaseTest {
    @Test
    public void testDoPhase() {
        // TODO: Test doPhase() method
    }
    
    @Test
    public void testChangePhase() {
        // TODO: Test changePhase() method
    }
    
    @Test
    public void testNextPhase() {
        // Test nextPhase() method
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
        assertTrue(Game.playingPlayer == 2);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
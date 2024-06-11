package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PhaseTest {
    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(new UpkeepPhase(), Game.currentPhase);
        
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertEquals(new DrawPhase(), Game.currentPhase);
        
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertEquals(new Main1Phase(), Game.currentPhase);
        
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertEquals(new CombatBeginningPhase(), Game.currentPhase);
        
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertEquals(new DeclareAttackersPhase(), Game.currentPhase);
        
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertEquals(new DeclareBlockersPhase(), Game.currentPhase);
        
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertEquals(new CombatDamagePhase(), Game.currentPhase);
        
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertEquals(new CombatEndPhase(), Game.currentPhase);
        
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertEquals(new Main2Phase(), Game.currentPhase);
        
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertEquals(new EndOfTurnPhase(), Game.currentPhase);
        
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertEquals(new CleanupPhase(), Game.currentPhase);
        
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertEquals(new PlayerChangePhase(), Game.currentPhase);
        
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals(new UntapPhase(), Game.currentPhase);
    }
}
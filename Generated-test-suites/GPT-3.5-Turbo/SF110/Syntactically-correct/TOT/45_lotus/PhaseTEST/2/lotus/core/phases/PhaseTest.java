import lotus.core.Game;
import lotus.core.phases.*;

import org.junit.*;

public class PhaseTest {
    
    // Testing doPhase() method 
    
    // Test case for an abstract method, should throw UnsupportedOperationException
    @Test(expected = UnsupportedOperationException.class)
    public void testDoPhase() {
        Phase phase = new UntapPhase();
        phase.doPhase();
    }
    
    // Testing nextPhase() method
    
    // Test typical use case where current phase is UntapPhase 
    @Test
    public void testNextPhase_UntapPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    // Test typical use case where current phase is UpkeepPhase
    @Test
    public void testNextPhase_UpkeepPhase() {
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    // Test typical use case where current phase is DrawPhase
    @Test
    public void testNextPhase_DrawPhase() {
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof Main1Phase);
    }
    
    // Test typical use case where current phase is Main1Phase
    @Test
    public void testNextPhase_Main1Phase() {
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }
    
    // Test typical use case where current phase is CombatBeginningPhase
    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }
    
    // Test typical use case where current phase is DeclareAttackersPhase
    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }
    
    // Test typical use case where current phase is DeclareBlockersPhase
    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }
    
    // Test typical use case where current phase is CombatDamagePhase
    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }
    
    // Test typical use case where current phase is CombatEndPhase
    @Test
    public void testNextPhase_CombatEndPhase() {
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof Main2Phase);
    }
    
    // Test typical use case where current phase is Main2Phase
    @Test
    public void testNextPhase_Main2Phase() {
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }
    
    // Test typical use case where current phase is EndOfTurnPhase
    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof CleanupPhase);
    }
    
    // Test typical use case where current phase is CleanupPhase
    @Test
    public void testNextPhase_CleanupPhase() {
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }
    
    // Test typical use case where current phase is PlayerChangePhase
    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof UntapPhase);
    }
    
    // Test edge case where current phase is null
    @Test
    public void testNextPhase_NullPhase() {
        Game.currentPhase = null;
        Phase.nextPhase();
        Assert.assertTrue(Game.currentPhase instanceof UntapPhase);
    }
    
}
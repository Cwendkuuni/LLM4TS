import org.junit.*;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Create a subclass of Phase to test the abstract method doPhase()
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // Test implementation of doPhase()
            }
        };
        
        // Test that doPhase() executes without throwing an exception
        phase.doPhase();
    }
    
    @Test
    public void testNextPhase() {
        // Test nextPhase() method with different phases
        
        // Test when current phase is UntapPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase(); // Move to UpkeepPhase
        
        // Verify that the current phase is now UpkeepPhase
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Test when current phase is UpkeepPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase(); // Move to DrawPhase
        
        // Verify that the current phase is now DrawPhase
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Test when current phase is DrawPhase
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase(); // Move to Main1Phase
        
        // Verify that the current phase is now Main1Phase
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        // Test when current phase is Main1Phase
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase(); // Move to CombatBeginningPhase
        
        // Verify that the current phase is now CombatBeginningPhase
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        // Test when current phase is CombatBeginningPhase
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase(); // Move to DeclareAttackersPhase
        
        // Verify that the current phase is now DeclareAttackersPhase
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        // Test when current phase is DeclareAttackersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase(); // Move to DeclareBlockersPhase
        
        // Verify that the current phase is now DeclareBlockersPhase
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        // Test when current phase is DeclareBlockersPhase
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase(); // Move to CombatDamagePhase
        
        // Verify that the current phase is now CombatDamagePhase
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        // Test when current phase is CombatDamagePhase
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase(); // Move to CombatEndPhase
        
        // Verify that the current phase is now CombatEndPhase
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        // Test when current phase is CombatEndPhase
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase(); // Move to Main2Phase
        
        // Verify that the current phase is now Main2Phase
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        // Test when current phase is Main2Phase
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase(); // Move to EndOfTurnPhase
        
        // Verify that the current phase is now EndOfTurnPhase
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        // Test when current phase is EndOfTurnPhase
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase(); // Move to CleanupPhase
        
        // Verify that the current phase is now CleanupPhase
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        // Test when current phase is CleanupPhase
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase(); // Move to PlayerChangePhase
        
        // Verify that the current phase is now PlayerChangePhase
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        
        // Test when current phase is PlayerChangePhase
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase(); // Move to UntapPhase
        
        // Verify that the current phase is now UntapPhase
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testDoPhaseMethod() {
        // Test case for UntapPhase
        Game.currentPhase = new UntapPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for UpkeepPhase
        Game.currentPhase = new UpkeepPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for DrawPhase
        Game.currentPhase = new DrawPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for Main1Phase
        Game.currentPhase = new Main1Phase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for CombatBeginningPhase
        Game.currentPhase = new CombatBeginningPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for DeclareAttackersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for DeclareBlockersPhase
        Game.currentPhase = new DeclareBlockersPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for CombatDamagePhase
        Game.currentPhase = new CombatDamagePhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for CombatEndPhase
        Game.currentPhase = new CombatEndPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for Main2Phase
        Game.currentPhase = new Main2Phase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for EndOfTurnPhase
        Game.currentPhase = new EndOfTurnPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for CleanupPhase
        Game.currentPhase = new CleanupPhase();
        Game.currentPhase.doPhase();
        // Add asserts here
        
        // Test case for PlayerChangePhase
        Game.currentPhase = new PlayerChangePhase();
        Game.currentPhase.doPhase();
        // Add asserts here
    }
    
    @Test
    public void testNextPhaseMethod() {
        // Test case for each possible phase change in the nextPhase method
        // TODO: Implement test cases for each phase change
    }
}
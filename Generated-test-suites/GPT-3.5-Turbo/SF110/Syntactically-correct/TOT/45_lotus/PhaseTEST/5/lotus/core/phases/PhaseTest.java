import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        // Test case for Abstract method doPhase()
        // Edge case: No implementation
        // Expected: It throws UnsupportedOperationException
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(UnsupportedOperationException.class, phase::doPhase);
    }

    @Test
    public void testNextPhase() {
        // Test cases for nextPhase() method
        
        // Test case for UntapPhase
        // Typical use case: Game.currentPhase is UntapPhase
        // Expected: Game.currentPhase is changed to UpkeepPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Test case for UpkeepPhase
        // Typical use case: Game.currentPhase is UpkeepPhase
        // Expected: Game.currentPhase is changed to DrawPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Test case for DrawPhase
        // Typical use case: Game.currentPhase is DrawPhase
        // Expected: Game.currentPhase is changed to Main1Phase
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        // Test case for Main1Phase
        // Typical use case: Game.currentPhase is Main1Phase
        // Expected: Game.currentPhase is changed to CombatBeginningPhase
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        // Test case for CombatBeginningPhase
        // Typical use case: Game.currentPhase is CombatBeginningPhase
        // Expected: Game.currentPhase is changed to DeclareAttackersPhase
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        // Test case for DeclareAttackersPhase
        // Typical use case: Game.currentPhase is DeclareAttackersPhase
        // Expected: Game.currentPhase is changed to DeclareBlockersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        // Test case for DeclareBlockersPhase
        // Typical use case: Game.currentPhase is DeclareBlockersPhase
        // Expected: Game.currentPhase is changed to CombatDamagePhase
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        // Test case for CombatDamagePhase
        // Typical use case: Game.currentPhase is CombatDamagePhase
        // Expected: Game.currentPhase is changed to CombatEndPhase
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        // Test case for CombatEndPhase
        // Typical use case: Game.currentPhase is CombatEndPhase
        // Expected: Game.currentPhase is changed to Main2Phase
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        // Test case for Main2Phase
        // Typical use case: Game.currentPhase is Main2Phase
        // Expected: Game.currentPhase is changed to EndOfTurnPhase
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        // Test case for EndOfTurnPhase
        // Typical use case: Game.currentPhase is EndOfTurnPhase
        // Expected: Game.currentPhase is changed to CleanupPhase
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        // Test case for CleanupPhase
        // Typical use case: Game.currentPhase is CleanupPhase
        // Expected: Game.currentPhase is changed to PlayerChangePhase
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        
        // Test case for PlayerChangePhase
        // Typical use case: Game.currentPhase is PlayerChangePhase
        // Expected: Game.currentPhase is changed to UntapPhase and Game.playingPlayer is updated
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(2, Game.playingPlayer);
    }
}
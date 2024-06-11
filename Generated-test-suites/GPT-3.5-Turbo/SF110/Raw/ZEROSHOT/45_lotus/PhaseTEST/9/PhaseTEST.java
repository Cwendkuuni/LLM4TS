import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Before
    public void setUp() {
        // Initialize any required objects or variables before each test case
    }

    @Test
    public void testDoPhase() {
        // Test doPhase method
        // Add test cases to cover different scenarios
    }
    
    @Test
    public void testNextPhase() {
        // Test nextPhase method
        
        // Test if current phase is UntapPhase
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        
        // Test if current phase is UpkeepPhase
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
        
        // Test if current phase is DrawPhase
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
        
        // Test if current phase is Main1Phase
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
        
        // Test if current phase is CombatBeginningPhase
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
        
        // Test if current phase is DeclareAttackersPhase
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
        
        // Test if current phase is DeclareBlockersPhase
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
        
        // Test if current phase is CombatDamagePhase
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
        
        // Test if current phase is CombatEndPhase
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
        
        // Test if current phase is Main2Phase
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
        
        // Test if current phase is EndOfTurnPhase
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
        
        // Test if current phase is CleanupPhase
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
        
        // Test if current phase is PlayerChangePhase
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(2, Game.playingPlayer);
    }
}
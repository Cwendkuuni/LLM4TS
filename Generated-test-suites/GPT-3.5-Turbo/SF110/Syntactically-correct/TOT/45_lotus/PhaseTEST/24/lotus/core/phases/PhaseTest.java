import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
  
    private Phase phase;
  
    @Before
    public void setUp() throws Exception {
        phase = new PhaseImpl();
    }
    
    @Test
    public void testDoPhase() {
        // TODO: Implement test cases for doPhase()
    }
    
    @Test
    public void testNextPhase_UntapPhase() {
        // Arrange
        Game.currentPhase = new UntapPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhase_UpkeepPhase() {
        // Arrange
        Game.currentPhase = new UpkeepPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    @Test
    public void testNextPhase_DrawPhase() {
        // Arrange
        Game.currentPhase = new DrawPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }
    
    @Test
    public void testNextPhase_Main1Phase() {
        // Arrange
        Game.currentPhase = new Main1Phase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }
    
    @Test
    public void testNextPhase_CombatBeginningPhase() {
        // Arrange
        Game.currentPhase = new CombatBeginningPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        // Arrange
        Game.currentPhase = new DeclareAttackersPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        // Arrange
        Game.currentPhase = new DeclareBlockersPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }
    
    @Test
    public void testNextPhase_CombatDamagePhase() {
        // Arrange
        Game.currentPhase = new CombatDamagePhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }
    
    @Test
    public void testNextPhase_CombatEndPhase() {
        // Arrange
        Game.currentPhase = new CombatEndPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }
    
    @Test
    public void testNextPhase_Main2Phase() {
        // Arrange
        Game.currentPhase = new Main2Phase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }
    
    @Test
    public void testNextPhase_EndOfTurnPhase() {
        // Arrange
        Game.currentPhase = new EndOfTurnPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }
    
    @Test
    public void testNextPhase_CleanupPhase() {
        // Arrange
        Game.currentPhase = new CleanupPhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }
    
    @Test
    public void testNextPhase_PlayerChangePhase() {
        // Arrange
        Game.currentPhase = new PlayerChangePhase();
        
        // Act
        phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(2, Game.playingPlayer);
    }
}

class PhaseImpl extends Phase {
    @Override
    public void doPhase() {
        // TODO: Implement doPhase() test cases
    }
}
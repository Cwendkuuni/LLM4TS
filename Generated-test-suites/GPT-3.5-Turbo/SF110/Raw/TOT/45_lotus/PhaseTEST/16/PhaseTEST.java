import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
    
    @Test
    public void testDoPhase_UntapPhase() {
        // Arrange
        Phase phase = new UntapPhase();
        
        // Act
        phase.doPhase();
        
        // Assert
        // Add assertions for expected behavior during the Untap phase
    }
    
    @Test
    public void testDoPhase_UpkeepPhase() {
        // Arrange
        Phase phase = new UpkeepPhase();
        
        // Act
        phase.doPhase();
        
        // Assert
        // Add assertions for expected behavior during the Upkeep phase
    }
    
    @Test
    public void testDoPhase_DrawPhase() {
        // Arrange
        Phase phase = new DrawPhase();
        
        // Act
        phase.doPhase();
        
        // Assert
        // Add assertions for expected behavior during the Draw phase
    }
    
    // More test cases for other phases...
    
    @Test
    public void testNextPhase_UntapPhase() {
        // Arrange
        Game.currentPhase = new UntapPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        // Verify that the next phase is UpkeepPhase
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhase_UpkeepPhase() {
        // Arrange
        Game.currentPhase = new UpkeepPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        // Verify that the next phase is DrawPhase
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    // More test cases for other phases...
}
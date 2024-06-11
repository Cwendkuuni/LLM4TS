import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhaseTest {
    
    private Phase phase;
    
    @Before
    public void setUp() {
        phase = new Phase() {
            @Override
            public void doPhase() {
                // Do nothing
            }
        };
    }
    
    @Test
    public void testNextPhase_UntapPhase() {
        // Arrange
        Game.currentPhase = new UntapPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhase_UpkeepPhase() {
        // Arrange
        Game.currentPhase = new UpkeepPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    @Test
    public void testNextPhase_DrawPhase() {
        // Arrange
        Game.currentPhase = new DrawPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }
    
    @Test
    public void testNextPhase_Main1Phase() {
        // Arrange
        Game.currentPhase = new Main1Phase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }
    
    @Test
    public void testNextPhase_CombatBeginningPhase() {
        // Arrange
        Game.currentPhase = new CombatBeginningPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        // Arrange
        Game.currentPhase = new DeclareAttackersPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        // Arrange
        Game.currentPhase = new DeclareBlockersPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }
    
    @Test
    public void testNextPhase_CombatDamagePhase() {
        // Arrange
        Game.currentPhase = new CombatDamagePhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }
    
    @Test
    public void testNextPhase_CombatEndPhase() {
        // Arrange
        Game.currentPhase = new CombatEndPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }
    
    @Test
    public void testNextPhase_Main2Phase() {
        // Arrange
        Game.currentPhase = new Main2Phase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }
    
    @Test
    public void testNextPhase_EndOfTurnPhase() {
        // Arrange
        Game.currentPhase = new EndOfTurnPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }
    
    @Test
    public void testNextPhase_CleanupPhase() {
        // Arrange
        Game.currentPhase = new CleanupPhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }
    
    @Test
    public void testNextPhase_PlayerChangePhase() {
        // Arrange
        Game.currentPhase = new PlayerChangePhase();
        
        // Act
        Phase.nextPhase();
        
        // Assert
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(2, Game.playingPlayer);
    }
}
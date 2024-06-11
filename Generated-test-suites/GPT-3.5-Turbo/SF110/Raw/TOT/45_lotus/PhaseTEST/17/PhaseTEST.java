import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {
    
    private Phase phase;
    
    @Before
    public void setUp() {
        phase = new Phase() {
            @Override
            public void doPhase() {
                // implementation not necessary for testing
            }
        };
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUntapPhase_ShouldChangePhaseToUpkeepPhase() {
        Game.currentPhase = new UntapPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsUpkeepPhase_ShouldChangePhaseToDrawPhase() {
        Game.currentPhase = new UpkeepPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsDrawPhase_ShouldChangePhaseToMain1Phase() {
        Game.currentPhase = new DrawPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain1Phase_ShouldChangePhaseToCombatBeginningPhase() {
        Game.currentPhase = new Main1Phase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatBeginningPhase_ShouldChangePhaseToDeclareAttackersPhase() {
        Game.currentPhase = new CombatBeginningPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsDeclareAttackersPhase_ShouldChangePhaseToDeclareBlockersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsDeclareBlockersPhase_ShouldChangePhaseToCombatDamagePhase() {
        Game.currentPhase = new DeclareBlockersPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatDamagePhase_ShouldChangePhaseToCombatEndPhase() {
        Game.currentPhase = new CombatDamagePhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatEndPhase_ShouldChangePhaseToMain2Phase() {
        Game.currentPhase = new CombatEndPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain2Phase_ShouldChangePhaseToEndOfTurnPhase() {
        Game.currentPhase = new Main2Phase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsEndOfTurnPhase_ShouldChangePhaseToCleanupPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsCleanupPhase_ShouldChangePhaseToPlayerChangePhase() {
        Game.currentPhase = new CleanupPhase();
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }
    
    @Test
    public void testNextPhase_WhenCurrentPhaseIsPlayerChangePhase_ShouldChangePhaseToUntapPhaseAndSwitchPlayingPlayer() {
        Game.currentPhase = new PlayerChangePhase();
        Game.playingPlayer = 1;
        
        phase.nextPhase();
        
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertEquals(2, Game.playingPlayer);
    }
}
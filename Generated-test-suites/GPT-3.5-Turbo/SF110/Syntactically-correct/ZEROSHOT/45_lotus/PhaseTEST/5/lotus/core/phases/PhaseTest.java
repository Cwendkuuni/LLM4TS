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
                // Implement abstract method for testing
            }
        };
    }

    @Test
    public void testNextPhase_Untap() {
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhase_Upkeep() {
        Game.currentPhase = new UpkeepPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    @Test
    public void testNextPhase_Draw() {
        Game.currentPhase = new DrawPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }
    
    @Test
    public void testNextPhase_Main1() {
        Game.currentPhase = new Main1Phase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }
    
    @Test
    public void testNextPhase_CombatBeginning() {
        Game.currentPhase = new CombatBeginningPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareAttackers() {
        Game.currentPhase = new DeclareAttackersPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareBlockers() {
        Game.currentPhase = new DeclareBlockersPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }
    
    @Test
    public void testNextPhase_CombatDamage() {
        Game.currentPhase = new CombatDamagePhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }
    
    @Test
    public void testNextPhase_CombatEnd() {
        Game.currentPhase = new CombatEndPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }
    
    @Test
    public void testNextPhase_Main2() {
        Game.currentPhase = new Main2Phase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }
    
    @Test
    public void testNextPhase_EndOfTurn() {
        Game.currentPhase = new EndOfTurnPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }
    
    @Test
    public void testNextPhase_Cleanup() {
        Game.currentPhase = new CleanupPhase();
        phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }
    
    @Test
    public void testNextPhase_PlayerChange() {
        Game.currentPhase = new PlayerChangePhase();
        phase.nextPhase();
        assertEquals(2, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

}
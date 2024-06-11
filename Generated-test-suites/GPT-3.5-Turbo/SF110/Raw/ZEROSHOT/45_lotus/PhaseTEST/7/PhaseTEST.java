import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
    }
    
    @Test
    public void testNextPhase_UpkeepPhase() {
        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DrawPhase);
    }
    
    @Test
    public void testNextPhase_DrawPhase() {
        Game.currentPhase = new DrawPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main1Phase);
    }
    
    @Test
    public void testNextPhase_Main1Phase() {
        Game.currentPhase = new Main1Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);
    }
    
    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game.currentPhase = new CombatBeginningPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);
    }
    
    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game.currentPhase = new DeclareBlockersPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);
    }
    
    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game.currentPhase = new CombatDamagePhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CombatEndPhase);
    }
    
    @Test
    public void testNextPhase_CombatEndPhase() {
        Game.currentPhase = new CombatEndPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof Main2Phase);
    }
    
    @Test
    public void testNextPhase_Main2Phase() {
        Game.currentPhase = new Main2Phase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);
    }
    
    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof CleanupPhase);
    }
    
    @Test
    public void testNextPhase_CleanupPhase() {
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);
    }
    
    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game.currentPhase = new PlayerChangePhase();
        Phase.nextPhase();
        assertEquals(Game.playingPlayer, 2);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }
}
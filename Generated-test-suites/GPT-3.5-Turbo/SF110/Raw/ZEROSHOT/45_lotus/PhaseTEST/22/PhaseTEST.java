import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PhaseTest {
    
    @Test
    public void testNextPhase() {
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), UpkeepPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), DrawPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), Main1Phase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), CombatBeginningPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), DeclareAttackersPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), DeclareBlockersPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), CombatDamagePhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), CombatEndPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), Main2Phase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), EndOfTurnPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), CleanupPhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), PlayerChangePhase.class);
        Phase.nextPhase();
        assertEquals(Game.currentPhase.getClass(), UntapPhase.class);
        assertEquals(Game.playingPlayer, 2);
    }
}
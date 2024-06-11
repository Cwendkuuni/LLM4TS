import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // write test logic here
            }
        };
        
        // write assertions here
    }

    @Test
    public void testNextPhase() {
        Phase nextPhase = Game.currentPhase;
        
        if (nextPhase instanceof UntapPhase) {
            changePhase(new UpkeepPhase());
            assertEquals(Game.currentPhase, new UpkeepPhase());
        }
        else if (nextPhase instanceof UpkeepPhase) {
            changePhase(new DrawPhase());
            assertEquals(Game.currentPhase, new DrawPhase());
        }
        else if (nextPhase instanceof DrawPhase) {
            changePhase(new Main1Phase());
            assertEquals(Game.currentPhase, new Main1Phase());
        }
        else if (nextPhase instanceof Main1Phase) {
            changePhase(new CombatBeginningPhase());
            assertEquals(Game.currentPhase, new CombatBeginningPhase());
        }
        else if (nextPhase instanceof CombatBeginningPhase) {
            changePhase(new DeclareAttackersPhase());
            assertEquals(Game.currentPhase, new DeclareAttackersPhase());
        }
        else if (nextPhase instanceof DeclareAttackersPhase) {
            changePhase(new DeclareBlockersPhase());
            assertEquals(Game.currentPhase, new DeclareBlockersPhase());
        }
        else if (nextPhase instanceof DeclareBlockersPhase) {
            changePhase(new CombatDamagePhase());
            assertEquals(Game.currentPhase, new CombatDamagePhase());
        }
        else if (nextPhase instanceof CombatDamagePhase) {
            changePhase(new CombatEndPhase());
            assertEquals(Game.currentPhase, new CombatEndPhase());
        }
        else if (nextPhase instanceof CombatEndPhase) {
            changePhase(new Main2Phase());
            assertEquals(Game.currentPhase, new Main2Phase());
        }
        else if (nextPhase instanceof Main2Phase) {
            changePhase(new EndOfTurnPhase());
            assertEquals(Game.currentPhase, new EndOfTurnPhase());
        }
        else if (nextPhase instanceof EndOfTurnPhase) {
            changePhase(new CleanupPhase());
            assertEquals(Game.currentPhase, new CleanupPhase());
        }
        else if (nextPhase instanceof CleanupPhase) {
            changePhase(new PlayerChangePhase());
            assertEquals(Game.currentPhase, new PlayerChangePhase());
            assertEquals(Game.playingPlayer, Game.playingPlayer % 2 + 1);
        }
    }
}
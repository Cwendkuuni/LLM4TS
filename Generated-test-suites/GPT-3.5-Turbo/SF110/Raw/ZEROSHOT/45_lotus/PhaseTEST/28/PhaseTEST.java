import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        // Set up
        Game.currentPhase = new UntapPhase();
        // Expected values
        Phase expectedPhase = new UpkeepPhase();
        int expectedPlayer = Game.playingPlayer;
        // Call method
        Phase.nextPhase();
        // Assert
        assertEquals(expectedPhase, Game.currentPhase);
        assertEquals(expectedPlayer, Game.playingPlayer);
    }
    
    @Test
    public void testNextPhase_UpkeepPhase() {
        // Set up
        Game.currentPhase = new UpkeepPhase();
        // Expected values
        Phase expectedPhase = new DrawPhase();
        int expectedPlayer = Game.playingPlayer;
        // Call method
        Phase.nextPhase();
        // Assert
        assertEquals(expectedPhase, Game.currentPhase);
        assertEquals(expectedPlayer, Game.playingPlayer);
    }
    
    @Test
    public void testNextPhase_DrawPhase() {
        // Set up
        Game.currentPhase = new DrawPhase();
        // Expected values
        Phase expectedPhase = new Main1Phase();
        int expectedPlayer = Game.playingPlayer;
        // Call method
        Phase.nextPhase();
        // Assert
        assertEquals(expectedPhase, Game.currentPhase);
        assertEquals(expectedPlayer, Game.playingPlayer);
    }
    
    // ... continue for the remaining phases
    
}
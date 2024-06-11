import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GameTest {
  
    @Test
    public void testGetPlayingPlayer() {
        Player player = Game.getPlayingPlayer();
        assertNotNull(player);
    }
  
    @Test
    public void testGetNonPlayingPlayer() {
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        assertNotNull(nonPlayingPlayer);
    }
  
    @Test
    public void testGetOtherPlayer() {
        Player otherPlayer = Game.getOtherPlayer(Game.player1);
        assertNotNull(otherPlayer);
    }
  
    @Test
    public void testInit() {
        // Implement test case for init method
    }
  
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Implement test case for givePriorityToCurrentPlayer method
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player player = Game.getPlayingPlayer();
        assertEquals(Game.player1, player);

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        player = Game.getPlayingPlayer();
        assertEquals(Game.player2, player);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player player = Game.getNonPlayingPlayer();
        assertEquals(Game.player2, player);

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        player = Game.getNonPlayingPlayer();
        assertEquals(Game.player1, player);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test when p is player1
        Player p = Game.player1;
        Player player = Game.getOtherPlayer(p);
        assertEquals(Game.player2, player);

        // Test when p is player2
        p = Game.player2;
        player = Game.getOtherPlayer(p);
        assertEquals(Game.player1, player);
    }
    
    @Test
    public void testInit() {
        // Add test cases for init method
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Add test cases for givePriorityToCurrentPlayer method
    }
}
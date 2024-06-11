import static org.junit.Assert.*;
import org.junit.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test if playing player is player1
        Game.player1 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test if playing player is player2
        Game.player2 = new Player();
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test if non-playing player is player2 when current playing player is player1
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test if non-playing player is player1 when current playing player is player2
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test if other player is player2 when input player is player1
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test if other player is player1 when input player is player2
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // TODO: Implement test case for init() method
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Implement test case for givePriorityToCurrentPlayer() method
    }
}
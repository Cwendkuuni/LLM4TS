import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(Game.getPlayingPlayer(), player1);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(Game.getNonPlayingPlayer(), player2);
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        assertEquals(Game.getOtherPlayer(player1), player2);
        assertEquals(Game.getOtherPlayer(player2), player1);
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(Game.player1.getName(), n1);
        assertEquals(Game.player2.getName(), n2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        // Add test case for this method
    }
}
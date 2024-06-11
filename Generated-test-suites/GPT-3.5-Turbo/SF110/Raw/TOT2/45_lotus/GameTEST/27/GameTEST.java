import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    // Test for getPlayingPlayer method
    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    // Test for getNonPlayingPlayer method
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    // Test for getOtherPlayer method
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    // Test for init method
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(Game.player1, Game.getPlayingPlayer());
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }

    // Test for givePriorityToCurrentPlayer method
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Add test case for givePriorityToCurrentPlayer method 
    }
}
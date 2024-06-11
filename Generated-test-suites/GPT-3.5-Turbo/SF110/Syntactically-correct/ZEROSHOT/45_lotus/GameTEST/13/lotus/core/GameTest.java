import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test if playing player is player1
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test if playing player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test if non-playing player is player1
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());

        // Test if non-playing player is player2
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test if other player is player1
        Game.player1 = new Player();
        Game.player2 = new Player();
        Player p = new Player();
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));

        // Test if other player is player2
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
    }

    @Test
    public void testInit() {
        // Test if init method initializes players correctly
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test if priority is given to current player correctly
        Game.stack = new Stack();
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;

        // Test when current player can speak but non-playing player cannot speak
        Game.getPlayingPlayer().setCanSpeak(true);
        Game.getNonPlayingPlayer().setCanSpeak(false);
        Game.givePriorityToCurrentPlayer();
        assertFalse(Game.getPlayingPlayer().canSpeak());

        // Test when non-playing player can speak but current player cannot speak
        Game.getPlayingPlayer().setCanSpeak(false);
        Game.getNonPlayingPlayer().setCanSpeak(true);
        Game.givePriorityToCurrentPlayer();
        assertFalse(Game.getNonPlayingPlayer().canSpeak());
    }
}
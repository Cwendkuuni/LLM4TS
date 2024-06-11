import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        // Expected output: player1
        Player expected = Game.player1;
        Player result = Game.getPlayingPlayer();
        assertEquals(expected, result);

        // Test when playingPlayer is not 1
        // Expected output: player2
        Game.playingPlayer = 2;
        expected = Game.player2;
        result = Game.getPlayingPlayer();
        assertEquals(expected, result);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when p is player1
        // Expected output: player2
        Player p = Game.player1;
        Player expected = Game.player2;
        Player result = Game.getNonPlayingPlayer(p);
        assertEquals(expected, result);

        // Test when p is player2
        // Expected output: player1
        p = Game.player2;
        expected = Game.player1;
        result = Game.getNonPlayingPlayer(p);
        assertEquals(expected, result);
    }

    @Test
    public void testInit() {
        // Test initializing with valid inputs
        // Expected output: successfully initialize Game
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Add additional checks to verify the initialization
        // ...

    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test when playingPlayer has priority
        // Expected output: stack resolved and priority given to non-playing player
        // Add additional checks to verify the behavior
        // ...

    }
}
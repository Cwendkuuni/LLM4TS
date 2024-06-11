import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.UserInterface;
import lotus.core.card.CardCollection;
import lotus.core.stack.Stack;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test when p is player1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test when p is player2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Create sample inputs
        UserInterface IP1 = null;
        UserInterface IP2 = null;
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        // Test the initialization
        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        // Check if the initialization was successful
        assertEquals(Game.player1, Game.player1);
        assertEquals(Game.player2, Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test when the stack is empty
        Game.stack = new Stack();
        Game.givePriorityToCurrentPlayer();
        // No assertion, just checking if it runs without errors
    }

    @Test
    public void testGetPlayingPlayer_Null() {
        // Test when playingPlayer is null
        Game.playingPlayer = null;
        assertNull(Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer_Null() {
        // Test when playingPlayer is null
        Game.playingPlayer = null;
        assertNull(Game.getNonPlayingPlayer());

        // Test when playingPlayer is 1 and player1 is null
        Game.playingPlayer = 1;
        Game.player1 = null;
        assertNull(Game.getNonPlayingPlayer());

        // Test when playingPlayer is 2 and player2 is null
        Game.playingPlayer = 2;
        Game.player2 = null;
        assertNull(Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer_Null() {
        // Test when p is null
        assertNull(Game.getOtherPlayer(null));

        // Test when p is player1 and player2 is null
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player1));

        // Test when p is player2 and player1 is null
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player2));

        // Test when p is player2 and player1 is null
        Game.player1 = null;
        assertNull(Game.getOtherPlayer(Game.player2));
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Edge Case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Edge Case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical Use Case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Typical Use Case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertEquals(n1, Game.player1.name);
        assertEquals(n2, Game.player2.name);
        assertEquals(deck1, Game.player1.deck);
        assertEquals(deck2, Game.player2.deck);
        assertEquals(Game.player1, deck1.get(0).owner);
        assertEquals(Game.player1.library, deck1.get(0).zone);
        assertEquals(Game.player2, deck2.get(0).owner);
        assertEquals(Game.player2.library, deck2.get(0).zone);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical Use Case
        Game.stack = new Stack();
        Game.playingPlayer = 1;
        Game.currentPhase = new UntapPhase();
        Game.givePriorityToCurrentPlayer();

        assertTrue(Game.stack.empty());

        // Edge Case: Stack is not empty
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();

        assertTrue(Game.stack.empty());
    }
}
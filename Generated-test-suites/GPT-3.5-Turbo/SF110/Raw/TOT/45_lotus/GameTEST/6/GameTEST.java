import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private UserInterface IP1;
    private UserInterface IP2;
    private String n1;
    private String n2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setUp() {
        IP1 = new MockUserInterface();
        IP2 = new MockUserInterface();
        n1 = "Player 1";
        n2 = "Player 2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Player expected = Game.player1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Player expected = Game.player2;
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOtherPlayer() {
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Player expected = Game.player2;
        Player actual = Game.getOtherPlayer(Game.player1);
        assertEquals(expected, actual);
    }

    @Test
    public void testInit() {
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        // Test if player1 and player2 are initialized correctly
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        // Test if player1 and player2 initalized with correct interfaces
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(IP2, Game.player2.getInterface());
        // Test if player1 and player2 decks are set correctly
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        // Test if the priority is given to the current player correctly
        Game.givePriorityToCurrentPlayer();
        assertEquals(Game.getPlayingPlayer(), Game.stack.getLastResolved());
    }
}
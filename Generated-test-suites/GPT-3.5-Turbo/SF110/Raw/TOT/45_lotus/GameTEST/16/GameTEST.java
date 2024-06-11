
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    @Before
    public void setUp() {
        Game.combat = null;
        Game.currentPhase = null;
        Game.playingPlayer = 0;
        Game.player1 = null;
        Game.player2 = null;
        Game.stack = null;
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player(null, "Player1", null);
        Game.player2 = new Player(null, "Player2", null);
        Game.playingPlayer = 1;
        Player expected = Game.player1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
        
        Game.playingPlayer = 2;
        expected = Game.player2;
        actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(null, "Player1", null);
        Game.player2 = new Player(null, "Player2", null);
        Game.playingPlayer = 1;
        Player expected = Game.player2;
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
        
        Game.playingPlayer = 2;
        expected = Game.player1;
        actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(null, "Player1", null);
        Game.player2 = new Player(null, "Player2", null);
        Player player = Game.player1;
        Player expected = Game.player2;
        Player actual = Game.getOtherPlayer(player);
        assertEquals(expected, actual);
        
        player = Game.player2;
        expected = Game.player1;
        actual = Game.getOtherPlayer(player);
        assertEquals(expected, actual);
    }

    @Test
    public void testInit() {
        UserInterface IP1 = null;
        UserInterface IP2 = null;
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = null;
        CardCollection deck2 = null;
        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertEquals(n1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());
        assertNotNull(IP1);
        assertNotNull(IP2);

        // Test initializing with non-null values
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertEquals(n1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());
        assertNotNull(IP1);
        assertNotNull(IP2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = new Stack();

        // Test with an empty stack
        Game.givePriorityToCurrentPlayer();

        // Test with a stack containing one resolve
        Game.stack.push(new Resolve());
        // Assuming letPlayerSpeak() returns false when a player can speak and true when they cannot
        Game.player1 = new Player(null, "Player1", null) {
            public boolean letPlayerSpeak() {
                return true;
            }
        };
        Game.player2 = new Player(null, "Player2", null) {
            public boolean letPlayerSpeak() {
                return false;
            }
        };
        Game.givePriorityToCurrentPlayer();

        // Test with a stack containing multiple resolves
        Game.stack.push(new Resolve());
        Game.stack.push(new Resolve());
        Game.stack.push(new Resolve());
        Game.player1 = new Player(null, "Player1", null) {
            public boolean letPlayerSpeak() {
                return false;
            }
        };
        Game.player2 = new Player(null, "Player2", null) {
            public boolean letPlayerSpeak() {
                return false;
            }
        };
        Game.givePriorityToCurrentPlayer();
    }
}


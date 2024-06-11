package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import lotus.core.interfaces.UserInterface;
import lotus.core.card.CardCollection;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private UserInterface userInterface1;
    private UserInterface userInterface2;
    private String name1;
    private String name2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setUp() {
        userInterface1 = new UserInterface();
        userInterface2 = new UserInterface();
        name1 = "Player1";
        name2 = "Player2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }

    @Test
    public void testGetPlayingPlayer() {
        assertNull(Game.player1);
        assertNull(Game.player2);
        assertNull(Game.getPlayingPlayer());

        Game.player1 = new Player(userInterface1, name1, deck1);
        Game.player2 = new Player(userInterface2, name2, deck2);
        assertEquals(Game.player1, Game.getPlayingPlayer());

        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertNull(Game.player1);
        assertNull(Game.player2);
        assertNull(Game.getNonPlayingPlayer());

        Game.player1 = new Player(userInterface1, name1, deck1);
        Game.player2 = new Player(userInterface2, name2, deck2);
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertNull(Game.player1);
        assertNull(Game.player2);
        assertNull(Game.getOtherPlayer(Game.player1));
        assertNull(Game.getOtherPlayer(Game.player2));

        Game.player1 = new Player(userInterface1, name1, deck1);
        Game.player2 = new Player(userInterface2, name2, deck2);
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        assertNull(Game.player1);
        assertNull(Game.player2);
        assertNull(Game.currentPhase);
        assertNull(Game.stack);

        Game.init(userInterface1, userInterface2, name1, name2, deck1, deck2);

        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.currentPhase);
        assertNotNull(Game.stack);
        assertEquals(userInterface1,Game.player1.getUserInterface());
        assertEquals(userInterface2,Game.player2.getUserInterface());
        assertEquals(name1,Game.player1.getName());
        assertEquals(name2,Game.player2.getName());
        assertEquals(deck1,Game.player1.getDeck());
        assertEquals(deck2,Game.player2.getDeck());
        assertEquals(Game.player1,Game.getPlayingPlayer());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        assertNull(Game.stack);

        Game.stack = new Stack();
        Game.givePriorityToCurrentPlayer();

        assertTrue(Game.stack.empty());

        Game.stack.push(new Card());
        Game.stack.resolveLast();

        Game.givePriorityToCurrentPlayer();

        assertTrue(Game.stack.empty());
    }
}
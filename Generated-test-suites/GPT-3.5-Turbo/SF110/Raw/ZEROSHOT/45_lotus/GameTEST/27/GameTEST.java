import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player expected = Game.player2;
        Game.playingPlayer = 2;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPlayingPlayerWhenPlayingPlayerIs1() {
        Player expected = Game.player1;
        Game.playingPlayer = 1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player = new Player(new UserInterface(), "TestPlayer1", new CardCollection());
        Player expected = player;
        Game.player1 = player;
        Game.player2 = new Player(new UserInterface(), "TestPlayer2", new CardCollection());
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOtherPlayer() {
        Player player = new Player(new UserInterface(), "TestPlayer2", new CardCollection());
        Player expected = player;
        Game.player1 = new Player(new UserInterface(), "TestPlayer1", new CardCollection());
        Game.player2 = player;
        Player actual = Game.getOtherPlayer(Game.player1);
        assertEquals(expected, actual);
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "TestPlayer1";
        String n2 = "TestPlayer2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.stack);
        assertEquals(IP1, Game.player1.userInterface);
        assertEquals(IP2, Game.player2.userInterface);
        assertEquals(n1, Game.player1.name);
        assertEquals(n2, Game.player2.name);
        assertEquals(deck1, Game.player1.deck);
        assertEquals(deck2, Game.player2.deck);
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);

        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Player player1 = new Player(new UserInterface(), "TestPlayer1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "TestPlayer2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;

        Game.stack.push(new Card()); // Add a card to the stack

        Game.playingPlayer = 1; // Set current playing player

        Game.givePriorityToCurrentPlayer();

        assertEquals(player1, Game.stack.getLastResolvedBy());
    }
}
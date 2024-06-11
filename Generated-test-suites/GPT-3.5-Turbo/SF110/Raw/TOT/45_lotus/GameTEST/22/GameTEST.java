import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        Game.playingPlayer = 1;
        assertEquals(p1, Game.getPlayingPlayer());

        // Edge case: playing player is 2
        Game.playingPlayer = 2;
        assertEquals(p2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        Game.playingPlayer = 1;
        assertEquals(p2, Game.getNonPlayingPlayer());

        // Edge case: playing player is 2
        Game.playingPlayer = 2;
        assertEquals(p1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        assertEquals(p2, Game.getOtherPlayer(p1));
        assertEquals(p1, Game.getOtherPlayer(p2));

        // Edge case: unknown player
        Player p3 = new Player();
        assertNull(Game.getOtherPlayer(p3));
    }

    @Test
    public void testInit() {
        // Typical use case
        UserInterface ui1 = new UserInterface();
        UserInterface ui2 = new UserInterface();
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ui1, ui2, name1, name2, deck1, deck2);
        assertEquals(ui1, Game.player1.userInterface);
        assertEquals(ui2, Game.player2.userInterface);
        assertEquals(name1, Game.player1.name);
        assertEquals(name2, Game.player2.name);
        assertEquals(deck1, Game.player1.deck);
        assertEquals(deck2, Game.player2.deck);
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());

        // Edge case: empty decks
        CardCollection deck3 = new CardCollection();
        CardCollection deck4 = new CardCollection();
        Game.init(ui1, ui2, name1, name2, deck3, deck4);
        assertEquals(deck3, Game.player1.deck);
        assertEquals(deck4, Game.player2.deck);

        // Edge case: decks with only one card
        CardCollection deck5 = new CardCollection();
        deck5.add(new Card());
        CardCollection deck6 = new CardCollection();
        deck6.add(new Card());
        Game.init(ui1, ui2, name1, name2, deck5, deck6);
        assertEquals(deck5, Game.player1.deck);
        assertEquals(deck6, Game.player2.deck);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        Game.playingPlayer = 1;
        Game.stack.push(new Card());
        
        Game.givePriorityToCurrentPlayer(); // Resolves the last card

        // Edge case: stack is empty
        Game.stack = new Stack();
        Game.givePriorityToCurrentPlayer(); // Should not throw any error
    }
}
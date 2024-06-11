import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        // Testing initialization of players and decks
        UserInterface ui1 = new UserInterface();
        UserInterface ui2 = new UserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(ui1, ui2, name1, name2, deck1, deck2);
        
        assertEquals(name1, Game.player1.getName());
        assertEquals(name2, Game.player2.getName());
        assertEquals(ui1, Game.player1.getInterface());
        assertEquals(ui2, Game.player2.getInterface());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Edge case where player can speak and stack is not empty
        Game.playingPlayer = 1;
        Game.stack.push(new Card());
        // Assuming letPlayerSpeak() returns true for testing purposes
        assertTrue(Game.getPlayingPlayer().letPlayerSpeak());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
}
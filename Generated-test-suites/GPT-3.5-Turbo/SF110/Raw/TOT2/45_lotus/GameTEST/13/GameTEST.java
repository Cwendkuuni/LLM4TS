import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        Game.playingPlayer = 1;
        assertEquals(Game.getPlayingPlayer(), p1);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        Game.playingPlayer = 1;
        assertEquals(Game.getNonPlayingPlayer(), p2);
    }

    @Test
    public void testGetOtherPlayer() {
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        assertEquals(Game.getOtherPlayer(p1), p2);
        assertEquals(Game.getOtherPlayer(p2), p1);
    }

    @Test
    public void testInit() {
        UserInterface ui1 = new UserInterface();
        UserInterface ui2 = new UserInterface();
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ui1, ui2, name1, name2, deck1, deck2);
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(Game.playingPlayer, 1);
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Player p1 = new Player();
        Game.player1 = p1;
        Game.playingPlayer = 1;
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
    
}
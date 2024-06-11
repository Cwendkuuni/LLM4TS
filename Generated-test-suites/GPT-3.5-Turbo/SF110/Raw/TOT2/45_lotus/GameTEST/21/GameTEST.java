import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        
        assertEquals(Game.player1, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();

        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test givePriorityToCurrentPlayer functionality
    }

}
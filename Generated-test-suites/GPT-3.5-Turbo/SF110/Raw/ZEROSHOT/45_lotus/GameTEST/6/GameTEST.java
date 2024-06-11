import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.card.CardCollection;

public class GameTest {

    @Before
    public void setUp() {
        UserInterface ui1 = new UserInterface();
        UserInterface ui2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ui1, ui2, "Player1", "Player2", deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        assertNotNull(Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertNotNull(Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertNotNull(Game.getOtherPlayer(Game.player1));
        assertNotNull(Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.givePriorityToCurrentPlayer();
    }

    // Add more test methods for other methods in Game class
    
}
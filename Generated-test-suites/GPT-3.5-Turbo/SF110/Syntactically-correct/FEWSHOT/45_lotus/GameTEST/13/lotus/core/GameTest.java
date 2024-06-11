import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
    @Before
    public void setUp() {
        // Initialize Game variables here if needed
    }

    @Test
    public void testGetPlayingPlayer() {
        assertNotNull(Game.getPlayingPlayer());
        // Add more assertions here
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertNotNull(Game.getNonPlayingPlayer());
        // Add more assertions here
    }

    @Test
    public void testGetOtherPlayer() {
        assertNotNull(Game.getOtherPlayer(Game.player1));
        // Add more assertions here
    }

    @Test
    public void testInit() {
        // Initialize required parameters for init() method
        UserInterface IP1 = null;
        UserInterface IP2 = null;
        String n1 = null;
        String n2 = null;
        CardCollection deck1 = null;
        CardCollection deck2 = null;

        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        // Add assertions to test the initialization of Game variables and objects created
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        assertNotNull(Game.stack);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Add assertions to test the behavior of givePriorityToCurrentPlayer() method
    }
}
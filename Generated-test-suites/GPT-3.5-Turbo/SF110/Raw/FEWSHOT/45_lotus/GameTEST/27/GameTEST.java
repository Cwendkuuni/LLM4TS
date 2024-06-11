package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;

public class GameTest {

    @Before
    public void setup() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        Player player = Game.getPlayingPlayer();
        assertNotNull(player);
        assertEquals("Player1", player.getName());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player = Game.getNonPlayingPlayer();
        assertNotNull(player);
        assertEquals("Player2", player.getName());
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = Game.player1;
        Player player2 = Game.player2;

        Player otherPlayer1 = Game.getOtherPlayer(player1);
        assertNotNull(otherPlayer1);
        assertEquals(player2, otherPlayer1);

        Player otherPlayer2 = Game.getOtherPlayer(player2);
        assertNotNull(otherPlayer2);
        assertEquals(player1, otherPlayer2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
}
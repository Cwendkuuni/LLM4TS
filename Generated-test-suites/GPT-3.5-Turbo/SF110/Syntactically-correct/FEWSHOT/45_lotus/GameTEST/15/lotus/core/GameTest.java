import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;
import lotus.core.Player;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // Test with Game.playingPlayer = 1
        Game.playingPlayer = 1;
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Game.player1 = player1;
        Game.player2 = new Player(new UserInterface(), "Player2", new CardCollection());

        assertEquals(player1, Game.getPlayingPlayer());

        // Test with Game.playingPlayer = 2
        Game.playingPlayer = 2;
        Player player2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Game.player2 = player2;

        assertEquals(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test with Game.playingPlayer = 1
        Game.playingPlayer = 1;
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Game.player1 = player1;
        Game.player2 = new Player(new UserInterface(), "Player2", new CardCollection());

        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test with Game.playingPlayer = 2
        Game.playingPlayer = 2;
        Player player2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Game.player2 = player2;

        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test with Game.player1 as input
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Game.player1 = player1;
        Game.player2 = new Player(new UserInterface(), "Player2", new CardCollection());

        assertEquals(Game.player2, Game.getOtherPlayer(player1));

        // Test with Game.player2 as input
        Player player2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Game.player2 = player2;

        assertEquals(Game.player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());

        // Test if the owner and zone of each card in deck1 is correctly set
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }

        // Test if the owner and zone of each card in deck2 is correctly set
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Test this method

        // Assuming Game.stack is correctly initialized,
        // write test cases to test the logic inside this method.
    }
}
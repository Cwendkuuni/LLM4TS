import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test player 1 is playing
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test player 2 is playing
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test player 1 is playing
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test player 2 is playing
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test player 1 is passed
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

        // Test player 2 is passed
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        // Verify player1 and player2 initializations
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());

        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());

        // Verify playingPlayer and currentPhase initializations
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());

        // Verify cards in deck1 are assigned to player1 and library zone
        for(Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }

        // Verify cards in deck2 are assigned to player2 and library zone
        for(Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Write test case for givePriorityToCurrentPlayer() method
    }
}
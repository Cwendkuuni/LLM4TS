import org.junit.*;
import static org.junit.Assert.*;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import lotus.core.*;

public class GameTest {
    
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Game.player1 = player1;
        Player expected = player1;
        assertEquals(expected, Game.getPlayingPlayer());

        // Edge Case
        Game.playingPlayer = 2;
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player2 = player2;
        expected = player2;
        assertEquals(expected, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical Use Case
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Game.player1 = player1;
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player2 = player2;
        Game.playingPlayer = 1;
        Player expected = player2;
        assertEquals(expected, Game.getNonPlayingPlayer());

        // Edge Case
        Game.playingPlayer = 2;
        expected = player1;
        assertEquals(expected, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical Use Case
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Game.player1 = player1;
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player2 = player2;
        Player expected = player2;
        assertEquals(expected, Game.getOtherPlayer(player1));

        // Error Scenario
        assertNull(Game.getOtherPlayer(null));
    }
    
    @Test
    public void testInit() {
        // Typical Use Case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Player expectedPlayer1 = new Player(IP1, n1, deck1);
        Player expectedPlayer2 = new Player(IP2, n2, deck2);
        expectedPlayer1.init();
        expectedPlayer2.init();
        assertEquals(expectedPlayer1, Game.player1);
        assertEquals(expectedPlayer2, Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        
        // Error Scenario
        UserInterface nullUI = null;
        String nullName = null;
        CardCollection nullDeck = null;
        exit.expectSystemExitWithStatus(1);
        Game.init(IP1, nullUI, n1, n2, deck1, nullDeck);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical Use Case
        Game.stack.push(new Card());
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Game.player1 = player1;
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player2 = player2;
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Error Scenario
        Game.stack.push(new Card());
        exit.expectSystemExitWithStatus(1);
        Game.givePriorityToCurrentPlayer();
    }
}
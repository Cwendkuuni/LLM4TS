import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.player2 = new Player();
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
        
        // Edge case
        Player player3 = new Player();
        assertEquals(player3, Game.getOtherPlayer(player3));
    }

    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(IP2, Game.player2.getInterface());
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        assertEquals(n1, Game.player1.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(Game.player1, deck1.get(0).getOwner());
        assertEquals(Game.player1.library, deck1.get(0).getZone());
        assertEquals(Game.player2, deck2.get(0).getOwner());
        assertEquals(Game.player2.library, deck2.get(0).getZone());
        
        // Error scenario
        UserInterface IP3 = new UserInterface();
        String n3 = "Player3";
        CardCollection deck3 = new CardCollection();
        Game.init(IP3, null, n3, n2, deck3, deck2);
        assertNotNull(Game.player1);
        assertNull(Game.player2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        Game.stack = new Stack();
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(player1, Game.getPlayingPlayer());
        assertTrue(Game.stack.empty());
        player1.setSpeaking(false);
        player2.setSpeaking(true);
        Game.givePriorityToCurrentPlayer();
        assertTrue(player1.isSpeaking());
        
        // Error scenario
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(player1.isSpeaking());
        assertFalse(player2.isSpeaking());
    }
}
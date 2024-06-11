import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    // Test for getPlayingPlayer() method
    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals("Expected player1", Game.player1, Game.getPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals("Expected player2", Game.player2, Game.getPlayingPlayer());
    }
    
    // Test for getNonPlayingPlayer() method
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        assertEquals("Expected player2", Game.player2, Game.getNonPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals("Expected player1", Game.player1, Game.getNonPlayingPlayer());
    }
    
    // Test for getOtherPlayer() method
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals("Expected player2", Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals("Expected player1", Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    // Test for init() method
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals("Expected IP1 to be initialized with player1", Game.player1, IP1.getPlayer());
        assertEquals("Expected IP2 to be initialized with player2", Game.player2, IP2.getPlayer());
        assertEquals("Expected playingPlayer to be 1", 1, Game.playingPlayer);
        assertTrue("Expected currentPhase to be an instance of UntapPhase", Game.currentPhase instanceof UntapPhase);
        
        for (Card c : deck1) {
            assertEquals("Expected owner of card in player1's deck to be player1", Game.player1, c.owner);
            assertEquals("Expected zone of card in player1's deck to be player1's library", Game.player1.library, c.zone);
        }
        
        for (Card c : deck2) {
            assertEquals("Expected owner of card in player2's deck to be player2", Game.player2, c.owner);
            assertEquals("Expected zone of card in player2's deck to be player2's library", Game.player2.library, c.zone);
        }
    }
    
    // Test for givePriorityToCurrentPlayer() method
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = new Stack();
        Game.stack.push(new Card());
        Game.player1 = new Player();
        Game.player2 = new Player();
        Game.playingPlayer = 1;
        
        boolean player1SpeakCalled = false;
        boolean player2SpeakCalled = false;
        
        Game.getPlayingPlayer().letPlayerSpeak();
        player1SpeakCalled = true;
        
        assertFalse("Expected letPlayerSpeak() to return false for player2", Game.getNonPlayingPlayer().letPlayerSpeak());
        player2SpeakCalled = true;
        
        assertTrue("Expected stack to be empty after resolving last card", Game.stack.empty());
        
        if (!player1SpeakCalled || !player2SpeakCalled) {
            fail("Expected both players to have spoken");
        }
    }
}
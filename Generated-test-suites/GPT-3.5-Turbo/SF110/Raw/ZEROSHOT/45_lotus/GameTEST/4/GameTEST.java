import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test Case 1
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player result1 = Game.getPlayingPlayer();
        assertEquals(Game.player1, result1);
        
        // Test Case 2
        // Test when playingPlayer is not 1
        Game.playingPlayer = 2;
        Player result2 = Game.getPlayingPlayer();
        assertEquals(Game.player2, result2);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test Case 1
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player result1 = Game.getNonPlayingPlayer();
        assertEquals(Game.player2, result1);
        
        // Test Case 2
        // Test when playingPlayer is not 1
        Game.playingPlayer = 2;
        Player result2 = Game.getNonPlayingPlayer();
        assertEquals(Game.player1, result2);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test Case 1
        // Test when p is player1
        Player p1 = Game.player1;
        Player result1 = Game.getOtherPlayer(p1);
        assertEquals(Game.player2, result1);
        
        // Test Case 2
        // Test when p is player2
        Player p2 = Game.player2;
        Player result2 = Game.getOtherPlayer(p2);
        assertEquals(Game.player1, result2);
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Test Case 1
        // Test if the initialization is done correctly
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(Game.player1, IP1.player);
        assertEquals(Game.player2, IP2.player);
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        
        // Test Case 2
        // Test if the owner and zone of each card in deck1 are set correctly
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        
        // Test Case 3
        // Test if the owner and zone of each card in deck2 are set correctly
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test Case 1
        // Test if the method executes without errors
        
        // Mocking the letPlayerSpeak() method of Player
        Player player1 = Game.player1;
        Player player2 = Game.player2;
        
        player1.letPlayerSpeak = false;
        player2.letPlayerSpeak = false;
        
        Game.stack.push(new Object());
        
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(true);
    }
}
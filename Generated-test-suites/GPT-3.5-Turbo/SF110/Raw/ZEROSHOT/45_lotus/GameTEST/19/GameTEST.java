import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test case 1
        Game.player1 = mock(Player.class);
        Game.player2 = mock(Player.class);
        Game.playingPlayer = 1;
        Player result1 = Game.getPlayingPlayer();
        assertEquals(Game.player1, result1);
        
        // Test case 2
        Game.playingPlayer = 2;
        Player result2 = Game.getPlayingPlayer();
        assertEquals(Game.player2, result2);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test case 1
        Player p = mock(Player.class);
        Game.player1 = mock(Player.class);
        Game.player2 = p;
        Player result1 = Game.getNonPlayingPlayer();
        assertEquals(Game.player1, result1);
        
        // Test case 2
        Game.player1 = p;
        Game.player2 = mock(Player.class);
        Player result2 = Game.getNonPlayingPlayer();
        assertEquals(Game.player2, result2);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test case 1
        Player p = mock(Player.class);
        Game.player1 = p;
        Game.player2 = mock(Player.class);
        Player result1 = Game.getOtherPlayer(p);
        assertEquals(Game.player2, result1);
        
        // Test case 2
        Game.player1 = mock(Player.class);
        Game.player2 = p;
        Player result2 = Game.getOtherPlayer(p);
        assertEquals(Game.player1, result2);
    }
    
    @Test
    public void testInit() {
        // Test case
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = mock(CardCollection.class);
        CardCollection deck2 = mock(CardCollection.class);
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Verify the initializing of player1, player2, and their decks
        verify(IP1).init(Game.player1);
        verify(IP2).init(Game.player2);
        assertEquals(1, Game.playingPlayer);
        
        // Verify the setting of currentPhase
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Verify the ownership and zone setting for cards in deck1 and deck2
        verify(deck1, times(2)).iterator(); // Assuming that deck1 has 2 cards
        verify(deck2, times(2)).iterator(); // Assuming that deck2 has 2 cards
        
        for (Card c : deck1) {
            verify(c).setOwner(Game.player1);
            verify(c).setZone(Game.player1.library);
        }
        
        for (Card c : deck2) {
            verify(c).setOwner(Game.player2);
            verify(c).setZone(Game.player2.library);
        }
    }
    
    // You can add test cases for other methods
    
}
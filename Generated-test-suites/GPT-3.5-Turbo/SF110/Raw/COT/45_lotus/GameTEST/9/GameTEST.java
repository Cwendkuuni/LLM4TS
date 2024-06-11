
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player expected = Game.player1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        expected = Game.player2;
        actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        
        // Test when current player is player1
        Game.playingPlayer = 1;
        Player expected = p2;
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
        
        // Test when current player is player 2
        Game.playingPlayer = 2;
        expected = p1;
        actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player p1 = new Player();
        Player p2 = new Player();
        Game.player1 = p1;
        Game.player2 = p2;
        
        // Test when p is player1
        Player expected = p2;
        Player actual = Game.getOtherPlayer(p1);
        assertEquals(expected, actual);
        
        // Test when p is player2
        expected = p1;
        actual = Game.getOtherPlayer(p2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        
        // Assert that players and their decks are initialized
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals("Player1", Game.player1.getName());
        assertEquals("Player2", Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Assert that user interfaces are initialized
        assertNotNull(IP1.initializedPlayer);
        assertNotNull(IP2.initializedPlayer);
        assertEquals(Game.player1, IP1.initializedPlayer);
        assertEquals(Game.player2, IP2.initializedPlayer);
        
        // Assert that playingPlayer is set to 1 and currentPhase is initialized as UntapPhase
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        // Assert that card owners and zones are correctly set
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Mock player interfaces
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Game.player1 = new Player(IP1);
        Game.player2 = new Player(IP2);
        Game.playingPlayer = 1;
        
        // Mock player speak methods
        when(IP1.letPlayerSpeak()).thenReturn(false);
        when(IP2.letPlayerSpeak()).thenReturn(true);
        
        // Test when playingPlayer is 1
        Game.givePriorityToCurrentPlayer();
        
        // Assert that letPlayerSpeak was called for each player
        verify(IP1, times(1)).letPlayerSpeak();
        verify(IP2, times(1)).letPlayerSpeak();
        
        // Assert that stack was resolved and givePriorityToCurrentPlayer method was called recursively
        assertEquals(1, Game.stack.resolveLastCalled);
        assertEquals(2, Game.givePriorityToCurrentPlayerCalled);
        
        // Reset the mocks and variables
        reset(IP1);
        reset(IP2);
        Game.stack.resolveLastCalled = 0;
        Game.givePriorityToCurrentPlayerCalled = 0;
        
        // Mock player speak methods again
        when(IP1.letPlayerSpeak()).thenReturn(true);
        when(IP2.letPlayerSpeak()).thenReturn(false);
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        Game.givePriorityToCurrentPlayer();
        
        // Assert that letPlayerSpeak was called for each player
        verify(IP1, times(1)).letPlayerSpeak();
        verify(IP2, times(1)).letPlayerSpeak();
        
        // Assert that stack was resolved and givePriorityToCurrentPlayer method was called recursively
        assertEquals(1, Game.stack.resolveLastCalled);
        assertEquals(2, Game.givePriorityToCurrentPlayerCalled);
    }
}

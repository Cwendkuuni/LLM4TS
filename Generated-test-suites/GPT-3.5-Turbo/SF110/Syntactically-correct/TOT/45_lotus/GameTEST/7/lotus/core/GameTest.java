import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    
    @Before
    public void setUp() {
        // Test setup code
    }
    
    @Test
    public void testGetPlayingPlayer() {
        // Test typical use case
        Game.playingPlayer = 1;
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Player expectedResult = player1;
        Player actualResult = Game.getPlayingPlayer();
        assertEquals(expectedResult, actualResult);
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        expectedResult = player2;
        actualResult = Game.getPlayingPlayer();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test typical use case
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Player currentPlayer = player1;
        Player expectedResult = player2;
        Player actualResult = Game.getNonPlayingPlayer(currentPlayer);
        assertEquals(expectedResult, actualResult);
        
        // Test when currentPlayer is player2
        currentPlayer = player2;
        expectedResult = player1;
        actualResult = Game.getNonPlayingPlayer(currentPlayer);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInit() {
        // Test typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Assert that the players are initialized correctly
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Assert that the UserInterfaces are initialized correctly
        assertEquals(Game.player1, IP1.getInitializedPlayer());
        assertEquals(Game.player2, IP2.getInitializedPlayer());
        
        // Assert that playingPlayer is set to 1 and currentPhase is UntapPhase
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        
        // Assert that the owner and zone are set correctly for cards in deck1 and deck2
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test typical use case
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        Game.stack = new Stack();
        
        // Create a mock object for letPlayerSpeak, assuming it returns true
        Player mockPlayer1 = Mockito.mock(Player.class);
        Player mockPlayer2 = Mockito.mock(Player.class);
        when(mockPlayer1.letPlayerSpeak()).thenReturn(true);
        when(mockPlayer2.letPlayerSpeak()).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);
        
        Game.givePriorityToCurrentPlayer();
        
        // Assert that both players' letPlayerSpeak method is called
        verify(mockPlayer1, times(1)).letPlayerSpeak();
        verify(mockPlayer2, times(1)).letPlayerSpeak();
        
        // Assert that stack.resolveLast() is called
        assertTrue(Game.stack.isEmpty());
        
        // Test when stack is not empty
        Game.stack = new Stack();
        Game.stack.push(new Card());
        
        Game.givePriorityToCurrentPlayer();
        
        // Assert that stack.resolveLast() is called recursively
        assertTrue(Game.stack.isEmpty());
        verify(mockPlayer1, times(2)).letPlayerSpeak();
        verify(mockPlayer2, times(2)).letPlayerSpeak();
    }
}
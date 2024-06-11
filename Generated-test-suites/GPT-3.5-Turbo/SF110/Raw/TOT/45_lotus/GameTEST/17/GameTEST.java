import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        
        assertEquals(player1, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        
        assertEquals(player2, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        
        Game.player1 = player1;
        Game.player2 = player2;
        
        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }
    
    @Test
    public void testInit() {
        UserInterface UI1 = new UserInterface();
        UserInterface UI2 = new UserInterface();
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Test initialization
        Game.init(UI1, UI2, name1, name2, deck1, deck2);
        
        // Check if players are initialized correctly
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        
        // Check if players have been assigned the correct names
        assertEquals(name1, Game.player1.getName());
        assertEquals(name2, Game.player2.getName());
        
        // Check if players have been assigned the correct decks
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Check if user interfaces have been initialized correctly
        assertNotNull(Game.player1.getUserInterface());
        assertNotNull(Game.player2.getUserInterface());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Create player interfaces for testing
        UserInterface UI1 = new UserInterface();
        UserInterface UI2 = new UserInterface();
        
        // Create players and assign interfaces
        Player player1 = new Player(UI1, "Player 1", new CardCollection());
        Player player2 = new Player(UI2, "Player 2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        
        // Set playing player to player1
        Game.playingPlayer = 1;
        
        // Initialize stack with a card
        Card card = new Card();
        Game.stack.push(card);
        
        // Set player1's letPlayerSpeak() to return true and player2's letPlayerSpeak() to return false
        UI1.setLetPlayerSpeak(true);
        UI2.setLetPlayerSpeak(false);
        
        // Call givePriorityToCurrentPlayer() method
        Game.givePriorityToCurrentPlayer();
        
        // Check if player1's letPlayerSpeak() was called
        assertTrue(UI1.isLetPlayerSpeakCalled());
        
        // Check if player2's letPlayerSpeak() was not called
        assertFalse(UI2.isLetPlayerSpeakCalled());
        
        // Check if the stack was resolved
        assertTrue(Game.stack.isEmpty());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer_WithNestedCalls() {
        // Create player interfaces for testing
        UserInterface UI1 = new UserInterface();
        UserInterface UI2 = new UserInterface();
        
        // Create players and assign interfaces
        Player player1 = new Player(UI1, "Player 1", new CardCollection());
        Player player2 = new Player(UI2, "Player 2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        
        // Set playing player to player1
        Game.playingPlayer = 1;
        
        // Initialize stack with cards
        Card card1 = new Card();
        Card card2 = new Card();
        Game.stack.push(card1);
        Game.stack.push(card2);
        
        // Set player1's letPlayerSpeak() to return true and player2's letPlayerSpeak() to return false
        UI1.setLetPlayerSpeak(true);
        UI2.setLetPlayerSpeak(false);
        
        // Set resolveLast() to return true for the first call and false for the second call
        Game.stack.setResolveLast(true);
        Game.stack.setResolveLast(false);
        
        // Call givePriorityToCurrentPlayer() method
        Game.givePriorityToCurrentPlayer();
        
        // Check if player1's letPlayerSpeak() was called twice
        assertEquals(2, UI1.getLetPlayerSpeakCallCount());
        
        // Check if player2's letPlayerSpeak() was not called
        assertFalse(UI2.isLetPlayerSpeakCalled());
        
        // Check if the stack was resolved twice
        assertTrue(Game.stack.isEmpty());
    }
}
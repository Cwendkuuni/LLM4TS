import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case: playingPlayer is 1, expect to return player1
        // Arrange
        Game.playingPlayer = 1;
        Game.player1 = new Player(null, "Player 1", null);
        // Act
        Player playingPlayer = Game.getPlayingPlayer();
        // Assert
        assertEquals(Game.player1, playingPlayer);
        
        // Typical use case: playingPlayer is 2, expect to return player2
        // Arrange
        Game.playingPlayer = 2;
        Game.player2 = new Player(null, "Player 2", null);
        // Act
        playingPlayer = Game.getPlayingPlayer();
        // Assert
        assertEquals(Game.player2, playingPlayer);
        
        // Edge case: playingPlayer is 0, expect to return null
        // Arrange
        Game.playingPlayer = 0;
        // Act
        playingPlayer = Game.getPlayingPlayer();
        // Assert
        assertNull(playingPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case: playingPlayer is 1, expect to return player2
        // Arrange
        Game.player2 = new Player(null, "Player 2", null);
        // Act
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        // Assert
        assertEquals(Game.player2, nonPlayingPlayer);
        
        // Typical use case: playingPlayer is 2, expect to return player1
        // Arrange
        Game.player1 = new Player(null, "Player 1", null);
        // Act
        nonPlayingPlayer = Game.getNonPlayingPlayer();
        // Assert
        assertEquals(Game.player1, nonPlayingPlayer);
        
        // Edge case: both player1 and player2 are null, expect to return null
        // Arrange
        Game.player1 = null;
        Game.player2 = null;
        // Act
        nonPlayingPlayer = Game.getNonPlayingPlayer();
        // Assert
        assertNull(nonPlayingPlayer);
    }
    
    @Test
    public void testInit() {
        // Typical use case
        // Arrange
        UserInterface ip1 = new UserInterface();
        UserInterface ip2 = new UserInterface();
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        // Act
        Game.init(ip1, ip2, name1, name2, deck1, deck2);
        // Assert
        assertEquals(ip1, Game.player1.getInterface());
        assertEquals(ip2, Game.player2.getInterface());
        assertEquals(name1, Game.player1.getName());
        assertEquals(name2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertNull(Game.player1.getOpponent());
        assertNull(Game.player2.getOpponent());
        assertNotNull(Game.player1.getLibrary());
        assertNotNull(Game.player2.getLibrary());
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        
        // Edge case: input null values for IP1 and IP2, expect to initialize players without any UserInterface
        // Arrange
        ip1 = null;
        ip2 = null;
        // Act
        Game.init(ip1, ip2, name1, name2, deck1, deck2);
        // Assert
        assertNull(Game.player1.getInterface());
        assertNull(Game.player2.getInterface());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case: stack is empty, expect to do nothing
        // Arrange
        Game.stack = new Stack();
        // Act
        Game.givePriorityToCurrentPlayer();
        // Assert
        // No exception is thrown
        
        // Typical use case: both players letPlayerSpeak return true, expect to do nothing
        // Arrange
        Player player1 = new Player(null, "Player 1", null);
        Player player2 = new Player(null, "Player 2", null);
        Game.stack = new Stack();
        Game.playingPlayer = 1;
        Game.player1 = player1;
        Game.player2 = player2;
        // Act
        Game.givePriorityToCurrentPlayer();
        // Assert
        // No exception is thrown
        
        // Typical use case: player2 letPlayerSpeak return false, expect to call resolveLast and recursively call givePriorityToCurrentPlayer
        // Arrange
        player1 = new Player(null, "Player 1", null);
        player2 = new Player(null, "Player 2", null);
        Game.stack = new Stack();
        Game.playingPlayer = 1;
        Game.player1 = player1;
        Game.player2 = player2;
        player2.setLetPlayerSpeakResult(false);
        // Act
        Game.givePriorityToCurrentPlayer();
        // Assert
        assertEquals(1, player2.getResolveLastCalledCount());
        assertEquals(1, player1.getResolveLastCalledCount());
        
        // Edge case: stack has one item and players keep returning true, expect to resolveLast and give priority to next player
        // Arrange
        player1 = new Player(null, "Player 1", null);
        player2 = new Player(null, "Player 2", null);
        Game.stack = new Stack();
        Game.playingPlayer = 1;
        Game.player1 = player1;
        Game.player2 = player2;
        Game.stack.push(new Object());
        // Act
        Game.givePriorityToCurrentPlayer();
        // Assert
        assertEquals(1, player1.getResolveLastCalledCount());
        assertEquals(1, player2.getResolveLastCalledCount());
        assertEquals(2, Game.playingPlayer);
    }
}
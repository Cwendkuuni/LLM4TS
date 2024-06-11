
import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        // Player 1 is the playing player
        Game.playingPlayer = 1;
        Player expectedResult = Game.player1;
        Player actualResult = Game.getPlayingPlayer();
        assertEquals(expectedResult, actualResult);
        
        // Edge case
        // Player 2 is the playing player
        Game.playingPlayer = 2;
        expectedResult = Game.player2;
        actualResult = Game.getPlayingPlayer();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        // Player 1 is the non-playing player
        Game.playingPlayer = 1;
        Player expectedResult = Game.player2;
        Player actualResult = Game.getNonPlayingPlayer();
        assertEquals(expectedResult, actualResult);
        
        // Edge case
        // Player 2 is the non-playing player
        Game.playingPlayer = 2;
        expectedResult = Game.player1;
        actualResult = Game.getNonPlayingPlayer();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        // Player 1 is the input player
        Player inputPlayer = Game.player1;
        Player expectedResult = Game.player2;
        Player actualResult = Game.getOtherPlayer(inputPlayer);
        assertEquals(expectedResult, actualResult);
        
        // Typical use case
        // Player 2 is the input player
        inputPlayer = Game.player2;
        expectedResult = Game.player1;
        actualResult = Game.getOtherPlayer(inputPlayer);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInit() {
        // Typical use case
        
        // Mock UserInterface objects
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        
        String n1 = "player1";
        String n2 = "player2";
        
        // Mock CardCollection objects
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Check if Game.player1 and Game.player2 are correctly initialized
        assertEquals(Game.player1.getName(), n1);
        assertEquals(Game.player2.getName(), n2);
        
        // Check if Game.playingPlayer and Game.currentPhase are correctly initialized
        assertEquals(Game.playingPlayer, 1);
        assertEquals(Game.currentPhase.getClass(), UntapPhase.class);
        
        // Check if the owners and zones of the cards in deck1 and deck2 are correctly set
        for (Card c : deck1) {
            assertEquals(c.owner, Game.player1);
            assertEquals(c.zone, Game.player1.library);
        }
        
        for (Card c : deck2) {
            assertEquals(c.owner, Game.player2);
            assertEquals(c.zone, Game.player2.library);
        }
        
        // Check if IP1 and IP2 are correctly initialized with the respective players
        assertEquals(IP1.getPlayer(), Game.player1);
        assertEquals(IP2.getPlayer(), Game.player2);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Edge case
        // The getPlayingPlayer() method returns null
        Game.playingPlayer = 1;
        Game.player1 = null;
        Game.player2 = new Player();
        
        Game.givePriorityToCurrentPlayer();
        
        // Check if Game.stack is empty
        assertTrue(Game.stack.empty());
    }

}

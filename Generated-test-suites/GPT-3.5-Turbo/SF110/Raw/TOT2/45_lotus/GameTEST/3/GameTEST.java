import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // typical use case
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // edge case
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // typical use case
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        
        // edge case
        // test with empty decks
        Game.init(new UserInterface(), new UserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // typical use case, cannot fully test without mocking letPlayerSpeak()
        // edge case
        // test when stack is not empty
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
    }
}
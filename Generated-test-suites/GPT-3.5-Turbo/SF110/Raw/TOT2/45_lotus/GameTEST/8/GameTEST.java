import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        UserInterface mockUI1 = new UserInterface();
        UserInterface mockUI2 = new UserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(mockUI1, mockUI2, "Player1", "Player2", deck1, deck2);
        
        assertEquals("Player1", Game.player1.name);
        assertEquals("Player2", Game.player2.name);
        assertEquals(mockUI1, Game.player1.userInterface);
        assertEquals(mockUI2, Game.player2.userInterface);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test when both players letPlayerSpeak()
        Game.playingPlayer = 1;        
        assertTrue(Game.getPlayingPlayer().letPlayerSpeak());
        assertTrue(Game.getNonPlayingPlayer().letPlayerSpeak());
        Game.givePriorityToCurrentPlayer();
        
        // Test when playingPlayer does not letPlayerSpeak()
        Game.playingPlayer = 2;        
        assertFalse(Game.getPlayingPlayer().letPlayerSpeak());
        assertTrue(Game.getNonPlayingPlayer().letPlayerSpeak());
        Game.givePriorityToCurrentPlayer();
        
        // Test when nonPlayingPlayer does not letPlayerSpeak()
        Game.playingPlayer = 1;        
        assertTrue(Game.getPlayingPlayer().letPlayerSpeak());
        assertFalse(Game.getNonPlayingPlayer().letPlayerSpeak());
        Game.givePriorityToCurrentPlayer();
    }
}
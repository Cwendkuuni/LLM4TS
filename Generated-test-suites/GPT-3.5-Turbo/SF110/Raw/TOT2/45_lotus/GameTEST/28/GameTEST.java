import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testInit() {
        // Typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(Game.player1, Game.getPlayingPlayer());
        assertEquals(new UntapPhase(), Game.currentPhase);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        // Assuming letPlayerSpeak() returns true for both players initially
        Game.givePriorityToCurrentPlayer();
        
        // Edge case
        // Assuming letPlayerSpeak() returns false for both players initially
        Game.getPlayingPlayer().letPlayerSpeak();
        Game.getNonPlayingPlayer().letPlayerSpeak();
        Game.givePriorityToCurrentPlayer();
    }

}
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import lotus.core.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical use case: game has just started and player1 is the playing player
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Edge case: game has just started and player2 is the playing player
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case: game has just started and player1 is the playing player
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Edge case: game has just started and player2 is the playing player
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical use case: player1 is passed as argument and player2 is returned
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Typical use case: player2 is passed as argument and player1 is returned
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Typical use case: Two players, decks, and user interfaces are provided and the game is properly initialized
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, IP1.player);
        assertEquals(Game.player2, IP2.player);
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
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
        // Typical use case: current playing player is given priority
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Game.stack.push(someCard);
        
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(Game.stack.empty());
    }
}
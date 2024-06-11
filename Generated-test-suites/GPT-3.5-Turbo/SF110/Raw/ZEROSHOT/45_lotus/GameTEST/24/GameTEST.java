import static org.junit.Assert.*;
import org.junit.*;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test case 1: playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test case 2: playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test case 1: playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test case 2: playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test case 1: input player is player1
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Test case 2: input player is player2
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        // Test case 1: initializing with IP1, IP2, n1, n2, deck1, deck2
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, IP1.getPlayer());
        assertEquals(Game.player2, IP2.getPlayer());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        
        // Additional assertions can be added for other variables initialization
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test case 1: stack is empty
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        // Assertions for expected behavior when stack is empty
        
        // Test case 2: stack is not empty
        // TODO: Add assertions for expected behavior when stack is not empty
        
        // Test case 3: APdone is true, NAPdone is true
        // TODO: Add assertions for expected behavior when APdone is true, NAPdone is true
    }
    
    // Additional test methods for other methods in the Game class can be added here
    
}
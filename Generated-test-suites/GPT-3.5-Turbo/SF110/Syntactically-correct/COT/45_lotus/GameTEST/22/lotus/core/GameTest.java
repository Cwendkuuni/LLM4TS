import lotus.core.*;
import lotus.core.card.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.stack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Check if getPlayingPlayer returns the correct player
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Check if getNonPlayingPlayer returns the correct player
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        
        Game.playingPlayer = 1;
        assertEquals(player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Check if getOtherPlayer returns the correct player
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        
        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }
    
    @Test
    public void testInit() {
        // Check if init sets the correct values
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, new Player(IP1, n1, deck1));
        assertEquals(Game.player2, new Player(IP2, n2, deck2));
        assertEquals(Game.playingPlayer, 1);
        assertEquals(Game.currentPhase, new UntapPhase());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test for givePriorityToCurrentPlayer
        // Create stack with one card
        Stack stack = new Stack();
        stack.push(new Card());
        Game.stack = stack;
        
        // Assume letPlayerSpeak is implemented correctly and returns true when a player speaks
        Player player1 = new Player(new UserInterface(), "Player1", new CardCollection());
        player1.setLetPlayerSpeak(true);
        Game.player1 = player1;
        Game.playingPlayer = 1;
        
        Game.givePriorityToCurrentPlayer();
        
        // Check if the stack is resolved and givePriorityToCurrentPlayer is called again
        assertTrue(stack.empty());
        assertEquals(player1, Game.getPlayingPlayer());
        
        // Test when stack is empty
        stack = new Stack();
        Game.stack = stack;
        
        Game.givePriorityToCurrentPlayer();
        
        // Check that the method finishes
        assertTrue(stack.empty());
        assertEquals(player1, Game.getPlayingPlayer());
    }
    
}
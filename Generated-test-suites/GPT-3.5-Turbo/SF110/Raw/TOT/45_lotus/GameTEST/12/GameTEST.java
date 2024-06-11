package lotus.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        Player player1 = new Player();
        Game.player1 = player1;
        assertEquals(player1, Game.getPlayingPlayer());
        
        // Edge Case: Switching playing player
        Game.playingPlayer = 2;
        Player player2 = new Player();
        Game.player2 = player2;
        assertEquals(player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Typical Use Case
        Game.playingPlayer = 1;
        Player player2 = new Player();
        Game.player2 = player2;
        assertEquals(player2, Game.getNonPlayingPlayer());
        
        // Edge Case: Switching playing player
        Game.playingPlayer = 2;
        Player player1 = new Player();
        Game.player1 = player1;
        assertEquals(player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Typical Use Case
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Typical Use Case
        Game.player1 = new Player();
        Game.player2 = new Player();
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
        
        // Edge Case: Same player instance
        Player player = new Player();
        Game.player1 = player;
        Game.player2 = player;
        assertNull(Game.getOtherPlayer(Game.player1));
    }
    
    @Test
    public void testInit() {
        // Typical Use Case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(IP2, Game.player2.getInterface());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(Game.currentPhase, new UntapPhase());
        
        // Error Scenario: Null interface
        UserInterface nullInterface = null;
        assertThrows(NullPointerException.class, () -> {
            Game.init(IP1, nullInterface, n1, n2, deck1, deck2);
        });
        
        // Error Scenario: Null player name
        String nullName = null;
        assertThrows(NullPointerException.class, () -> {
            Game.init(IP1, IP2, nullName, n2, deck1, deck2);
        });
        
        // Error Scenario: Null deck
        CardCollection nullDeck = null;
        assertThrows(NullPointerException.class, () -> {
            Game.init(IP1, IP2, n1, n2, deck1, nullDeck);
        });
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical Use Case
        Game.stack.push(new Card());
        Player player1 = new Player();
        Game.player1 = player1;
        Player player2 = new Player();
        Game.player2 = player2;
        
        player1.setCanSpeak(true);
        player2.setCanSpeak(false);
        
        Game.givePriorityToCurrentPlayer();
        
        assertFalse(Game.stack.empty());
        assertFalse(player1.canSpeak());
        assertTrue(player2.canSpeak());
        
        // Typical Use Case: Resolving last item in the stack
        Game.stack.push(new Card());
        player1.setCanSpeak(true);
        player2.setCanSpeak(false);
        
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(Game.stack.empty());
        assertFalse(player1.canSpeak());
        assertTrue(player2.canSpeak());
    }
}
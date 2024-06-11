package lotus.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import lotus.core.interfaces.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {
    private UserInterface IP1;
    private UserInterface IP2;
    private String n1;
    private String n2;
    private CardCollection deck1;
    private CardCollection deck2;
    
    @Before
    public void setUp() {
        IP1 = new UserInterfaceImpl();
        IP2 = new UserInterfaceImpl();
        n1 = "Player 1";
        n2 = "Player 2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
    }
    
    @Test
    public void testInit() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        IP1.init(Game.player1);
        IP2.init(Game.player2);
        Game.playingPlayer = 1;
        Game.currentPhase = new UntapPhase();
        
        // Deck1 initialization
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        
        // Deck2 initialization
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        
        // Simulate player1's turn
        Game.player1 = new Player(IP1, n1, deck1);
        Game.playingPlayer = 1;
        
        // Simulate player1 playing cards
        Game.stack.resolveLast();
        Game.stack.resolveLast();
        
        // Simulate player2's turn
        Game.player2 = new Player(IP2, n2, deck2);
        Game.playingPlayer = 2;
        
        // Simulate player2 playing cards
        Game.stack.resolveLast();
        
        // Check if stack is empty
        assertTrue(Game.stack.empty());
    }
}
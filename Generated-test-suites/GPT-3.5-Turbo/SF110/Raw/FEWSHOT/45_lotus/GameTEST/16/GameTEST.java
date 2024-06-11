package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    private UserInterface IP1;
    private UserInterface IP2;
    private String n1;
    private String n2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setUp() {
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        n1 = "Player 1";
        n2 = "Player 2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, Game.player1);
        assertEquals(Game.player2, Game.player2);
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
        Game.stack = new Stack();
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(Game.stack.empty());
    }

}
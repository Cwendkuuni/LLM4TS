package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;

public class GameTest {
    
    private UserInterface IP1;
    private UserInterface IP2;
    private CardCollection deck1;
    private CardCollection deck2;
    
    @Before
    public void setup() {
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player(IP1, "Player1", deck1);
        Game.player2 = new Player(IP2, "Player2", deck2);
        Game.playingPlayer = 1;
        
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(IP1, "Player1", deck1);
        Game.player2 = new Player(IP2, "Player2", deck2);
        
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(IP1, "Player1", deck1);
        Game.player2 = new Player(IP2, "Player2", deck2);
        
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        Game.player1 = new Player(IP1, "Player1", deck1);
        Game.player2 = new Player(IP2, "Player2", deck2);
        
        Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        
        assertEquals(Game.player1, IP1.getAssociatedPlayer());
        assertEquals(Game.player2, IP2.getAssociatedPlayer());
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
        UserInterface AP = new UserInterface();
        UserInterface NAP = new UserInterface();
        Game.player1 = new Player(AP, "Player1", deck1);
        Game.player2 = new Player(NAP, "Player2", deck2);
        
        AP.setLetPlayerSpeakResult(false);
        NAP.setLetPlayerSpeakResult(true);
        
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(AP.getLetPlayerSpeakCalled());
        assertTrue(NAP.getLetPlayerSpeakCalled());
        assertTrue(Game.stack.empty());
        
        AP.setLetPlayerSpeakResult(true);
        NAP.setLetPlayerSpeakResult(false);
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(AP.getLetPlayerSpeakCalled());
        assertTrue(NAP.getLetPlayerSpeakCalled());
        assertTrue(Game.stack.empty());
        
        AP.setLetPlayerSpeakResult(false);
        NAP.setLetPlayerSpeakResult(false);
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        
        assertTrue(AP.getLetPlayerSpeakCalled());
        assertTrue(NAP.getLetPlayerSpeakCalled());
        assertTrue(Game.stack.empty());      
    }
    
    // Add more test cases for other methods in the Game class
    
}
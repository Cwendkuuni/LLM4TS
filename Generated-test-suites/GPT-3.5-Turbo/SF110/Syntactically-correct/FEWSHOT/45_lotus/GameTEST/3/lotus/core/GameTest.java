package lotus.core;

import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        Player playingPlayer = Game.getPlayingPlayer();
        
        assertEquals(Game.player1, playingPlayer);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        
        assertEquals(Game.player2, nonPlayingPlayer);
    }
    
    @Test
    public void testGetOtherPlayer() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        Player p = Game.player1;
        Player otherPlayer = Game.getOtherPlayer(p);
        
        assertEquals(Game.player2, otherPlayer);
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(Game.player1, IP1.getPlayer());
        assertEquals(Game.player2, IP2.getPlayer());
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
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
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        // Test with empty stack
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // Test with non-empty stack
        Card card1 = new Card();
        Card card2 = new Card();
        Game.stack.push(card1);
        Game.stack.push(card2);
        
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }

}
package lotus.core;

import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // typical use case where playingPlayer = 1
        Game.playingPlayer = 1;
        Player expected = Game.player1;
        Player actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
        
        // typical use case where playingPlayer != 1
        Game.playingPlayer = 2;
        expected = Game.player2;
        actual = Game.getPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // typical use case where playingPlayer = 1
        Game.playingPlayer = 1;
        Player expected = Game.player2;
        Player actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
        
        // typical use case where playingPlayer != 1
        Game.playingPlayer = 2;
        expected = Game.player1;
        actual = Game.getNonPlayingPlayer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetOtherPlayer() {
        // typical use case where p is player1
        Player p = Game.player1;
        Player expected = Game.player2;
        Player actual = Game.getOtherPlayer(p);
        assertEquals(expected, actual);
        
        // typical use case where p is player2
        p = Game.player2;
        expected = Game.player1;
        actual = Game.getOtherPlayer(p);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInit() {
        // typical use case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertEquals(IP1, Game.player1.getInterface());
        assertEquals(IP2, Game.player2.getInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        
        // edge case where deck1 and deck2 are empty
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        assertTrue(Game.player1.library.isEmpty());
        assertTrue(Game.player2.library.isEmpty());
        
        // error scenario where IP1 and IP2 are null
        IP1 = null;
        IP2 = null;
        assertThrows(NullPointerException.class, () -> {
            Game.init(IP1, IP2, n1, n2, deck1, deck2);
        });
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // typical use case where stack is empty and both players give priority
        Game.stack.clear();
        Game.playingPlayer = 1;
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        Game.player1 = new Player(IP1, "player1", new CardCollection());
        Game.player2 = new Player(IP2, "player2", new CardCollection());
        assertTrue(Game.stack.empty());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // edge case where stack is not empty and players resolve last item in stack
        Game.stack.push("item1");
        assertFalse(Game.stack.empty());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        // error scenario where playingPlayer = 0
        Game.playingPlayer = 0;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Game.givePriorityToCurrentPlayer();
        });
    }
}
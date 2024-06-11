import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import lotus.core.*;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        Player p1 = new Player(null, null, null);
        Player p2 = new Player(null, null, null);
        Game.player1 = p1;
        Game.player2 = p2;
        
        Game.playingPlayer = 1;
        assertEquals(p1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(p2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player p1 = new Player(null, null, null);
        Player p2 = new Player(null, null, null);
        Game.player1 = p1;
        Game.player2 = p2;
        
        Game.playingPlayer = 1;
        assertEquals(p2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(p1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player p1 = new Player(null, null, null);
        Player p2 = new Player(null, null, null);
        Game.player1 = p1;
        Game.player2 = p2;
        
        assertEquals(p2, Game.getOtherPlayer(p1));
        assertEquals(p1, Game.getOtherPlayer(p2));
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = null;
        UserInterface IP2 = null;
        String n1 = "Player1";
        String n2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = new Stack();
        
        Player p1 = new Player(null, null, null);
        Player p2 = new Player(null, null, null);
        Game.player1 = p1;
        Game.player2 = p2;
        
        Game.playingPlayer = 1;
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
        
        Game.stack.push(new Card());
        Game.playingPlayer = 2;
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }
}
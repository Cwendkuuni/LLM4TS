import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class GameTest
{
    @Test
    public void testGetPlayingPlayer()
    {
        // typical use case
        Game.playingPlayer = 1;
        Player expectedResult = Game.player1;
        Player actualResult = Game.getPlayingPlayer();
        assertEquals(expectedResult, actualResult);
        
        // edge case
        Game.playingPlayer = 2;
        expectedResult = Game.player2;
        actualResult = Game.getPlayingPlayer();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetNonPlayingPlayer()
    {
        // typical use case
        Game.playingPlayer = 1;
        Player expectedResult = Game.player2;
        Player actualResult = Game.getNonPlayingPlayer();
        assertEquals(expectedResult, actualResult);
        
        // edge case
        Game.playingPlayer = 2;
        expectedResult = Game.player1;
        actualResult = Game.getNonPlayingPlayer();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetOtherPlayer()
    {
        // typical use case
        Player testPlayer = new Player(null, "test", new CardCollection());
        Game.player1 = testPlayer;
        Game.player2 = new Player(null, "test2", new CardCollection());
        Player expectedResult = Game.player2;
        Player actualResult = Game.getOtherPlayer(Game.player1);
        assertEquals(expectedResult, actualResult);
        
        // edge case
        expectedResult = Game.player1;
        actualResult = Game.getOtherPlayer(Game.player2);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInit()
    {
        // typical use case
        UserInterface IP1 = null;
        UserInterface IP2 = null;
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        Player player1 = Game.player1;
        Player player2 = Game.player2;
        
        assertEquals(n1, player1.getName());
        assertEquals(n2, player2.getName());
        assertEquals(deck1, player1.getDeck());
        assertEquals(deck2, player2.getDeck());
        
        // edge case
        n1 = "player1 ";
        n2 = "player2 ";
        deck1.add(new Card());
        deck2.add(new Card());
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        player1 = Game.player1;
        player2 = Game.player2;
        
        assertEquals(n1.trim(), player1.getName());
        assertEquals(n2.trim(), player2.getName());
        assertEquals(deck1, player1.getDeck());
        assertEquals(deck2, player2.getDeck());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer()
    {
        // typical use case
        // assuming letPlayerSpeak() returns false when player is done speaking
        Game.playingPlayer = 1;
        Game.stack = new Stack();
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        assertTrue(Game.stack.size() > 0);
        
        Game.givePriorityToCurrentPlayer();
        assertFalse(Game.stack.isEmpty());
        
        // edge case
        Game.playingPlayer = 2;
        Game.stack = new Stack();
        Game.stack.push(new Card());
        assertFalse(Game.stack.isEmpty());
        
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.isEmpty());
    }
}
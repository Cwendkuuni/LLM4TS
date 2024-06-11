import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.interfaces.*;
import lotus.core.player.*;
import lotus.core.card.*;
import lotus.core.phases.*;
import lotus.core.stack.*;
import java.util.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        assertTrue(Game.getPlayingPlayer() instanceof Player);
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        assertTrue(Game.getNonPlayingPlayer() instanceof Player);
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player p = new Player(new UserInterface(), "name", new CardCollection());
        assertNotEquals(p, Game.getOtherPlayer(p));
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "name1";
        String n2 = "name2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals("name1", Game.player1.getName());
        assertEquals("name2", Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertFalse(Game.stack.empty());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "name1";
        String n2 = "name2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        Game.givePriorityToCurrentPlayer();
        
        // Add test assertions here
    }
    
    // Add more test methods for other methods in the Game class
    
}
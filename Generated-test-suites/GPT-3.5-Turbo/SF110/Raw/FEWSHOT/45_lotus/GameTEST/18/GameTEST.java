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
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        Player player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }
    
    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
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
        Player player1 = new Player(new UserInterface(), "Player 1", new CardCollection());
        player1.letPlayerSpeak(); // Assume player1 takes priority
        Game.stack.push(new Card());
        
        Game.player1 = player1;
        Game.player2 = new Player(new UserInterface(), "Player 2", new CardCollection());
        Game.playingPlayer = 1;
        
        Game.givePriorityToCurrentPlayer();
        
        assertFalse(Game.stack.empty());
        assertEquals(player1, Game.stack.resolveLast());
    }
}
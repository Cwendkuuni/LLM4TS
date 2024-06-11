import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lotus.core.interfaces.UserInterface;
import lotus.core.card.CardCollection;

public class GameTest {
    
    private static class MockUI implements UserInterface {
        public void init(Player player) {}
        public boolean letPlayerSpeak() { return true; }
    }
    
    private static class MockCardCollection extends CardCollection {}
    
    private UserInterface player1UI;
    private UserInterface player2UI;
    private CardCollection player1Deck;
    private CardCollection player2Deck;
    
    @Before
    public void setUp() {
        player1UI = new MockUI();
        player2UI = new MockUI();
        player1Deck = new MockCardCollection();
        player2Deck = new MockCardCollection();
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = new Player(player1UI, "Player 1", player1Deck);
        Game.player2 = new Player(player2UI, "Player 2", player2Deck);
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(player1UI, "Player 1", player1Deck);
        Game.player2 = new Player(player2UI, "Player 2", player2Deck);
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(player1UI, "Player 1", player1Deck);
        Game.player2 = new Player(player2UI, "Player 2", player2Deck);
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }
    
    @Test
    public void testInit() {
        Game.player1 = null;
        Game.player2 = null;
        Game.playingPlayer = 0;
        Game.currentPhase = null;
        Game.stack.clear();
        
        Game.init(player1UI, player2UI, "Player 1", "Player 2", player1Deck, player2Deck);
        
        assertEquals("Player 1", Game.player1.getName());
        assertEquals("Player 2", Game.player2.getName());
        assertEquals(player1Deck, Game.player1.getDeck());
        assertEquals(player2Deck, Game.player2.getDeck());
        assertEquals(player1UI, Game.player1.getUserInterface());
        assertEquals(player2UI, Game.player2.getUserInterface());
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.stack.isEmpty());
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.player1 = new Player(player1UI, "Player 1", player1Deck);
        Game.player2 = new Player(player2UI, "Player 2", player2Deck);
        Game.playingPlayer = 1;
        Game.currentPhase = new UntapPhase();
        
        Game.stack.clear();
        Game.stack.add(new Card());
        
        assertFalse(Game.stack.isEmpty());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.isEmpty());
        
        Game.stack.clear();
        Game.stack.add(new Card());
        Game.stack.add(new Card());
        
        assertFalse(Game.stack.isEmpty());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.isEmpty());
    }
}
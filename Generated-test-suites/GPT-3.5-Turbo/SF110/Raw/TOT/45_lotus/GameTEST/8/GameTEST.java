import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test typical case with playing player 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test typical case with playing player 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test typical case with playing player 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test typical case with playing player 2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test typical case with player 1
        Player p = Game.player1;
        assertEquals(Game.player2, Game.getOtherPlayer(p));
        
        // Test typical case with player 2
        p = Game.player2;
        assertEquals(Game.player1, Game.getOtherPlayer(p));
    }
    
    @Test
    public void testInit() {
        // Test typical case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(IP1, Game.player1.userInterface);
        assertEquals(IP2, Game.player2.userInterface);
        assertEquals(n1, Game.player1.name);
        assertEquals(n2, Game.player2.name);
        assertEquals(deck1, Game.player1.deck);
        assertEquals(deck2, Game.player2.deck);
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        
        // Test case with empty decks
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertEquals(deck1, Game.player1.deck);
        assertEquals(deck2, Game.player2.deck);
        
        // Test case with null decks
        deck1 = null;
        deck2 = null;
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertNull(Game.player1.deck);
        assertNull(Game.player2.deck);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test case with empty stack
        Game.stack.clear();
        Game.givePriorityToCurrentPlayer();
        
        // TODO: Add more test cases for this method
        
        // Test case with non-empty stack
        // This test case is complex and will be added later
    }
}
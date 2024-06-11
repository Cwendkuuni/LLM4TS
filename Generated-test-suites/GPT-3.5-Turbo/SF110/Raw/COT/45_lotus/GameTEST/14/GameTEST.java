import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {
    
    @Test
    public void testGetPlayingPlayer() {
        // Test if the correct playing player is returned
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        // Test if the correct non-playing player is returned
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        assertEquals(player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        assertEquals(player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        // Test if the correct other player is returned
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }
    
    @Test
    public void testInit() {
        // Test if the initialization is done correctly
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals(IP1, Game.player1.getUserInterface());
        assertEquals(IP2, Game.player2.getUserInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        
        for (Card c : deck1) {
            assertEquals(Game.player1, c.getOwner());
            assertEquals(Game.player1.library, c.getZone());
        }
        
        for (Card c : deck2) {
            assertEquals(Game.player2, c.getOwner());
            assertEquals(Game.player2.library, c.getZone());
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test if the priority is given correctly to the current player
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.stack.clear();
        
        // Simulate player1 having priority
        Game.playingPlayer = 1;
        
        // Simulate player1 choosing not to speak first
        player1.setPlayerSpeaking(false);
        player2.setPlayerSpeaking(true);
        
        // Simulate player2 choosing to speak
        player2.setPlayerSpeaking(false);
        player1.setPlayerSpeaking(true);
        
        // Test if the stack is resolved when both players are done speaking
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.isEmpty());
        
        // Test if the method calls itself recursively when the stack is not empty
        Game.stack.add(new Card());
        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.isEmpty());
    }
}
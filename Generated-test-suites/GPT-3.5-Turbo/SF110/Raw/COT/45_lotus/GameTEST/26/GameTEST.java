import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
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
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        
        assertEquals(player2, Game.getNonPlayingPlayer(player1));
        assertEquals(player1, Game.getNonPlayingPlayer(player2));
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
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
        
        assertEquals(player1, Game.player1);
        assertEquals(player2, Game.player2);
        
        assertEquals(player1, deck1.get(0).owner);
        assertEquals(player1.library, deck1.get(0).zone);
        
        assertEquals(player2, deck2.get(0).owner);
        assertEquals(player2.library, deck2.get(0).zone);
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        Game.init(IP1, IP2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
        
        Game.givePriorityToCurrentPlayer();
        
        // Test additional scenarios and edge cases for this method
    }
    
    // Additional JUnit test cases
    
    // Exception handling scenarios
    
}


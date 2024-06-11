import static org.junit.Assert.*;
import org.junit.*;

public class GameTest {
  
  @Test
  public void testGetPlayingPlayer() {
    // Initialize the necessary objects
    UserInterface IP1 = new UserInterface();
    UserInterface IP2 = new UserInterface();
    String n1 = "Player1";
    String n2 = "Player2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    
    // Initialize the game
    Game.init(IP1, IP2, n1, n2, deck1, deck2);
    
    // Test the getPlayingPlayer() method
    assertEquals(Game.player1, Game.getPlayingPlayer());
  }
  
  @Test
  public void testGetNonPlayingPlayer() {
    // Initialize the necessary objects
    UserInterface IP1 = new UserInterface();
    UserInterface IP2 = new UserInterface();
    String n1 = "Player1";
    String n2 = "Player2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    
    // Initialize the game
    Game.init(IP1, IP2, n1, n2, deck1, deck2);
    
    // Test the getNonPlayingPlayer() method
    assertEquals(Game.player2, Game.getNonPlayingPlayer());
  }
  
  @Test
  public void testGetOtherPlayer() {
    // Initialize the necessary objects
    UserInterface IP1 = new UserInterface();
    UserInterface IP2 = new UserInterface();
    String n1 = "Player1";
    String n2 = "Player2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    
    // Initialize the game
    Game.init(IP1, IP2, n1, n2, deck1, deck2);
    
    // Test the getOtherPlayer() method
    assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
  }
  
  @Test
  public void testGivePriorityToCurrentPlayer() {
    // Initialize the necessary objects
    UserInterface IP1 = new UserInterface();
    UserInterface IP2 = new UserInterface();
    String n1 = "Player1";
    String n2 = "Player2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    
    // Initialize the game
    Game.init(IP1, IP2, n1, n2, deck1, deck2);
    
    // Test the givePriorityToCurrentPlayer() method
    Game.givePriorityToCurrentPlayer();
    
    // Check that the stack is empty
    assertTrue(Game.stack.empty());
  }
}
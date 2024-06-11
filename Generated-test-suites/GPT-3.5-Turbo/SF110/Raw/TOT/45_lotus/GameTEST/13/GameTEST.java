import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
  
  // Test cases for getPlayingPlayer method
  @Test
  public void testGetPlayingPlayer() {
    // Typical use case: Playing player is player1
    Game.playingPlayer = 1;
    Player player = Game.getPlayingPlayer();
    assertEquals(Game.player1, player);
    
    // Typical use case: Playing player is player2
    Game.playingPlayer = 2;
    player = Game.getPlayingPlayer();
    assertEquals(Game.player2, player);
  }
  
  // Test cases for getNonPlayingPlayer method
  @Test
  public void testGetNonPlayingPlayer() {
    // Typical use case: Playing player is player1, so non-playing player is player2
    Game.playingPlayer = 1;
    Player player = Game.getNonPlayingPlayer();
    assertEquals(Game.player2, player);
    
    // Typical use case: Playing player is player2, so non-playing player is player1
    Game.playingPlayer = 2;
    player = Game.getNonPlayingPlayer();
    assertEquals(Game.player1, player);
  }
  
  // Test cases for getOtherPlayer method
  @Test
  public void testGetOtherPlayer() {
    // Typical use case: Input player is player1, so other player is player2
    Player player = Game.getOtherPlayer(Game.player1);
    assertEquals(Game.player2, player);
    
    // Typical use case: Input player is player2, so other player is player1
    player = Game.getOtherPlayer(Game.player2);
    assertEquals(Game.player1, player);
  }
  
  // Test cases for init method
  @Test
  public void testInit() {
    // Typical use case: Initialize the game with valid inputs
    UserInterface IP1 = new UserInterface();
    UserInterface IP2 = new UserInterface();
    String n1 = "Player 1";
    String n2 = "Player 2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    
    Game.init(IP1, IP2, n1, n2, deck1, deck2);
    
    assertEquals(IP1, Game.player1.IP);
    assertEquals(IP2, Game.player2.IP);
    assertEquals(n1, Game.player1.name);
    assertEquals(n2, Game.player2.name);
    assertEquals(deck1, Game.player1.deck);
    assertEquals(deck2, Game.player2.deck);
    assertEquals(1, Game.playingPlayer);
    assertEquals(UntapPhase.class, Game.currentPhase.getClass());
  }
  
  // Test cases for givePriorityToCurrentPlayer method
  @Test
  public void testGivePriorityToCurrentPlayer() {
    // Typical use case: Current player gets priority and stack is empty
    Game.stack.empty();
    
    // Mocking letPlayerSpeak method of Player class to return true
    Game.player1.letPlayerSpeak() -> true;
    Game.givePriorityToCurrentPlayer();
    
    // Verify that stack is still empty
    assertEquals(true, Game.stack.isEmpty());
    
    // Typical use case: Current player gets priority and stack is not empty
    Game.stack.push(new Card());
    
    // Mocking letPlayerSpeak method of Player class to return true
    Game.player1.letPlayerSpeak() -> true;
    Game.givePriorityToCurrentPlayer();
    
    // Verify that stack has been resolved and is empty
    assertEquals(true, Game.stack.isEmpty());
    
    // Error scenario: Current player and non-playing player both cannot speak
    Game.stack.push(new Card());
    
    // Mocking letPlayerSpeak method of Player class to return false for both players
    Game.player1.letPlayerSpeak() -> false;
    Game.player2.letPlayerSpeak() -> false;
    Game.givePriorityToCurrentPlayer();
    
    // Verify that stack is still not empty
    assertEquals(false, Game.stack.isEmpty());
  }
}
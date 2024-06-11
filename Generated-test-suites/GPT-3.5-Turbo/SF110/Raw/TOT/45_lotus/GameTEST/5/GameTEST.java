import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.card.CardCollection;

public class GameTest {
  
  private UserInterfaceMock interface1;
  private UserInterfaceMock interface2;
  
  @Before
  public void setUp() {
    interface1 = new UserInterfaceMock();
    interface2 = new UserInterfaceMock();
  }
  
  // Test for getPlayingPlayer()
  @Test
  public void testGetPlayingPlayer() {
    Game.init(interface1, interface2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    assertEquals("Player 1", Game.getPlayingPlayer().getName());
  }
  
  // Test for getNonPlayingPlayer()
  @Test
  public void testGetNonPlayingPlayer() {
    Game.init(interface1, interface2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    assertEquals("Player 2", Game.getNonPlayingPlayer().getName());
  }
  
  // Test for getOtherPlayer()
  @Test
  public void testGetOtherPlayer() {
    Game.init(interface1, interface2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    assertEquals("Player 2", Game.getOtherPlayer(Game.player1).getName());
    assertEquals("Player 1", Game.getOtherPlayer(Game.player2).getName());
  }
  
  // Test for init()
  @Test
  public void testInit() {
    Game.init(interface1, interface2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    assertEquals("Player 1", Game.player1.getName());
    assertEquals("Player 2", Game.player2.getName());
    assertEquals(1, Game.playingPlayer);
    assertNotNull(Game.currentPhase);
    assertNotNull(Game.stack);
  }
  
  // Test for givePriorityToCurrentPlayer()
  @Test
  public void testGivePriorityToCurrentPlayer() {
    Game.init(interface1, interface2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    
    // Mock player interfaces to return true for letPlayerSpeak()
    interface1.setLetPlayerSpeak(true);
    interface2.setLetPlayerSpeak(true);
    
    Game.givePriorityToCurrentPlayer();
    
    // Assert that both players were able to speak
    assertTrue(interface1.isLetPlayerSpeakCalled());
    assertTrue(interface2.isLetPlayerSpeakCalled());
    
    // Mock player interfaces to return false for letPlayerSpeak()
    interface1.setLetPlayerSpeak(false);
    interface2.setLetPlayerSpeak(true);
    
    Game.givePriorityToCurrentPlayer();
    
    // Assert that only the non-playing player was able to speak
    assertTrue(interface2.isLetPlayerSpeakCalled());
    assertFalse(interface1.isLetPlayerSpeakCalled());
    
    // Mock player interfaces to return false for letPlayerSpeak()
    interface1.setLetPlayerSpeak(false);
    interface2.setLetPlayerSpeak(false);
    
    Game.givePriorityToCurrentPlayer();
    
    // Assert that both players were unable to speak and the stack was resolved
    assertTrue(interface1.isLetPlayerSpeakCalled());
    assertTrue(interface2.isLetPlayerSpeakCalled());
    assertFalse(Game.stack.empty());
  }
}

class UserInterfaceMock implements UserInterface {
  
  private boolean letPlayerSpeakCalled;
  
  UserInterfaceMock() {
    letPlayerSpeakCalled = false;
  }
  
  @Override
  public void init(Player player) {
    // Mock method
  }
  
  @Override
  public boolean letPlayerSpeak() {
    letPlayerSpeakCalled = true;
    return true;
  }
  
  public boolean isLetPlayerSpeakCalled() {
    return letPlayerSpeakCalled;
  }
  
  public void setLetPlayerSpeak(boolean letPlayerSpeakCalled) {
    this.letPlayerSpeakCalled = letPlayerSpeakCalled;
  }
}
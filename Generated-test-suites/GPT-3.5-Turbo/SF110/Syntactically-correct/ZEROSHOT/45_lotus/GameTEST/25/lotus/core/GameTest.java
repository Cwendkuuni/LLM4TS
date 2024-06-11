import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {
  
  private UserInterface IP1;
  private UserInterface IP2;
  private String n1;
  private String n2;
  private CardCollection deck1;
  private CardCollection deck2;
  
  @Before
  public void setUp() {
    IP1 = new UserInterface();
    IP2 = new UserInterface();
    n1 = "Player1";
    n2 = "Player2";
    deck1 = new CardCollection();
    deck2 = new CardCollection();
  }
  
  @Test
  public void testGetPlayingPlayer() {
    Player player1 = Game.player1;
    Player result = Game.getPlayingPlayer();
    
    assertEquals(player1, result);
  }
  
  @Test
  public void testGetNonPlayingPlayer() {
    Player player2 = Game.player2;
    Player result = Game.getNonPlayingPlayer();
    
    assertEquals(player2, result);
  }
  
  @Test
  public void testGetOtherPlayer() {
    Player player1 = Game.player1;
    Player result = Game.getOtherPlayer(Game.player2);
    
    assertEquals(player1, result);
  }
  
  @Test
  public void testInit() {
    Game.init(IP1, IP2, n1, n2, deck1, deck2);
    
    assertEquals(IP1, Game.player1.getInterface());
    assertEquals(IP2, Game.player2.getInterface());
    assertEquals(n1, Game.player1.getName());
    assertEquals(n2, Game.player2.getName());
    
    for (Card c : deck1) {
      assertEquals(Game.player1, c.getOwner());
      assertEquals(Game.player1.getLibrary(), c.getZone());
    }
    
    for (Card c : deck2) {
      assertEquals(Game.player2, c.getOwner());
      assertEquals(Game.player2.getLibrary(), c.getZone());
    }
  }
  
  @Test
  public void testGivePriorityToCurrentPlayer() {
    // To be implemented
  }
}
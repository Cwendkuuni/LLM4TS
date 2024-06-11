import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.card.*;
import lotus.core.phases.*;
import lotus.core.stack.*;
import java.util.*;

public class GameTest {
   
   @Test
   public void testGetPlayingPlayer() {
      // Test when playingPlayer is 1
      Game.playingPlayer = 1;
      assertEquals(Game.player1, Game.getPlayingPlayer());
      
      // Test when playingPlayer is 2
      Game.playingPlayer = 2;
      assertEquals(Game.player2, Game.getPlayingPlayer());
   }
   
   @Test
   public void testGetNonPlayingPlayer() {
      // Test when playingPlayer is 1
      Game.playingPlayer = 1;
      assertEquals(Game.player2, Game.getNonPlayingPlayer());
      
      // Test when playingPlayer is 2
      Game.playingPlayer = 2;
      assertEquals(Game.player1, Game.getNonPlayingPlayer());
   }
   
   @Test
   public void testGetOtherPlayer() {
      Player p1 = Game.player1;
      Player p2 = Game.player2;
      
      // Test when p is player1
      assertEquals(p2, Game.getOtherPlayer(p1));
      
      // Test when p is player2
      assertEquals(p1, Game.getOtherPlayer(p2));
   }
   
   @Test
   public void testInit() {
      UserInterface IP1 = new UserInterface();
      UserInterface IP2 = new UserInterface();
      String n1 = "player1";
      String n2 = "player2";
      CardCollection deck1 = new CardCollection();
      CardCollection deck2 = new CardCollection();
      Game.init(IP1, IP2, n1, n2, deck1, deck2);
      
      // Assert player1 and player2 are initialized correctly
      assertEquals(n1, Game.player1.getName());
      assertEquals(deck1, Game.player1.getDeck());
      assertEquals(n2, Game.player2.getName());
      assertEquals(deck2, Game.player2.getDeck());
      
      // Assert init is called on UserInterfaces
      assertTrue(IP1.isInitCalled());
      assertTrue(IP2.isInitCalled());
      
      // Assert playingPlayer and currentPhase are set correctly
      assertEquals(1, Game.playingPlayer);
      assertTrue(Game.currentPhase instanceof UntapPhase);
      
      // Assert cards in deck1 have correct owner and zone
      for (Card c : deck1) {
         assertEquals(Game.player1, c.owner);
         assertEquals(Game.player1.library, c.zone);
      }
      
      // Assert cards in deck2 have correct owner and zone
      for (Card c : deck2) {
         assertEquals(Game.player2, c.owner);
         assertEquals(Game.player2.library, c.zone);
      }
   }
   
   @Test
   public void testGivePriorityToCurrentPlayer() {
      UserInterface IP1 = new UserInterface();
      UserInterface IP2 = new UserInterface();
      String n1 = "player1";
      String n2 = "player2";
      CardCollection deck1 = new CardCollection();
      CardCollection deck2 = new CardCollection();
      Game.init(IP1, IP2, n1, n2, deck1, deck2);
      
      // Test when no cards in stack
      Game.givePriorityToCurrentPlayer();
      assertNull(Game.stack.resolveLast());
      
      // Test when cards in stack
      Game.stack.push(new Card());
      Game.givePriorityToCurrentPlayer();
      assertNotNull(Game.stack.resolveLast());
   }
}
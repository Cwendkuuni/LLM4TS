import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits() {
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
    Node n = new Node(Token.RETURN);
    assertFalse(minimizeExitPoints.tryMinimizeExits(n, Token.CONTINUE, null));
    
    n = new Node(Token.RETURN);
    assertTrue(minimizeExitPoints.tryMinimizeExits(n, Token.RETURN, null));
  }
  
  @Test
  public void testMatchingExitNode() {
    Node n = new Node(Token.RETURN);
    assertTrue(MinimizeExitPoints.matchingExitNode(n, Token.RETURN, null));
    
    n = new Node(Token.RETURN);
    assertFalse(MinimizeExitPoints.matchingExitNode(n, Token.CONTINUE, null));
    
    n = new Node(Token.BREAK);
    assertTrue(MinimizeExitPoints.matchingExitNode(n, Token.BREAK, null));
    
    n = new Node(Token.BREAK, new Node(Token.NAME, "label"));
    assertTrue(MinimizeExitPoints.matchingExitNode(n, Token.BREAK, "label"));
    
    n = new Node(Token.BREAK, new Node(Token.NAME, "label2"));
    assertFalse(MinimizeExitPoints.matchingExitNode(n, Token.BREAK, "label"));
  }
  
  @Test
  public void testMoveAllFollowing() {
    Node start = new Node(Token.NAME, "start");
    Node srcParent = new Node(Token.NAME, "srcParent");
    Node destParent = new Node(Token.NAME, "destParent");
    srcParent.addChildToBack(start);
    Node child1 = new Node(Token.NAME, "child1");
    srcParent.addChildToBack(child1);
    Node child2 = new Node(Token.NAME, "child2");
    srcParent.addChildToBack(child2);
    
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    
    assertEquals(0, srcParent.getChildCount());
    assertEquals(2, destParent.getChildCount());
    assertSame(child1, destParent.getFirstChild());
    assertSame(child2, destParent.getLastChild());
  }

}
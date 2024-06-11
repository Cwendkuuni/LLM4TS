import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

  // Test case for tryMinimizeExits() method
  @Test
  public void testTryMinimizeExits() {
    MinimizeExitPoints obj = new MinimizeExitPoints();
    
    // Test case 1: n is a matching exit node, so it should be removed
    Node n1 = new Node(Token.RETURN);
    Node parent1 = new Node(Token.BLOCK);
    parent1.addChildToBack(n1);
    obj.tryMinimizeExits(n1, Token.RETURN, null);
    assertEquals(0, parent1.getChildCount());
    
    // Test case 2: n is not a matching exit node, so it should not be removed
    Node n2 = new Node(Token.CONTINUE);
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToBack(n2);
    obj.tryMinimizeExits(n2, Token.RETURN, null);
    assertEquals(1, parent2.getChildCount());
  }

  // Test case for tryMinimizeIfBlockExits() method
  @Test
  public void testTryMinimizeIfBlockExits() {
    MinimizeExitPoints obj = new MinimizeExitPoints();
    
    // Test case 1: srcBlock has no children, so nothing should be moved
    Node srcBlock1 = new Node(Token.BLOCK);
    Node dstBlock1 = new Node(Token.BLOCK);
    Node ifNode1 = new Node(Token.IF);
    ifNode1.addChildToBack(srcBlock1);
    obj.tryMinimizeIfBlockExits(srcBlock1, dstBlock1, ifNode1, Token.RETURN, null);
    assertEquals(0, dstBlock1.getChildCount());
    
    // Test case 2: srcBlock has children, so they should be moved to dstBlock
    Node srcBlock2 = new Node(Token.BLOCK);
    srcBlock2.addChildToBack(new Node(Token.IF));
    Node dstBlock2 = new Node(Token.BLOCK);
    Node ifNode2 = new Node(Token.IF);
    ifNode2.addChildToBack(srcBlock2);
    obj.tryMinimizeIfBlockExits(srcBlock2, dstBlock2, ifNode2, Token.RETURN, null);
    assertEquals(1, dstBlock2.getChildCount());
    
    // Test case 3: dstBlock is null, so nothing should be moved
    Node srcBlock3 = new Node(Token.BLOCK);
    srcBlock3.addChildToBack(new Node(Token.IF));
    Node ifNode3 = new Node(Token.IF);
    ifNode3.addChildToBack(srcBlock3);
    obj.tryMinimizeIfBlockExits(srcBlock3, null, ifNode3, Token.RETURN, null);
    assertEquals(1, ifNode3.getChildCount());
  }

  // Test case for matchingExitNode() method
  @Test
  public void testMatchingExitNode() {
    boolean result;
    
    // Test case 1: n is a matching exit node (return without expression)
    Node n1 = new Node(Token.RETURN);
    result = obj.matchingExitNode(n1, Token.RETURN, null);
    assertTrue(result);
    
    // Test case 2: n is not a matching exit node (return with expression)
    Node n2 = new Node(Token.RETURN);
    n2.addChildToBack(new Node(Token.NAME, "foo"));
    result = obj.matchingExitNode(n2, Token.RETURN, null);
    assertFalse(result);
    
    // Test case 3: n is a matching exit node (break without label)
    Node n3 = new Node(Token.BREAK);
    result = obj.matchingExitNode(n3, Token.BREAK, null);
    assertTrue(result);
    
    // Test case 4: n is not a matching exit node (break with label)
    Node n4 = new Node(Token.BREAK);
    n4.addChildToBack(new Node(Token.LABEL_NAME, "loop"));
    result = obj.matchingExitNode(n4, Token.BREAK, null);
    assertFalse(result);
    
    // Test case 5: n is a matching exit node (break with label)
    Node n5 = new Node(Token.BREAK);
    n5.addChildToBack(new Node(Token.LABEL_NAME, "loop"));
    result = obj.matchingExitNode(n5, Token.BREAK, "loop");
    assertTrue(result);
  }

  // Test case for moveAllFollowing() method
  @Test
  public void testMoveAllFollowing() {
    Node start = new Node();
    Node srcParent = new Node(Token.BLOCK);
    Node destParent = new Node(Token.BLOCK);
    
    // Test case 1: start is the last child of srcParent, so nothing should be moved
    srcParent.addChildToBack(start);
    obj.moveAllFollowing(start, srcParent, destParent);
    assertEquals(0, destParent.getChildCount());
    
    // Test case 2: start is not the last child of srcParent, so it should be moved to destParent
    Node sibling = new Node();
    srcParent.addChildToBack(sibling);
    srcParent.addChildToBack(start);
    obj.moveAllFollowing(start, srcParent, destParent);
    assertEquals(1, destParent.getChildCount());
  }
}
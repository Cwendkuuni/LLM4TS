import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits() {
    // Edge case: testing with empty node
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    mep.tryMinimizeExits(IR.block().srcref("test"), Token.RETURN, null);
    // No assertion as this is void method, should not throw any exceptions
  }

  @Test
  public void testMatchingExitNode() {
    // Typical use case: matching return node
    Node returnNode = IR.returnNode();
    assertTrue(MinimizeExitPoints.matchingExitNode(returnNode, Token.RETURN, null));

    // Edge case: matching break node with label
    Node breakNodeWithLabel = new Node(Token.BREAK, new Node(Token.STRING, "test"));
    assertTrue(MinimizeExitPoints.matchingExitNode(breakNodeWithLabel, Token.BREAK, "test"));

    // Error scenario: non-matching node
    Node nonMatchingNode = new Node(Token.CONTINUE);
    assertFalse(MinimizeExitPoints.matchingExitNode(nonMatchingNode, Token.RETURN, null));
  }

  @Test
  public void testMoveAllFollowing() {
    // Typical use case: moving all nodes from srcParent to destParent
    Node srcParent = IR.block(IR.empty());
    Node destParent = IR.block(IR.empty());
    Node node1 = IR.empty();
    Node node2 = IR.empty();
    srcParent.addChildToBack(node1);
    srcParent.addChildToBack(node2);

    MinimizeExitPoints.moveAllFollowing(srcParent.getFirstChild(), srcParent, destParent);

    assertNull(srcParent.getFirstChild());
    assertEquals(node1, destParent.getFirstChild());
    assertEquals(node2, destParent.getLastChild());
  }

}
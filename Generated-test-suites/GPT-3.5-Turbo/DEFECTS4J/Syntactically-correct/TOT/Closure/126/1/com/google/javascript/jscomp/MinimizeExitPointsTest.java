import org.junit.Assert;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits() {
    // Typical use case: for loop with break
    MinimizeExitPoints tester = new MinimizeExitPoints();
    NodeTraversal t = new NodeTraversal();
    Node loopNode = new Node(Token.FOR);
    Node loopBlock = new Node(Token.BLOCK);

    loopNode.addChildToBack(loopBlock);

    // Add break statement
    Node breakNode = new Node(Token.BREAK);
    loopBlock.addChildToBack(breakNode);

    tester.tryMinimizeExits(loopNode, Token.BREAK, null);

    // Assert that the break statement is removed
    Assert.assertFalse(loopBlock.hasChildren());
  }

  @Test
  public void testTryMinimizeIfBlockExits() {
    // Edge case: single statement if block
    MinimizeExitPoints tester = new MinimizeExitPoints();
    Node ifNode = new Node(Token.IF);
    Node trueBlock = new Node(Token.BLOCK);
    Node falseBlock = new Node(Token.BLOCK);

    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(falseBlock);

    // Add statement to true block
    Node trueStatement = new Node(Token.NAME);
    trueBlock.addChildToBack(trueStatement);

    // Add exit statement to true block
    Node exitNode = new Node(Token.RETURN);
    trueBlock.addChildToBack(exitNode);

    // Add another statement to true block
    Node additionalStatement = new Node(Token.NAME);
    trueBlock.addChildToBack(additionalStatement);

    tester.tryMinimizeIfBlockExits(trueBlock, falseBlock, ifNode, Token.RETURN, null);

    // Assert that the exit node is removed and the additional statement is moved to the false block
    Assert.assertFalse(trueBlock.hasChildren());
    Assert.assertTrue(falseBlock.hasChildren());
    Assert.assertEquals(additionalStatement, falseBlock.getFirstChild());
  }

  @Test
  public void testMatchingExitNode() {
    // Error scenario: non-matching exit node
    Node exitNode = new Node(Token.RETURN);

    boolean result = MinimizeExitPoints.matchingExitNode(exitNode, Token.BREAK, null);

    // Assert that the exit node does not match the specified block-exit type
    Assert.assertFalse(result);
  }

  @Test
  public void testMoveAllFollowing() {
    // Error scenario: moving child nodes
    Node srcParent = new Node(Token.BLOCK);
    Node destParent = new Node(Token.BLOCK);
    Node start = new Node(Token.NAME);
    srcParent.addChildToBack(start);

    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);

    // Assert that the child nodes are moved from srcParent to destParent
    Assert.assertFalse(srcParent.hasChildren());
    Assert.assertTrue(destParent.hasChildren());
    Assert.assertEquals(start, destParent.getFirstChild());
  }
}
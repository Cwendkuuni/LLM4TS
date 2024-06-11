import org.junit.Test;

import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits_ExitNodeRemoved_ReturnsTrue() {
    // Arrange
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();

    // Act
    boolean result = minimizeExitPoints.tryMinimizeExits(new Node(), Token.RETURN, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testTryMinimizeExits_ExitNodeNotRemoved_ReturnsFalse() {
    // Arrange
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();

    // Act
    boolean result = minimizeExitPoints.tryMinimizeExits(new Node(), Token.BREAK, "label");

    // Assert
    assertFalse(result);
  }

  @Test
  public void testTryMinimizeExits_IfBlockWithSingleTrueBlock_ExitsRemoved() {
    // Arrange
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
    Node trueBlock = new Node();
    Node ifNode = new Node();
    ifNode.addChildToFront(trueBlock);

    // Act
    minimizeExitPoints.tryMinimizeIfBlockExits(trueBlock, null, ifNode, Token.BREAK, "label");

    // Assert
    assertFalse(ifNode.hasChildren());
  }

  @Test
  public void testTryMinimizeExits_IfBlockWithTrueAndFalseBlock_ExitsRemoved() {
    // Arrange
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
    Node trueBlock = new Node();
    Node falseblock = new Node();
    Node ifNode = new Node();
    ifNode.addChildToFront(trueBlock);
    ifNode.addChildToBack(falseblock);

    // Act
    minimizeExitPoints.tryMinimizeIfBlockExits(trueBlock, falseblock, ifNode, Token.CONTINUE, null);

    // Assert
    assertFalse(ifNode.hasChildren());
  }

  @Test
  public void testMatchingExitNode_MatchingExitNode_ReturnsTrue() {
    // Arrange
    Node n = new Node(Token.RETURN);

    // Act
    boolean result = MinimizeExitPoints.matchingExitNode(n, Token.RETURN, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testMatchingExitNode_NonMatchingExitNode_ReturnsFalse() {
    // Arrange
    Node n = new Node(Token.BREAK);

    // Act
    boolean result = MinimizeExitPoints.matchingExitNode(n, Token.CONTINUE, null);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testMatchingExitNode_MatchingExitNodeWithLabel_ReturnsTrue() {
    // Arrange
    Node n = new Node(Token.BREAK);
    n.addChildToFront(new Node("label"));

    // Act
    boolean result = MinimizeExitPoints.matchingExitNode(n, Token.BREAK, "label");

    // Assert
    assertTrue(result);
  }

  @Test
  public void testMatchingExitNode_NonMatchingExitNodeWithLabel_ReturnsFalse() {
    // Arrange
    Node n = new Node(Token.BREAK);
    n.addChildToFront(new Node("otherLabel"));

    // Act
    boolean result = MinimizeExitPoints.matchingExitNode(n, Token.BREAK, "label");

    // Assert
    assertFalse(result);
  }

  @Test
  public void testMoveAllFollowing_ChildNodesMovedToDestination_SuccessfullyMoved() {
    // Arrange
    Node start = new Node();
    Node srcParent = new Node();
    srcParent.addChildToBack(start);

    Node destParent = new Node();

    // Act
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);

    // Assert
    assertEquals(0, srcParent.getChildCount());
    assertEquals(1, destParent.getChildCount());
    assertEquals(start, destParent.getFirstChild());
  }

  @Test
  public void testMoveAllFollowing_LastChildNodeMovedToDestination_SuccessfullyMoved() {
    // Arrange
    Node start = new Node();
    Node srcParent = new Node();
    srcParent.addChildToBack(start);

    Node destParent = new Node();

    // Act
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);

    // Assert
    assertEquals(0, srcParent.getChildCount());
    assertEquals(1, destParent.getChildCount());
    assertEquals(start, destParent.getFirstChild());
  }

}
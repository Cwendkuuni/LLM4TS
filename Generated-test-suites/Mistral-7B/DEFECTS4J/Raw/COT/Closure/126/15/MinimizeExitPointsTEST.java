package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    Node root = createTestNodeForMethodCalls("test1", "test2");
    minimizeExitPoints.process(null, root);

    // Then
    verifyExitsAreMinimized(root);
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    Node root = createTestNodeForLabeledStatements("label1: if (true) break label1;", "test");
    minimizeExitPoints.process(null, root);

    // Then
    verifyExitsAreMinimized(root, "label1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinimizeExitPointsThrowsIllegalArgumentExceptionForNullCompiler() {
    // Given
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);

    // When
    Node root = createTestNodeForMethodCalls("test1", "test2");

    // Then
    minimizeExitPoints.process(null, root);
  }

  @Test(expected = NullPointerException.class)
  public void testMinimizeExitPointsThrowsNullPointerExceptionForNullRoot() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinimizeExitPointsThrowsIllegalArgumentExceptionForNullExterns() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, createTestNodeForMethodCalls("test1", "test2"));

    // Then
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    minimizeExitPoints.process(null, null);
    when(compiler).reportCodeChange();
    minimizeExitPoints.process(null, nodeCaptor.capture());
    assertNull(nodeCaptor.getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinimizeExitPointsThrowsIllegalArgumentExceptionForNullParentNode() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, null);

    // Then
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    minimizeExitPoints.process(null, nodeCaptor.capture());
    assertNull(nodeCaptor.getValue());
  }

  // Helper methods for test cases

  private Node createTestNodeForMethodCalls(String methodName1, String methodName2) {
    // Create a test node with method calls
    Node externs = createTestExterns();
    Node functionNode = createTestFunctionNode("function test() { " +
        "if (true) return; " +
        "if (true) return; " +
        "}" +
        "test1(); " +
        "test2(); ");
    Node root = new Node(Token.PROGRAM, null, externs, functionNode);
    return root;
  }

  private Node createTestNodeForLabeledStatements(String labelStatement, String labelName) {
    // Create a test node with labeled statements
    Node externs = createTestExterns();
    Node functionNode = new Node(Token.FUNCTION, null, null,
        new Node(Token.IDENT, labelName),
        new Node(Token.BLOCK, null,
          new Node(Token.IF, null, null,
            new Node(Token.BOOLEAN_LITERAL, false),
            new Node(Token.LABEL, labelName),
            new Node(Token.BREAK, null, null, labelName),
            null
          ),
          null
        ),
        null
    );
    Node root = new Node(Token.PROGRAM, null, externs, functionNode);
    return root;
  }

  private Node createTestNodeForUnreachableExit() {
    // Create a test node with unreachable exit
    Node externs = createTestExterns();
    Node functionNode = new Node(Token.FUNCTION, null, null,
        null,
        new Node(Token.BLOCK, null,
          new Node(Token.IF, null, null,
            new Node(Token.BOOLEAN_LITERAL, false),
            null,
            new Node(Token.RETURN, null, null),
            null
          ),
          new Node(Token.BREAK, null, null)
        ),
        null
    );
    Node root = new Node(Token.PROGRAM, null, externs, functionNode);
    return root;
  }

  private Node createTestExterns() {
    return new Node(Token.PROGRAM, null, null, null);
  }

  private Node createTestFunctionNode(String nodeCode) {
    return new Node(Token.FUNCTION, null, null,
        null,
        new Node(Token.BLOCK, null, NodeUtil.parseNode(nodeCode))
    );
  }

  private void verifyExitsAreMinimized(Node root, String labelName) {
    if (labelName != null) {
      Node exitNode = findNodeByLabel(root, labelName);
      assertNull("Exit with label should not be present.", exitNode);
    }
    Node exitNode = findFirstNodeWithType(root, Token.RETURN);
    assertNull("Return statements should be removed.", exitNode);
    Node exitNode1 = findFirstNodeWithType(root, Token.BREAK);
    assertNull("Break statements should be removed.", exitNode1);
    Node exitNode2 = findFirstNodeWithType(root, Token.CONTINUE);
    assertNull("Continue statements should be moved.", exitNode2);
  }

  private void verifyExitsAreMinimized(Node root) {
    verifyExitsAreMinimized(root, null);
  }

  private Node findFirstNodeWithType(Node node, int nodeType) {
    if (node.getType() == nodeType) {
      return node;
    }
    for (Node child : node.children()) {
      Node result = findFirstNodeWithType(child, nodeType);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findNodeByLabel(Node node, String labelName) {
    if (node.isLabel()) {
      if (node.getFirstChild().getString().equals(labelName)) {
        return node;
      }
    }
    for (Node child : node.children()) {
      Node result = findNodeByLabel(child, labelName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}
package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case for dead assignment elimination with simple assignments
    String sourceCode = "function test() {" +
        "  var x = 1, y = 1, z = 1;" +
        "  x = 2;" +
        "  y = 3;" +
        "  z = 4;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    Node root = AbstractNodeTraverser.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    assertEquals(3, root.getChildCount());

    Node xAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "x"), new Node(Token.NUM, 2)});
    assertNull(xAssignment);

    Node yAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "y"), new Node(Token.NUM, 3)});
    assertNull(yAssignment);

    Node zAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "z"), new Node(Token.NUM, 4)});
    assertNull(zAssignment);

    // Test case for dead assignment elimination with compound assignments
    sourceCode = "function test() {" +
        "  var x = 1, y = 1, z = 1;" +
        "  x += 1;" +
        "  y -= 1;" +
        "  z *= 1;" +
        "}";

    root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    assertEquals(3, root.getChildCount());

    xAssignment = findNodeByTypeAndChildren(root, Token.ADD_ASSIGN,
        new Node[]{new Node(Token.NAME, "x"), new Node(Token.NUM, 1)});
    assertNull(xAssignment);

    yAssignment = findNodeByTypeAndChildren(root, Token.SUB_ASSIGN,
        new Node[]{new Node(Token.NAME, "y"), new Node(Token.NUM, 1)});
    assertNull(yAssignment);

    zAssignment = findNodeByTypeAndChildren(root, Token.MUL_ASSIGN,
        new Node[]{new Node(Token.NAME, "z"), new Node(Token.NUM, 1)});
    assertNull(zAssignment);

    // Test case for dead assignment elimination with increment and decrement operators
    sourceCode = "function test() {" +
        "  var x = 1, y = 1, z = 1;" +
        "  x++;" +
        "  y--;" +
        "  z++;" +
        "}";

    root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    assertEquals(3, root.getChildCount());

    xAssignment = findNodeByTypeAndChildren(root, Token.INC, new Node[]{new Node(Token.NAME, "x")});
    assertNull(xAssignment);

    yAssignment = findNodeByTypeAndChildren(root, Token.DEC, new Node[]{new Node(Token.NAME, "y")});
    assertNull(yAssignment);

    zAssignment = findNodeByTypeAndChildren(root, Token.INC, new Node[]{new Node(Token.NAME, "z")});
    assertNull(zAssignment);

    // Test case for dead assignment elimination with multiple assignments and conditional statements
    sourceCode = "function test() {" +
        "  var x = 1, y = 1, z = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  } else {" +
        "    y = 3;" +
        "  }" +
        "  z = 4;" +
        "}";

    root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    assertEquals(4, root.getChildCount());

    xAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "x"), new Node(Token.NUM, 2)});
    assertNull(xAssignment);

    yAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "y"), new Node(Token.NUM, 3)});
    assertNull(yAssignment);

    zAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "z"), new Node(Token.NUM, 4)});
    assertNull(zAssignment);

    // Test case for dead assignment elimination with loops
    sourceCode = "function test() {" +
        "  var x = 1, y = 1, z = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x = 2;" +
        "  }" +
        "  y = 3;" +
        "  z = 4;" +
        "}";

    root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    assertEquals(4, root.getChildCount());

    xAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "x"), new Node(Token.NUM, 2)});
    assertNull(xAssignment);

    yAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "y"), new Node(Token.NUM, 3)});
    assertNull(yAssignment);

    zAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "z"), new Node(Token.NUM, 4)});
    assertNull(zAssignment);

    // Test case for dead assignment elimination with functions
    sourceCode = "function test() {" +
        "  function foo() {" +
        "    var x = 1, y = 1, z = 1;" +
        "    x = 2;" +
        "  }" +
        "  foo();" +
        "  y = 3;" +
        "  z = 4;" +
        "}";

    root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    assertEquals(4, root.getChildCount());

    xAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "x"), new Node(Token.NUM, 2)});
    assertNull(xAssignment);

    yAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "y"), new Node(Token.NUM, 3)});
    assertNull(yAssignment);

    zAssignment = findNodeByTypeAndChildren(root, Token.ASSIGN,
        new Node[]{new Node(Token.NAME, "z"), new Node(Token.NUM, 4)});
    assertNull(zAssignment);
  }

  private Node findNodeByTypeAndChildren(Node node, int type, Node... nodes) {
    for (Node currentNode = node; currentNode != null;
         currentNode = currentNode.getNext()) {
      if (currentNode.getType() == type) {
        for (int i = 0; i < nodes.length; i++) {
          if (NodeUtil.isSequenceEqual(nodes[i], currentNode)) {
            return currentNode;
          }
        }
      }
    }
    return null;
  }
}
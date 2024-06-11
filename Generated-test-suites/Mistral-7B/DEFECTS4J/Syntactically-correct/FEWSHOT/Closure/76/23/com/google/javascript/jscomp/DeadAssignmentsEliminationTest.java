package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testTryRemoveAssignment() {
    // Test case 1: dead assignment with no reads before
    String code1 = "var x = 1, y = x;";
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(null);
    Node root = ParserUtil.parse(code1);
    eliminator.process(null, root);
    assertTrue( NodeUtil.isNull(root.getFirstChild(Node.CHILD_ONLY)) );

    // Test case 2: dead assignment with reads before
    String code2 = "var x = 1, y = x, print(x);";
    root = ParserUtil.parse(code2);
    eliminator.process(null, root);
    assertFalse( NodeUtil.isNull(root.getFirstChild(Node.CHILD_ONLY)) );

    // Test case 3: dead assignment inside conditional statement
    String code3 = "if (x = 1) { y = x; }";
    root = ParserUtil.parse(code3);
    eliminator.process(null, root);
    assertTrue( NodeUtil.isNull(root.getFirstChild(Node.CHILD_ONLY)) );

    // Test case 4: dead assignment inside loop
    String code4 = "for (var i = 0; i < 10; i++) { i = i + 1; }";
    root = ParserUtil.parse(code4);
    eliminator.process(null, root);
    assertTrue( NodeUtil.isNull(root.getFirstChild(Node.CHILD_ONLY)) );

    // Test case 5: dead assignment inside function
    String code5 = "function foo() { var x = 1, y = x; }";
    root = ParserUtil.parse(code5);
    eliminator.process(null, root);
    assertTrue( NodeUtil.isNull(root.getLastChild().getLastChild(Node.CHILD_ONLY)) );

    // Test case 6: dead assignment inside switch statement
    String code6 = "switch (x) { case 1: y = x; }";
    root = ParserUtil.parse(code6);
    eliminator.process(null, root);
    assertTrue( NodeUtil.isNull(root.getFirstChild(Node.CHILD_ONLY).getLastChild(Node.CHILD_ONLY)) );
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // Test case 1: variable is dead
    Node n1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUM, 1));
    Node exprRoot1 = new Node(Token.ASSIGN, new Node(Token.NAME, "y"), n1);
    assertTrue( DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n1, exprRoot1, "x") );

    // Test case 2: variable is still live
    Node n2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "x"));
    Node exprRoot2 = new Node(Token.ASSIGN, new Node(Token.NAME, "y"), new Node(Token.NAME, "x"));
    assertTrue( DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n2, exprRoot2, "x") );

    // Test case 3: variable is dead but reads are after the write
    Node n3 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUM, 1));
    Node exprRoot3 = new Node(Token.ASSIGN, new Node(Token.NAME, "y"), new Node(Token.NAME, "x"), new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUM, 2)));
    assertFalse( DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n3, exprRoot3, "x") );

    // Test case 4: variable is still live because it is read before the write
    Node n4 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUM, 1));
    Node exprRoot4 = new Node(Token.ASSIGN, new Node(Token.NAME, "y"), new Node(Token.NAME, "x"), new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUM, 2)), new Node(Token.NAME, "x"));
    assertTrue( DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n4, exprRoot4, "x") );
  }
}
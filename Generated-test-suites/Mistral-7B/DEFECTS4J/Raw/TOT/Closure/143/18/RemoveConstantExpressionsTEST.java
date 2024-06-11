import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.ParallelCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Result;
import com.google.javascript.rhino.Token;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RemoveConstantExpressionsTest {

  private static final Node createNode(int type, int lineNumber) {
    Node node = new Node(type, lineNumber);
    node.setParent(null);
    return node;
  }

  private static final Node createBinaryOperatorNode(int operatorType, Node left, Node right) {
    Node node = new Node(operatorType, 0);
    node.setParent(null);
    node.setFirstChild(left);
    node.setLastChild(right);
    return node;
  }

  private static final Node createUnaryOperatorNode(int operatorType, Node operand) {
    Node node = new Node(operatorType, 0);
    node.setParent(null);
    node.setFirstChild(operand);
    return node;
  }

  @org.junit.Test
  public void testRemoveConstantExpressions() {
    Compiler compiler = new Compiler();
    Node root = createNode(EXPR_STATEMENT, 0);

    // Basic test case
    Node expr1 = createBinaryOperatorNode(ADD, createNumberNode(1, 1),
        createCallExpressionNode(createIdentifier("foo"), Arrays.asList(createNull()), 1));
    Node expr2 = createBinaryOperatorNode(ADD, createCallExpressionNode(createIdentifier("bar"), Arrays.asList(createNull()), 1), expr1);
    Node exprStatement1 = createNode(EXPR_STATEMENT, 0);
    exprStatement1.setFirstChild(expr2);
    root.setFirstChild(exprStatement1);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, root);
    assertEquals(2, root.getChildCount());
    Node expectedExprStatement1 = createNode(EXPR_STATEMENT, 0);
    Node expectedCallExpression1 = createCallExpressionNode(createIdentifier("foo"), Collections.EMPTY_LIST, 1);
    Node expectedCallExpression2 = createCallExpressionNode(createIdentifier("bar"), Collections.EMPTY_LIST, 1);
    expectedExprStatement1.setFirstChild(expectedCallExpression2);
    expectedCallExpression2.setNext(expectedCallExpression1);
    expectedExprStatement1.setLineNumber(1);
    assertEquals(expectedExprStatement1, root.getFirstChild());

    // Test with empty arguments
    Node expr3 = createBinaryOperatorNode(ADD, createNumberNode(1, 1),
        createCallExpressionNode(createIdentifier("foo"), Collections.EMPTY_LIST, 1));
    Node exprStatement3 = createNode(EXPR_STATEMENT, 0);
    exprStatement3.setFirstChild(expr3);
    root.setFirstChild(exprStatement3);

    removeConstantExpressions.process(null, root);
    assertEquals(1, root.getChildCount());
    Node expectedExprStatement3 = createNode(EXPR_STATEMENT, 0);
    expectedExprStatement3.setFirstChild(createIdentifier("foo"));
    expectedExprStatement3.setLineNumber(1);
    assertEquals(expectedExprStatement3, root.getFirstChild());

    // Test with multiple statements
    Node root2 = createNode(BLOCK, 0);
    Node exprStatement4 = createNode(EXPR_STATEMENT, 0);
    exprStatement4.setFirstChild(expr1);
    Node exprStatement5 = createNode(EXPR_STATEMENT, 0);
    exprStatement5.setFirstChild(expr2);
    root2.setFirstChild(exprStatement4);
    root2.setLastChild(exprStatement5);

    removeConstantExpressions.process(null, root2);
    assertEquals(2, root2.getChildCount());
    Node expectedRoot2 = createNode(BLOCK, 0);
    expectedRoot2.setFirstChild(expectedCallExpression1);
    expectedRoot2.setLastChild(expectedCallExpression2);
    assertEquals(expectedRoot2, root2);

    // Test error scenario: non-expression statement
    Node nonExprStatement = createNode(ASSIGN, 0, createIdentifier("x"), createNumberNode(1, 1));
    root.setFirstChild(nonExprStatement);

    try {
      removeConstantExpressions.process(null, root);
      fail("Expected CompilerException");
    } catch (Exception e) {
      assertTrue(e instanceof CompilerException);
    }
  }
}
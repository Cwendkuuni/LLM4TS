import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeConstant.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    rootNode = new Node(Token.ROOT_NODE, null);
  }

  @Test
  public void testRemoveConstantExpressions() {
    // Create a simple expression tree with constant expressions
    Node expr1 = new Node(Token.EXPR_STATEMENT, null);
    Node addExpr = new Node(Token.ADD, null);
    Node num1 = new NumberLiteral(1, null);
    Node funcCall1 = new CallExpression(num1, null, Lists.newArrayList(
        new Identifier("foo", null)), null);
    addExpr.setChildren(num1, funcCall1, new NumberLiteral(2, null));
    expr1.setChildren(addExpr);

    Node expr2 = new Node(Token.EXPR_STATEMENT, null);
    Node addExpr2 = new Node(Token.ADD, null);
    Node num2 = new NumberLiteral(1, null);
    Node funcCall2 = new CallExpression(num2, null, Lists.newArrayList(
        new Identifier("bar", null)), null);
    addExpr2.setChildren(num2, funcCall2);
    expr2.setChildren(addExpr2);

    Node expr3 = new Node(Token.EXPR_STATEMENT, null);
    Node addExpr3 = new Node(Token.ADD, null);
    Node num3 = new NumberLiteral(1, null);
    Node funcCall3 = new CallExpression(num3, null, Lists.newArrayList(
        new Identifier("foo", null), new Identifier("bar", null)), null);
    addExpr3.setChildren(num3, funcCall3);
    expr3.setChildren(addExpr3);

    Node statementList = new Node(Token.STATEMENT_LIST, null);
    statementList.setChildren(expr1, expr2, expr3);
    rootNode.setChildren(statementList);

    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, rootNode);

    // Assert that the side-effect expressions were extracted and the
    // constant expressions were removed
    Node expectedRoot = new Node(Token.ROOT_NODE, null);
    Node actualRoot = removeConstantExpressions.getResult().ast;
    assertEquals(expectedRoot, actualRoot);

    Node expectedStatementList = new Node(Token.STATEMENT_LIST, null);
    Node actualStatementList = actualRoot.getFirstChild();
    assertEquals(expectedStatementList, actualStatementList);

    Node expectedExpr1 = new Node(Token.EXPR_STATEMENT, null);
    Node actualExpr1 = actualStatementList.getChildren().get(0);
    Node expectedFoo = new Identifier("foo", null);
    Node actualFoo = actualExpr1.getChildren().get(0);
    assertEquals(expectedFoo, actualFoo);
    Node expectedBar = new Identifier("bar", null);
    Node actualBar = actualExpr1.getChildren().get(1);
    assertEquals(expectedBar, actualBar);

    Node expectedExpr2 = new Node(Token.EXPR_STATEMENT, null);
    Node actualExpr2 = actualStatementList.getChildren().get(1);
    assertEquals(expectedExpr1, actualExpr2);

    Node expectedExpr3 = new Node(Token.EXPR_STATEMENT, null);
    Node actualExpr3 = actualStatementList.getChildren().get(2);
    Node expectedFooCall = new CallExpression(expectedFoo, null, Lists.newArrayList(expectedBar), null);
    Node actualFooCall = actualExpr3.getChildren().get(0);
    assertEquals(expectedFooCall, actualFooCall);
  }
}

class NodeUtil {
  public static boolean nodeTypeMayHaveSideEffects(Node node) {
    // Implementation of NodeUtil.nodeTypeMayHaveSideEffects() for testing
    List<Token> tokenTypes = Lists.newArrayList(
        Token.ASSIGN, Token.CALL, Token.COND, Token.IF, Token.SWITCH,
        Token.WHILE, Token.DO, Token.FOR, Token.FOR_IN, Token.FOR_OF,
        Token.TRY, Token.TRY_FINALLY, Token.TRY_CATCH, Token.TRY_CATCH_FINALLY,
        Token.WITH, Token.VAR, Token.LET, Token.CONST, Token.LABEL, Token.BREAK,
        Token.CONTINUE, Token.RETURN);
    return tokenTypes.contains(node.getType());
  }
}
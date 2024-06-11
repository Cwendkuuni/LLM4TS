import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private static final Logger logger = Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    node.appendChild(Node.newExpressionStatement(Node.newReturnStatement(new NodeOperators.IntegerLiteral(1))));
    node.appendChild(Node.newExpressionStatement(Node.newPrintStatement(new NodeOperators.StringLiteral("Unreachable"))));

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(node.getLastChild()));
  }

  @Test
  public void testRemoveNoOpStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    node.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(1))));
    node.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), NodeUtil.newVar("x"))));
    node.appendChild(Node.newExpressionStatement(Node.newPrintStatement(NodeUtil.newVar("x"))));

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertFalse(NodeUtil.hasChild(node, Node.newAssign(NodeUtil.newVar("x"), NodeUtil.newVar("x"))));
  }

  @Test
  public void testRemoveEmptyBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    node.appendChild(Node.newBlock(null, Node.newToken(Token.EMPTY, 0)));

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(node));
  }

  @Test
  public void testRemoveBreakStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node label = Node.newLabel();
    Node breakStatement = Node.newBreakStatement(label);

    node.appendChild(Node.newExpressionStatement(Node.newLabel(label)));
    node.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(1))));
    node.appendChild(breakStatement);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(node.getLastChild()));
  }

  @Test
  public void testRemoveContinueStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node label = Node.newLabel();
    Node continueStatement = Node.newContinueStatement(label);

    node.appendChild(Node.newExpressionStatement(Node.newLabel(label)));
    node.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(1))));
    node.appendChild(continueStatement);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(node.getLastChild()));
  }

  @Test
  public void testRemoveReturnStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));

    node.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(1))));
    node.appendChild(returnStatement);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(node.getLastChild()));
  }

  @Test
  public void testRemoveUnreachableCodeInsideFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node function = Node.newFunction(Node.newToken(Token.FUNCTION, 0), NodeUtil.newVar("x"), null, null);
    node.appendChild(Node.newExpressionStatement(function));

    Node functionBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));
    functionBody.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(1))));
    functionBody.appendChild(returnStatement);
    function.setBody(functionBody);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(functionBody));
  }

  @Test
  public void testRemoveUnreachableCodeInsideIfStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node ifStatement = Node.newIfStatement(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(1))), Node.newBlock(null, Node.newToken(Token.BLOCK, 0)));
    node.appendChild(ifStatement);

    Node ifBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));
    ifBody.appendChild(Node.newExpressionStatement(Node.newAssign(NodeUtil.newVar("x"), new NodeOperators.IntegerLiteral(2))));
    ifBody.appendChild(returnStatement);
    ifStatement.setThenStatement(ifBody);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(ifBody));
  }

  @Test
  public void testRemoveUnreachableCodeInsideSwitchStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node switchStatement = Node.newSwitchStatement(Node.newIdentifier("x"), Node.newBlock(null, Node.newToken(Token.BLOCK, 0)));
    node.appendChild(switchStatement);

    Node switchBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node caseStatement = Node.newCaseStatement(Node.newIntegerLiteral(1), Node.newBlock(null, Node.newToken(Token.BLOCK, 0)));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));
    caseStatement.appendChild(returnStatement);
    switchBody.appendChild(caseStatement);
    switchStatement.setBody(switchBody);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(caseStatement));
  }

  @Test
  public void testRemoveUnreachableCodeInsideLoopStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node forStatement = Node.newForStatement(Node.newIdentifier("x"), new NodeOperators.IntegerLiteral(1), new NodeOperators.IntegerLiteral(10), Node.newBlock(null, Node.newToken(Token.BLOCK, 0)), null);
    node.appendChild(forStatement);

    Node forBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));
    forBody.appendChild(returnStatement);
    forStatement.setBody(forBody);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(forBody));
  }

  @Test
  public void testRemoveUnreachableCodeInsideTryCatchStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node tryStatement = Node.newTryStatement(Node.newBlock(null, Node.newToken(Token.BLOCK, 0)), null, null);
    node.appendChild(tryStatement);

    Node tryBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node catchStatement = Node.newCatchStatement(NodeUtil.newVar("e"), Node.newBlock(null, Node.newToken(Token.BLOCK, 0)));
    tryBody.appendChild(catchStatement);
    tryStatement.setTryBlock(tryBody);

    Node catchBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));
    catchBody.appendChild(returnStatement);
    catchStatement.setBody(catchBody);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(catchBody));
  }

  @Test
  public void testRemoveUnreachableCodeInsideWithStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node node = Node.newBlock(null, new Token(Token.SCRIPT, 0));
    Node withStatement = Node.newWithStatement(NodeUtil.newVar("a"), Node.newFunctionCall(NodeUtil.newVar("xhr"), Node.newArrayLiteral(new NodeOperators.StringLiteral("GET"), NodeUtil.newIdentifier("url"))), Node.newBlock(null, Node.newToken(Token.BLOCK, 0)));
    node.appendChild(withStatement);

    Node withBody = Node.newBlock(null, Node.newToken(Token.BLOCK, 0));
    Node returnStatement = Node.newReturnStatement(Node.newStringLiteral("Unreachable"));
    withBody.appendChild(returnStatement);
    withStatement.setBody(withBody);

    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    elimination.process(NodeTraversal.rootTraversal(traversal), null);

    assertTrue(NodeUtil.isEmpty(withBody));
  }
}
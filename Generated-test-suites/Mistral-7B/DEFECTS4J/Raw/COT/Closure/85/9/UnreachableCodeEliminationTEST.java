import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ENTER_SCOPE;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EXIT_SCOPE;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.PROCESS;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.VISIT;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private Node rootNode;
  private NodeTraversal nodeTraversal;
  private UnreachableCodeElimination elimination;

  @Before
  public void setup() {
    // Create a root node for testing purposes.
    rootNode = new Node("root");
    nodeTraversal = new NodeTraversal(null, rootNode);
    elimination = new UnreachableCodeElimination(null, false);
  }

  @Test
  public void testEnterScope() {
    elimination.enterScope(nodeTraversal);
    assertEquals(ENTER_SCOPE, elimination.getCallbackType());
  }

  @Test
  public void testExitScope() {
    elimination.exitScope(nodeTraversal);
    assertEquals(EXIT_SCOPE, elimination.getCallbackType());
  }

  @Test
  public void testProcess() {
    elimination.process(null, rootNode);
    assertEquals(PROCESS, elimination.getCallbackType());
  }

  @Test
  public void testVisit() {
    elimination.visit(nodeTraversal, null, rootNode);
    assertEquals(VISIT, elimination.getCallbackType());
  }

  @Test
  public void testRemoveEmptyBlock() {
    Node emptyBlock = new Node("emptyBlock", Token.BLOCK);
    Node parent = new Node("parent");
    parent.addChildToEnd(emptyBlock);
    elimination.visit(nodeTraversal, emptyBlock, parent);
    assertTrue(emptyBlock.getParent() == null);
  }

  @Test
  public void testRemoveBlockWithNoOpStatements() {
    Node block = new Node("block", Token.BLOCK);
    Node expression = new Node("expression", Token.ASSIGN);
    expression.setRightChild(new Node("noOpStatement", Token.VOID));
    block.addChildToEnd(expression);
    Node parent = new Node("parent");
    parent.addChildToEnd(block);
    elimination.visit(nodeTraversal, block, parent);
    assertTrue(block.getParent() == null);
  }

  @Test
  public void testRemoveFunctionDeclaration() {
    Node functionDeclaration = new Node("functionDeclaration", Token.FUNCTION);
    Node body = new Node("functionBody", Token.BLOCK);
    functionDeclaration.addChildToEnd(body);
    Node parent = new Node("parent");
    parent.addChildToEnd(functionDeclaration);
    elimination.visit(nodeTraversal, functionDeclaration, parent);
    assertTrue(functionDeclaration.getParent() == null);
  }

  @Test(expected = NullPointerException.class)
  public void testRemoveNullNode() {
    elimination.visit(nodeTraversal, null, rootNode);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node breakStatement = new Node("breakStatement", Token.BREAK);
    Node target = new Node("target", Token.LABEL);
    Node parent = new Node("parent");
    parent.addChildToEnd(breakStatement);
    parent.addChildToEnd(target);
    elimination.tryRemoveUnconditionalBranching(breakStatement);
    assertEquals(target, elimination.tryRemoveUnconditionalBranching(breakStatement));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingToAnotherBranch() {
    Node breakStatement1 = new Node("breakStatement1", Token.BREAK);
    Node target1 = new Node("target1", Token.LABEL);
    Node breakStatement2 = new Node("breakStatement2", Token.BREAK);
    Node target2 = new Node("target2", Token.LABEL);
    Node parent = new Node("parent");
    parent.addChildToEnd(breakStatement1);
    parent.addChildToEnd(target1);
    parent.addChildToEnd(breakStatement2);
    parent.addChildToEnd(target2);
    assertNull(elimination.tryRemoveUnconditionalBranching(breakStatement1));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingToAnotherScope() {
    Node breakStatement = new Node("breakStatement", Token.BREAK);
    Node target = new Node("target", Token.SCRIPT);
    Node parent = new Node("parent");
    parent.addChildToEnd(breakStatement);
    elimination.visit(nodeTraversal, target, null);
    elimination.visit(nodeTraversal, parent, null);
    assertNull(elimination.tryRemoveUnconditionalBranching(breakStatement));
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    Node expression = new Node("expression", Token.ASSIGN);
    Node parent = new Node("parent");
    parent.addChildToEnd(expression);
    elimination.removeDeadExprStatementSafely(expression);
    assertTrue(expression.getParent() == null);
  }

  @Test
  public void testRemoveDeadBlock() {
    Node block = new Node("block", Token.BLOCK);
    Node parent = new Node("parent");
    parent.addChildToEnd(block);
    elimination.removeDeadExprStatementSafely(block);
    assertTrue(block.getParent() == null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveDeadBlockWithChildren() {
    Node block = new Node("block", Token.BLOCK);
    Node child = new Node("child", Token.ASSIGN);
    block.addChildToEnd(child);
    elimination.removeDeadExprStatementSafely(block);
  }

  @Test
  public void testRemoveEmptyFunctionDeclaration() {
    Node functionDeclaration = new Node("functionDeclaration", Token.FUNCTION);
    Node parent = new Node("parent");
    parent.addChildToEnd(functionDeclaration);
    elimination.removeDeadExprStatementSafely(functionDeclaration);
    assertTrue(functionDeclaration.getParent() == null);
  }

  @Test
  public void testRemoveFunctionDeclarationWithBody() {
    Node functionDeclaration = new Node("functionDeclaration", Token.FUNCTION);
    Node body = new Node("functionBody", Token.BLOCK);
    Node parent = new Node("parent");
    parent.addChildToEnd(functionDeclaration);
    functionDeclaration.addChildToEnd(body);
    elimination.removeDeadExprStatementSafely(functionDeclaration);
    assertTrue(functionDeclaration.getParent() == null);
  }

  @Test
  public void testRemoveFunctionDeclarationWithBodyAndChildren() {
    Node functionDeclaration = new Node("functionDeclaration", Token.FUNCTION);
    Node body = new Node("functionBody", Token.BLOCK);
    Node child = new Node("child", Token.ASSIGN);
    functionDeclaration.addChildToEnd(body);
    body.addChildToEnd(child);
    Node parent = new Node("parent");
    parent.addChildToEnd(functionDeclaration);
    elimination.removeDeadExprStatementSafely(functionDeclaration);
    assertTrue(functionDeclaration.getParent() == null);
  }

  @Test
  public void testRemoveTryCatchNode() {
    Node tryNode = new Node("tryNode", Token.TRY);
    Node catchNode = new Node("catchNode", Token.CATCH);
    Node finallyNode = new Node("finallyNode", Token.BLOCK);
    Node parent = new Node("parent");
    parent.addChildToEnd(tryNode);
    tryNode.addChildToEnd(catchNode);
    tryNode.addChildToEnd(finallyNode);
    elimination.removeDeadExprStatementSafely(tryNode);
    assertTrue(tryNode.getParent() == null);
  }

  @Test
  public void testRemoveTryCatchNodeWithFinally() {
    Node tryNode = new Node("tryNode", Token.TRY);
    Node catchNode = new Node("catchNode", Token.CATCH);
    Node finallyNode = new Node("finallyNode", Token.BLOCK);
    Node parent = new Node("parent");
    parent.addChildToEnd(tryNode);
    tryNode.addChildToEnd(catchNode);
    tryNode.addChildToEnd(finallyNode);
    elimination.visit(nodeTraversal, finallyNode, null);
    elimination.removeDeadExprStatementSafely(tryNode);
    assertTrue(tryNode.getParent() == null);
  }

  @Test
  public void testRemoveTryCatchNodeWithEmptyTry() {
    Node tryNode = new Node("tryNode", Token.TRY);
    Node catchNode = new Node("catchNode", Token.CATCH);
    Node parent = new Node("parent");
    parent.addChildToEnd(tryNode);
    tryNode.addChildToEnd(catchNode);
    elimination.removeDeadExprStatementSafely(tryNode);
    assertTrue(tryNode.getParent() == null);
  }
}
package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadExprStatementSafely() {
    Node node = Node.newBlock(null);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);

    elimination.removeDeadExprStatementSafely(node);
    assertNull(node.getFirstChild());

    node = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(42)));
    elimination.removeDeadExprStatementSafely(node);
    assertNull(node.getFirstChild());

    node = Node.newBlock(Node.newReturn(null, Node.newIdent("x")));
    elimination.removeDeadExprStatementSafely(node);
    assertTrue(NodeUtil.hasSideEffects(node));

    node = Node.newBlock(Node.newReturn(null, Node.newIdent("x")), Node.newIdent("x").setInit(Node.newNumberLiteral(42)));
    elimination.removeDeadExprStatementSafely(node);
    assertTrue(node.getFirstChild().getType() == Token.RETURN);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node root = Node.newBlock(
        Node.newVar("x", Node.newNumberLiteral(1)),
        Node.newVar("y", Node.newNumberLiteral(1)),
        Node.newBreak(Node.newIdent("outer")),
        Node.newIf(Node.newIdent("x"), Node.newBlock(Node.newBreak(Node.newIdent("inner"))), Node.newEmpty()),
        Node.newLabel("inner"),
        Node.newIf(Node.newIdent("y"), Node.newBlock(Node.newBreak(Node.newIdent("outer"))), Node.newEmpty()),
        Node.newLabel("outer")
    );

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node innerBreak = NodeUtil.getFirstChildByType(NodeUtil.getFirstChild(NodeUtil.getFirstChild(root), Token.IF), Token.BREAK);
    Node innerIf = NodeUtil.getFirstChildByType(root, Token.IF);
    Node innerLabel = NodeUtil.getFirstChildByType(root, Token.LABEL, "inner");

    Node outerBreak = NodeUtil.getFirstChildByType(NodeUtil.getFirstChild(root, Token.IF), Token.BREAK);
    Node outerIf = NodeUtil.getFirstChildByType(root, Token.IF);
    Node outerLabel = NodeUtil.getFirstChildByType(root, Token.LABEL, "outer");

    Node innerBreakAfterRemoval = elimination.tryRemoveUnconditionalBranching(innerBreak);
    Node outerBreakAfterRemoval = elimination.tryRemoveUnconditionalBranching(outerBreak);

    assertTrue(innerBreakAfterRemoval == innerLabel);
    assertTrue(outerBreakAfterRemoval == outerIf);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingEmptyStack() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(42)));
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    assertNull(elimination.tryRemoveUnconditionalBranching(root));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingNullNode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    assertNull(elimination.tryRemoveUnconditionalBranching(null));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingNoOutEdges() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(42)));
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node returnNode = NodeUtil.getFirstChildByType(root, Token.RETURN);
    assertNull(elimination.tryRemoveUnconditionalBranching(returnNode));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingNoUnconditionalBranch() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(42)), Node.newEmpty());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node returnNode = NodeUtil.getFirstChildByType(root, Token.RETURN);
    Node emptyNode = NodeUtil.getFirstChildByType(root, Token.EMPTY);

    assertNull(elimination.tryRemoveUnconditionalBranching(returnNode));
    assertNull(elimination.tryRemoveUnconditionalBranching(emptyNode));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingDifferentTarget() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(1)), Node.newReturn(null, Node.newNumberLiteral(2)));
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node returnNode1 = NodeUtil.getFirstChildByType(root, Token.RETURN);
    Node returnNode2 = NodeUtil.getFirstChildByType(root, Token.RETURN);

    assertNull(elimination.tryRemoveUnconditionalBranching(returnNode1));
    assertNull(elimination.tryRemoveUnconditionalBranching(returnNode2));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingSameTarget() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(1)), Node.newBreak(Node.newIdent("outer")), Node.newReturn(null, Node.newNumberLiteral(2)), Node.newLabel("outer"));
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node returnNode1 = NodeUtil.getFirstChildByType(root, Token.RETURN);
    Node breakNode = NodeUtil.getFirstChildByType(root, Token.BREAK);
    Node returnNode2 = NodeUtil.getFirstChildByType(root, Token.RETURN);
    Node outerLabel = NodeUtil.getFirstChildByType(root, Token.LABEL, "outer");

    Node removedReturnNode = elimination.tryRemoveUnconditionalBranching(returnNode1);
    Node removedBreakNode = elimination.tryRemoveUnconditionalBranching(breakNode);

    assertTrue(removedBreakNode == outerLabel);
    assertTrue(removedReturnNode == returnNode2);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingSameTargetWithNextStatement() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(1)), Node.newBreak(Node.newIdent("outer")), Node.newFunction(null, Node.newIdent("f"), null), Node.newLabel("outer"));
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node returnNode1 = NodeUtil.getFirstChildByType(root, Token.RETURN);
    Node breakNode = NodeUtil.getFirstChildByType(root, Token.BREAK);
    Node functionNode = NodeUtil.getFirstChildByType(root, Token.FUNCTION);
    Node outerLabel = NodeUtil.getFirstChildByType(root, Token.LABEL, "outer");

    Node removedBreakNode = elimination.tryRemoveUnconditionalBranching(breakNode);

    assertTrue(removedBreakNode == outerLabel);
    assertFalse(NodeUtil.isFunctionDeclaration(NodeUtil.getParent(functionNode)));
  }

  @Test
  public void testTryRemoveUnconditionalBranchingSameTargetWithNextStatementAndNoParentFunction() {
    Node root = Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(1)), Node.newBreak(Node.newIdent("outer")), Node.newFunction(Node.newBlock(Node.newReturn(null, Node.newNumberLiteral(42))), null, null), Node.newLabel("outer"));
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    NodeTraversal traversal = new NodeTraversal(null);
    traversal.traverse(elimination, root);

    Node returnNode1 = NodeUtil.getFirstChildByType(root, Token.RETURN);
    Node breakNode = NodeUtil.getFirstChildByType(root, Token.BREAK);
    Node functionNode = NodeUtil.getFirstChildByType(root, Token.FUNCTION);
    Node outerLabel = NodeUtil.getFirstChildByType(root, Token.LABEL, "outer");

    Node removedBreakNode = elimination.tryRemoveUnconditionalBranching(breakNode);

    assertTrue(removedBreakNode == outerLabel);
    assertTrue(NodeUtil.isFunctionDeclaration(NodeUtil.getParent(functionNode)));
  }
}
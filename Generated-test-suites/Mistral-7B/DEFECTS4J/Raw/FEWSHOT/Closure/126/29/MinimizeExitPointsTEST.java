package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    Node root = createSimpleAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithBreakLabel() {
    Node root = createBreakWithLabelAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithContinueLabel() {
    Node root = createContinueWithLabelAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithComplexExit() {
    Node root = createComplexExitAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithTryCatchFinally() {
    Node root = createTryCatchFinallyAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithMultiExitIf() {
    Node root = createMultiExitIfAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithNestedIfElseBlock() {
    Node root = createNestedIfElseAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyBlock() {
    Node root = createEmptyBlockAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithNoExitBlock() {
    Node root = createNoExitAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithDeleteProperty() {
    Node root = createDeletePropertyAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  @Test
  public void testMinimizeExitPointsWithFunctionDeclaration() {
    Node root = createFunctionDeclarationAST();
    NodeTraversal.traverse(new MinimizeExitPoints(null), root, null);
    assertMinimizedExitPoints(root);
  }

  private void assertMinimizedExitPoints(Node node) {
    assertEquals(0, nodeCount(node, Token.RETURN));
    assertEquals(0, nodeCount(node, Token.BREAK));
    assertEquals(0, nodeCount(node, Token.CONTINUE));
  }

  private Node createSimpleAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node ifNode = IR.ifStatement(null, IR.booleanLiteral(true), IR.block());
    Node ifBlock = ifNode.getFirstChild();
    Node elseBlock = IR.block();
    Node elseIfNode = IR.ifStatement(null, IR.booleanLiteral(false), IR.block());
    Node elseIfBlock = elseIfNode.getFirstChild();
    Node returnNode = IR.returnStatement(null);
    Node returnBlock = IR.block(returnNode);
    Node elseReturnNode = IR.returnStatement(null);
    Node elseReturnBlock = IR.block(elseReturnNode);

    ifNode.addChildToBack(ifBlock);
    ifBlock.addChildToBack(IR.printStatement("if"));
    ifBlock.addChildToBack(elseBlock);
    elseBlock.addChildToBack(IR.printStatement("else"));
    elseBlock.addChildToBack(elseIfNode);
    elseIfBlock.addChildToBack(IR.printStatement("else if"));
    elseBlock.addChildToBack(returnBlock);
    elseIfBlock.addChildToBack(IR.printStatement("else if"));
    elseBlock.addChildToBack(elseReturnBlock);
    root.addChildToBack(ifNode);

    return root;
  }

  private Node createBreakWithLabelAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node labelNode = IR.labelStatement("label");
    Node breakNode = IR.breakStatement(labelNode.getString());
    Node ifNode = IR.ifStatement(null, IR.booleanLiteral(true), IR.block());
    Node ifBlock = ifNode.getFirstChild();
    ifBlock.addChildToBack(IR.printStatement("if"));
    Node labelBlock = IR.block();
    Node returnNode = IR.returnStatement(null);
    Node returnBlock = IR.block(returnNode);

    labelBlock.addChildToBack(labelNode);
    labelBlock.addChildToBack(breakNode);
    labelBlock.addChildToBack(returnBlock);
    ifBlock.addChildToBack(labelBlock);
    root.addChildToBack(ifNode);

    return root;
  }

  private Node createContinueWithLabelAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node labelNode = IR.labelStatement("label");
    Node continueNode = IR.continueStatement(labelNode.getString());
    Node forNode = IR.forStatement(null, IR.nullLiteral(), IR.nullLiteral(), IR.nullLiteral(),
        null, null, IR.block(), null);
    Node forBlock = forNode.getLastChild();
    Node labelBlock = IR.block();
    Node returnNode = IR.returnStatement(null);
    Node returnBlock = IR.block(returnNode);

    labelBlock.addChildToBack(labelNode);
    labelBlock.addChildToBack(continueNode);
    labelBlock.addChildToBack(returnBlock);
    forBlock.addChildToBack(labelBlock);
    forNode.setLastChild(forBlock);
    root.addChildToBack(forNode);

    return root;
  }

  private Node createComplexExitAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node ifNode = IR.ifStatement(null, IR.booleanLiteral(false), IR.block());
    Node ifBlock = ifNode.getFirstChild();
    Node continueNode = IR.continueStatement();
    Node breakNode1 = IR.breakStatement(null);
    Node breakNode2 = IR.breakStatement(null);
    Node returnNode = IR.returnStatement(null);

    ifBlock.addChildToBack(IR.printStatement("if"));
    ifBlock.addChildToBack(continueNode);
    ifBlock.addChildToBack(IR.printStatement("continue"));
    ifBlock.addChildToBack(IR.printStatement("if"));
    ifBlock.addChildToBack(breakNode1);
    ifBlock.addChildToBack(IR.printStatement("break"));
    ifBlock.addChildToBack(breakNode2);
    ifBlock.addChildToBack(returnNode);
    ifNode.setLastChild(ifBlock);
    root.addChildToBack(ifNode);

    return root;
  }

  private Node createTryCatchFinallyAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node tryNode = IR.tryStatement(null, IR.block(), null, null, null);
    Node catchNode = IR.catchClause(null, IR.identifier("e"), null, null, IR.block(), null);
    Node finallyNode = IR.finallyStatement(null, IR.block());
    Node tryBlock = tryNode.getLastChild();
    Node catchBlock = catchNode.getLastChild();
    Node finallyBlock = finallyNode.getLastChild();
    Node returnNode = IR.returnStatement(null);
    Node returnBlock = IR.block(returnNode);

    tryBlock.addChildToBack(IR.printStatement("try"));
    catchBlock.addChildToBack(IR.printStatement("catch"));
    finallyBlock.addChildToBack(IR.printStatement("finally"));
    tryNode.setLastChild(tryBlock);
    tryBlock.addChildToBack(returnBlock);
    catchNode.setLastChild(catchBlock);
    finallyNode.setLastChild(finallyBlock);
    tryBlock.addChildToBack(catchNode);
    tryBlock.addChildToBack(finallyNode);
    root.addChildToBack(tryNode);

    return root;
  }

  private Node createMultiExitIfAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node ifNode1 = IR.ifStatement(null, IR.booleanLiteral(false), IR.block());
    Node ifBlock1 = ifNode1.getFirstChild();
    Node returnNode1 = IR.returnStatement(null);
    Node returnBlock1 = IR.block(returnNode1);
    Node ifNode2 = IR.ifStatement(null, IR.booleanLiteral(true), IR.block());
    Node ifBlock2 = ifNode2.getFirstChild();
    Node returnNode2 = IR.returnStatement(null);
    Node returnBlock2 = IR.block(returnNode2);

    ifBlock1.addChildToBack(IR.printStatement("if 1"));
    ifBlock1.addChildToBack(returnBlock1);
    ifBlock2.addChildToBack(IR.printStatement("if 2"));
    ifBlock2.addChildToBack(returnBlock2);
    ifNode1.setLastChild(ifBlock1);
    ifNode2.setLastChild(ifBlock2);
    root.addChildToBack(ifNode1);
    root.addChildToBack(ifNode2);

    return root;
  }

  private Node createNestedIfElseAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node ifNode1 = IR.ifStatement(null, IR.booleanLiteral(false), IR.block());
    Node ifBlock1 = ifNode1.getFirstChild();
    Node returnNode1 = IR.returnStatement(null);
    Node returnBlock1 = IR.block(returnNode1);
    Node elseNode1 = IR.ifStatement(null, null, IR.block());
    Node elseBlock1 = elseNode1.getFirstChild();
    Node returnNode2 = IR.returnStatement(null);
    Node returnBlock2 = IR.block(returnNode2);
    Node ifNode2 = IR.ifStatement(null, IR.booleanLiteral(true), IR.block());
    Node ifBlock2 = ifNode2.getFirstChild();
    Node returnNode3 = IR.returnStatement(null);
    Node returnBlock3 = IR.block(returnNode3);

    ifBlock1.addChildToBack(IR.printStatement("if 1"));
    ifBlock1.addChildToBack(elseNode1);
    elseBlock1.addChildToBack(IR.printStatement("else if 1.1"));
    elseBlock1.addChildToBack(returnBlock2);
    ifBlock2.addChildToBack(IR.printStatement("if 2"));
    ifBlock2.addChildToBack(returnBlock3);
    ifNode1.setLastChild(ifBlock1);
    ifNode2.setLastChild(ifBlock2);
    elseNode1.setLastChild(elseBlock1);
    root.addChildToBack(ifNode1);
    root.addChildToBack(ifNode2);
    root.addChildToBack(elseNode1);

    return root;
  }

  private Node createEmptyBlockAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node emptyBlock = IR.block();
    root.addChildToBack(emptyBlock);

    return root;
  }

  private Node createNoExitAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node block = IR.block();
    root.addChildToBack(block);

    return root;
  }

  private Node createDeletePropertyAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node objNode = IR.memberExpression(IR.thisKeyword(), "obj");
    Node deleteNode = IR.deleteProperty(objNode, IR.identifier("prop"));
    Node block = IR.block(deleteNode);
    root.addChildToBack(block);

    return root;
  }

  private Node createFunctionDeclarationAST() {
    Node root = IR.block().srcref(new Node(Token.PROGRAM, null, null));
    Node functionNode = IR.functionDeclaration(null, "foo", new Token[0], null, null, null, IR.block());
    Node functionBlock = functionNode.getLastChild();
    root.addChildToBack(functionNode);

    return root;
  }

  private int nodeCount(Node node, int type) {
    int count = 0;
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == type) {
        count++;
      } else if (child.isBlock()) {
        count += nodeCount(child, type);
      }
    }
    return count;
  }
}
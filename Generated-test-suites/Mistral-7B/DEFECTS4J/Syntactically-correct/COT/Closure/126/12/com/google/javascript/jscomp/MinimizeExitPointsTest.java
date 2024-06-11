package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for MinimizeExitPoints.
 */
@RunWith(MockitoJUnitRunner.class)
public class MinimizeExitPointsTest {

  private static final int EXIT_TYPE_BREAK = Token.BREAK;
  private static final int EXIT_TYPE_CONTINUE = Token.CONTINUE;
  private static final int EXIT_TYPE_RETURN = Token.RETURN;

  @Mock
  private AbstractCompiler compiler;
  @InjectMocks
  private MinimizeExitPoints minimizeExitPoints;
  @Mock
  private NodeFactory nodeFactory;

  private Node rootNode;
  private Node labelNode;
  private Node forNode;
  private Node whileNode;
  private Node doNode;
  private Node functionNode;
  private Node ifNode;
  private Node ifElseNode;
  private Node tryNode;
  private Node catchNode;
  private Node finallyNode;
  private Node blockNode;

  @Before
  public void setup() {
    rootNode = mockNode("function(){}", "root");
    labelNode = mockNode("label foo", "labelNode");
    forNode = mockNode("for(var i = 0; i < 10; i++){}", "forNode");
    whileNode = mockNode("while(true){}", "whileNode");
    doNode = mockNode("do{while(true);}while(false)", "doNode");
    functionNode = mockNode("function foo(){return;}", "functionNode");
    ifNode = mockNode("if(true) {}", "ifNode");
    ifElseNode = mockNode("if(true) {} else {}", "ifElseNode");
    tryNode = mockNode("try{foo()}finally{}", "tryNode");
    catchNode = mockNode("try{foo()}catch(e){}", "catchNode");
    finallyNode = mockNode("try{foo()}finally{}", "finallyNode");
    blockNode = mockNode("{};", "blockNode");

    when(compiler.getFactory()).thenReturn(nodeFactory);
  }

  /**
   * Test method for process.
   */
  @Test
  public void testProcess() {
    minimizeExitPoints.process(null, rootNode);
    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit.
   */
  @Test
  public void testVisitLabel() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), labelNode, rootNode);
    NodeUtil.removeChild(labelNode.getParent(), labelNode);
    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit for statement.
   */
  @Test
  public void testVisitFor() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), forNode, rootNode);

    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    minimizeExitPoints.tryMinimizeExits(loopCodeBlock, EXIT_TYPE_CONTINUE, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit while statement.
   */
  @Test
  public void testVisitWhile() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), whileNode, rootNode);

    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(whileNode);
    minimizeExitPoints.tryMinimizeExits(loopCodeBlock, EXIT_TYPE_CONTINUE, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit do statement.
   */
  @Test
  public void testVisitDo() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), doNode, rootNode);

    Node condition = NodeUtil.getConditionExpression(doNode);
    Preconditions.checkState(NodeUtil.getImpureBooleanValue(condition) == TernaryValue.FALSE);
    minimizeExitPoints.tryMinimizeExits(doNode.getFirstChild(), EXIT_TYPE_BREAK, null);

    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(doNode);
    minimizeExitPoints.tryMinimizeExits(loopCodeBlock, EXIT_TYPE_CONTINUE, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit function statement.
   */
  @Test
  public void testVisitFunction() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), functionNode, rootNode);

    Node returnNode = functionNode.getLastChild();
    minimizeExitPoints.tryMinimizeExits(returnNode, EXIT_TYPE_RETURN, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit if statement.
   */
  @Test
  public void testVisitIf() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), ifNode, rootNode);

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node elseBlock = ifBlock.getNext();

    minimizeExitPoints.tryMinimizeIfBlockExits(ifBlock, elseBlock, ifNode, EXIT_TYPE_BREAK, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit if else statement.
   */
  @Test
  public void testVisitIfElse() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), ifElseNode, rootNode);

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node elseBlock = ifBlock.getNext();

    minimizeExitPoints.tryMinimizeIfBlockExits(ifBlock, elseBlock, ifNode, EXIT_TYPE_BREAK, null);

    // Verify that the else block is not empty and not removed.
    Preconditions.checkState(!elseBlock.isEmpty());

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit try statement.
   */
  @Test
  public void testVisitTry() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), tryNode, rootNode);

    Node tryBlock = tryNode.getFirstChild();
    Node allCatchNodes = NodeUtil.getCatchBlock(tryNode);
    Node catchCodeBlock = allCatchNodes.getFirstChild();
    Node finallyBlock = tryNode.getLastChild();

    minimizeExitPoints.tryMinimizeExits(tryBlock, EXIT_TYPE_RETURN, null);

    // Verify that the catch block is not empty and not removed.
    Preconditions.checkState(!catchCodeBlock.isEmpty());

    minimizeExitPoints.tryMinimizeExits(catchCodeBlock, EXIT_TYPE_RETURN, null);

    // Verify that the finally block is not empty and not removed.
    Preconditions.checkState(!finallyBlock.isEmpty());

    verify(compiler).reportCodeChange();
  }

  /**
   * Test method for visit block statement.
   */
  @Test
  public void testVisitBlock() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), blockNode, rootNode);

    Node lastChild = blockNode.getLastChild();
    minimizeExitPoints.tryMinimizeExits(lastChild, EXIT_TYPE_RETURN, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test edge case for visit if statement with empty true block.
   */
  @Test
  public void testVisitEmptyIfTrueBlock() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), ifNode, rootNode);

    Node ifBlock = ifNode.getFirstChild().getNext();
    Preconditions.checkState(ifBlock.isEmpty());

    Node elseBlock = ifBlock.getNext();

    minimizeExitPoints.tryMinimizeIfBlockExits(ifBlock, elseBlock, ifNode, EXIT_TYPE_BREAK, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test edge case for visit if statement with empty false block.
   */
  @Test
  public void testVisitEmptyIfFalseBlock() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), ifElseNode, rootNode);

    Node ifBlock = ifNode.getFirstChild().getNext();
    Preconditions.checkState(ifBlock.isEmpty());

    Node elseBlock = ifBlock.getNext();

    minimizeExitPoints.tryMinimizeIfBlockExits(ifBlock, elseBlock, ifNode, EXIT_TYPE_BREAK, null);

    verify(compiler).reportCodeChange();
  }

  /**
   * Test edge case for visit if statement with label break.
   */
  @Test
  public void testVisitLabelBreak() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), ifNode, rootNode);

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node breakNode = new Node(Node.BREAK, null, null);
    Node labelNode = new Node(Token.IDENT, "foo", null);
    breakNode.setParent(ifBlock);
    breakNode.setNext(new Node(Node.EMPTY, null, null));
    ifBlock.addChildToBack(labelNode);
    ifBlock.addChildToBack(breakNode);

    minimizeExitPoints.tryMinimizeIfBlockExits(ifBlock, null, ifNode, EXIT_TYPE_BREAK, "foo");

    verify(compiler).reportCodeChange();
  }

  /**
   * Test edge case for visit if statement with empty try statement.
   */
  @Test
  public void testVisitEmptyTryStatement() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), tryNode, rootNode);

    Node tryBlock = tryNode.getFirstChild();
    Preconditions.checkState(tryBlock.isEmpty());

    Node allCatchNodes = NodeUtil.getCatchBlock(tryNode);
    Node catchCodeBlock = allCatchNodes.getFirstChild();
    Node finallyBlock = tryNode.getLastChild();

    minimizeExitPoints.tryMinimizeExits(tryBlock, EXIT_TYPE_RETURN, null);

    // Verify that the catch block is not empty and not removed.
    Preconditions.checkState(!catchCodeBlock.isEmpty());

    minimizeExitPoints.tryMinimizeExits(catchCodeBlock, EXIT_TYPE_RETURN, null);

    // Verify that the finally block is not empty and not removed.
    Preconditions.checkState(!finallyBlock.isEmpty());

    verify(compiler).reportCodeChange();
  }

  /**
   * Test edge case for visit if statement with empty catch statement.
   */
  @Test
  public void testVisitEmptyCatchStatement() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), tryNode, rootNode);

    Node tryBlock = tryNode.getFirstChild();
    Node allCatchNodes = NodeUtil.getCatchBlock(tryNode);
    Node catchCodeBlock = allCatchNodes.getFirstChild();
    Node finallyBlock = tryNode.getLastChild();

    minimizeExitPoints.tryMinimizeExits(tryBlock, EXIT_TYPE_RETURN, null);

    // Verify that the catch block is empty.
    Preconditions.checkState(catchCodeBlock.isEmpty());

    verify(compiler).reportCodeChange();
  }

  /**
   * Test edge case for visit if statement with empty finally statement.
   */
  @Test
  public void testVisitEmptyFinallyStatement() {
    minimizeExitPoints.visit(
        mock(NodeTraversal.class), tryNode, rootNode);

    Node tryBlock = tryNode.getFirstChild();
    Node allCatchNodes = NodeUtil.getCatchBlock(tryNode);
    Node catchCodeBlock = allCatchNodes.getFirstChild();
    Node finallyBlock = tryNode.getLastChild();

    minimizeExitPoints.tryMinimizeExits(tryBlock, EXIT_TYPE_RETURN, null);

    // Verify that the finally block is empty.
    Preconditions.checkState(finallyBlock.isEmpty());

    verify(compiler).reportCodeChange();
  }

  /**
   * Helper method to create a mock Node.
   */
  private Node mockNode(String code, String name) {
    Node node = new Node(Token.FUNCTION, null, null);
    node.setCode(code);
    node.setName(name);
    node.setParent(rootNode);
    return node;
  }
}
package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;

/**
 * Test cases for MinimizeExitPoints class
 *
 * @author johnlenz
 */
@RunWith(MockitoJUnitRunner.class)
public class MinimizeExitPointsTest {

  @InjectMocks
  private MinimizeExitPoints minimizeExitPoints;
  private AbstractCompiler compiler;
  private NodeUtil nodeUtil;
  private NodeTraversal nodeTraversal;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    nodeUtil = mock(NodeUtil.class);
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = mock(NodeTraversal.class);
    when(nodeUtil.getCatchBlock(anyNode())).thenReturn(anyNode());
    when(nodeUtil.getCatchBlock(eq(null))).thenReturn(null);
    when(nodeUtil.hasCatchHandler(anyNode())).thenReturn(true);
    when(nodeUtil.hasCatchHandler(eq(null))).thenReturn(false);
    when(nodeUtil.hasFinally(anyNode())).thenReturn(true);
    when(nodeUtil.hasFinally(eq(null))).thenReturn(false);
    when(nodeUtil.isFunctionDeclaration(anyNode())).thenReturn(false);
    when(nodeUtil.isFunctionDeclaration(eq(null))).thenReturn(false);
  }

  /**
   * Test constructor
   */
  @Test
  public void testMinimizeExitPointsConstructor() {
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(abstractCompiler);
    assertEquals("AbstractCompiler", minimizeExitPoints.compiler.getClass().getSimpleName());
  }

  /**
   * Test process method
   */
  @Test
  public void testProcess() {
    Node externs = Node.newTextNode("externs");
    Node root = Node.newTextNode("root");
    minimizeExitPoints.process(externs, root);
    verify(nodeTraversal).traverse(eq(compiler), eq(root), eq(minimizeExitPoints));
  }

  /**
   * Test visit method for label nodes
   */
  @Test
  public void testVisitLabelNode() {
    Node label = Node.newLabelNode("label");
    Node breakNode = Node.newBreakNode();

    Node parent = Node.newBlockNode();
    parent.addChildToBack(label);
    parent.addChildToBack(breakNode);

    minimizeExitPoints.visit(nodeTraversal, parent, null);

    assertEquals(1, minimizeExitPoints.compiler.getNumCodeChanges());
    verify(compiler).reportCodeChange();
    verify(nodeUtil, times(1)).removeChild(parent, breakNode);
  }

  /**
   * Test visit method for for and while nodes
   */
  @Test
  public void testVisitForAndWhileNodes() {
    Node forNode = Node.newForNode();
    Node continueNode = Node.newContinueNode();

    Node loopCodeBlock = Node.newBlockNode();
    loopCodeBlock.addChildToBack(continueNode);

    Node parent = Node.newBlockNode();
    parent.addChildToBack(forNode);
    parent.addChildToBack(loopCodeBlock);

    minimizeExitPoints.visit(nodeTraversal, parent, null);

    verify(nodeUtil, times(1)).getLoopCodeBlock(forNode);
    verify(nodeUtil).tryMinimizeExits(loopCodeBlock, Token.CONTINUE, null);
  }

  /**
   * Test visit method for do-while nodes
   */
  @Test
  public void testVisitDoWhileNodes() {
    Node doNode = Node.newDoNode();
    Node continueNode = Node.newContinueNode();
    Node breakNode = Node.newBreakNode();

    Node loopCodeBlock = Node.newBlockNode();
    loopCodeBlock.addChildToBack(continueNode);
    loopCodeBlock.addChildToBack(breakNode);

    Node condition = Node.newConditionalNode(Token.TRUE, null, null);
    Node parent = Node.newBlockNode();
    parent.addChildToBack(doNode);
    parent.addChildToBack(loopCodeBlock);
    parent.addChildToBack(condition);

    minimizeExitPoints.visit(nodeTraversal, parent, null);

    verify(nodeUtil, times(2)).getLoopCodeBlock(doNode);
    verify(nodeUtil).tryMinimizeExits(loopCodeBlock, Token.CONTINUE, null);
    verify(nodeUtil).tryMinimizeExits(condition, Token.BREAK, null);
  }

  /**
   * Test visit method for function nodes
   */
  @Test
  public void testVisitFunctionNodes() {
    Node functionNode = Node.newFunctionNode("functionName", new ArrayList<>());
    Node returnNode = Node.newReturnNode(Node.newTextNode("returnValue"));

    Node lastChild = Node.newBlockNode();
    lastChild.addChildToBack(returnNode);

    Node parent = Node.newBlockNode();
    parent.addChildToBack(functionNode);
    parent.addChildToBack(lastChild);

    minimizeExitPoints.visit(nodeTraversal, parent, null);

    verify(nodeUtil, times(1)).tryMinimizeExits(lastChild, Token.RETURN, null);
  }

  /**
   * Test tryMinimizeExits method with exit node
   */
  @Test
  public void testTryMinimizeExitsWithExitNode() {
    Node exitNode = Node.newReturnNode(Node.newTextNode("exitValue"));

    Node parent = Node.newBlockNode();
    parent.addChildToBack(exitNode);

    minimizeExitPoints.tryMinimizeExits(parent, Token.RETURN, null);

    verify(nodeUtil, times(1)).removeChild(parent, exitNode);
    verify(compiler, times(1)).reportCodeChange();
  }

  /**
   * Test tryMinimizeExits method with if nodes
   */
  @Test
  public void testTryMinimizeExitsWithIfNodes() {
    Node ifNodeTrue = Node.newBlockNode();
    Node ifNodeFalse = Node.newBlockNode();

    Node parent = Node.newBlockNode();
    parent.addChildToBack(Node.newIfNode(Token.TRUE, ifNodeTrue, ifNodeFalse));

    minimizeExitPoints.tryMinimizeExits(parent, Token.RETURN, null);

    verify(nodeUtil, times(1)).tryMinimizeIfBlockExits(ifNodeTrue, ifNodeFalse, eq(Node.newIfNode(Token.TRUE, ifNodeTrue, ifNodeFalse)), Token.RETURN, null);
  }

  /**
   * Test tryMinimizeExits method with try-catch-finally nodes
   */
  @Test
  public void testTryMinimizeExitsWithTryCatchFinallyNodes() {
    Node tryNode = Node.newTryNode();
    Node tryCodeBlock = Node.newBlockNode();
    Node catchNode = Node.newBlockNode();
    Node finallyNode = Node.newBlockNode();

    Node parent = Node.newBlockNode();
    parent.addChildToBack(tryNode);
    parent.addChildToBack(tryCodeBlock);
    parent.addChildToBack(catchNode);
    parent.addChildToBack(finallyNode);

    minimizeExitPoints.tryMinimizeExits(parent, Token.RETURN, null);

    verify(nodeUtil, times(3)).tryMinimizeExits(anyNode(), anyInt(), anyString());
  }
}
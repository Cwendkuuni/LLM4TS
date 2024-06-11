package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Test class for MinimizeExitPoints.
 */
@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    nodeFactory = compiler.getNodeFactory();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testProcess() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    minimizeExitPoints.process(null, root);
    // Add assertions for the expected behavior of the process() method here.
  }

  @Test
  public void testVisitLabel() {
    Node labelNode = NodeUtil.createLabelNode(nodeFactory, "TestLabel");
    Node exitNode = NodeUtil.createReturnNode(nodeFactory);
    Node parentNode = NodeUtil.createBlockNode(nodeFactory);
    parentNode.addChildToBack(exitNode);
    parentNode.addChildToBack(labelNode);

    minimizeExitPoints.visit(null, parentNode, null);

    assertFalse("Expected exitNode to be removed", parentNode.hasChild(exitNode));
  }

  @Test
  public void testVisitFor() {
    Node forNode = NodeUtil.createForNode(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    Node continueNode = NodeUtil.createContinueNode(nodeFactory);
    Node parentNode = NodeUtil.createBlockNode(nodeFactory);
    parentNode.addChildToBack(loopCodeBlock);
    parentNode.addChildToBack(continueNode);

    minimizeExitPoints.visit(null, parentNode, null);

    assertFalse("Expected continueNode to be removed", parentNode.hasChild(continueNode));
  }

  @Test
  public void testVisitDoWhile() {
    Node doNode = NodeUtil.createDoNode(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(doNode);
    Node conditionNode = NodeUtil.createLiteralNode(nodeFactory, Token.FALSE);
    Node parentNode = NodeUtil.createBlockNode(nodeFactory);
    parentNode.addChildToBack(conditionNode);
    parentNode.addChildToBack(loopCodeBlock);
    parentNode.addChildToBack(NodeUtil.createLabelNode(nodeFactory, "TestLabel"));
    parentNode.addChildToBack(NodeUtil.createBreakNode(nodeFactory, "TestLabel"));

    minimizeExitPoints.visit(null, parentNode, null);

    // Expected behavior: continueNode is removed, but breakNode is left in place
    assertFalse("Expected continueNode to be removed", parentNode.hasChild(continueNode));
    assertTrue("Expected breakNode to still be present", parentNode.hasChild(getNodeWithName(parentNode, "TestLabel")));
  }

  @Test
  public void testVisitFunction() {
    Node functionNode = NodeUtil.createFunctionNode(nodeFactory);
    Node returnNode = NodeUtil.createReturnNode(nodeFactory);
    Node parentNode = NodeUtil.createBlockNode(nodeFactory);
    parentNode.addChildToBack(functionNode);
    parentNode.addChildToBack(returnNode);

    minimizeExitPoints.visit(null, parentNode, null);

    assertFalse("Expected returnNode to be removed", parentNode.hasChild(returnNode));
  }

  @Test
  public void testVisitTryCatchFinally() {
    Node tryNode = NodeUtil.createTryNode(nodeFactory);
    Node tryBlock = NodeUtil.createBlockNode(nodeFactory);
    Node catchNode = NodeUtil.createBlockNode(nodeFactory);
    Node finallyNode = NodeUtil.createBlockNode(nodeFactory);
    Node exitNode = NodeUtil.createReturnNode(nodeFactory);
    Node parentNode = NodeUtil.createBlockNode(nodeFactory);

    tryBlock.addChildToBack(exitNode);
    tryNode.setTryBlock(tryBlock);
    tryNode.setCatchBlock(catchNode);
    tryNode.setFinallyBlock(finallyNode);
    parentNode.addChildToBack(tryNode);

    minimizeExitPoints.visit(null, parentNode, null);

    // Expected behavior: returnNode is removed, but catchBlock and finallyBlock
    // are left in place
    assertFalse("Expected returnNode to be removed", parentNode.hasChild(exitNode));
    assertTrue("Expected catchBlock to still be present", parentNode.hasChild(catchNode));
    assertTrue("Expected finallyBlock to still be present", parentNode.hasChild(finallyNode));
  }

  @Test
  public void testTryMinimizeIfBlockExits() {
    Node ifNode = NodeUtil.createIfNode(nodeFactory);
    Node ifTrueBlock = NodeUtil.createBlockNode(nodeFactory);
    Node ifFalseBlock = NodeUtil.createBlockNode(nodeFactory);
    Node exitNode1 = NodeUtil.createReturnNode(nodeFactory);
    Node exitNode2 = NodeUtil.createReturnNode(nodeFactory);
    Node parentNode = NodeUtil.createBlockNode(nodeFactory);

    parentNode.addChildToBack(ifNode);
    parentNode.addChildToBack(ifTrueBlock);
    parentNode.addChildToBack(ifFalseBlock);
    ifTrueBlock.addChildToBack(exitNode1);
    ifFalseBlock.addChildToBack(exitNode2);

    minimizeExitPoints.tryMinimizeIfBlockExits(ifTrueBlock, ifFalseBlock, ifNode, Token.RETURN, null);

    // Expected behavior: exitNode1 is moved into ifFalseBlock and exitNode2 is removed
    assertFalse("Expected exitNode2 to be removed", parentNode.hasChild(exitNode2));
    assertTrue("Expected exitNode1 to be in ifFalseBlock", ifFalseBlock.hasChild(exitNode1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeIfBlockExitsWithNullParentNode() {
    Node ifNode = NodeUtil.createIfNode(nodeFactory);
    Node ifTrueBlock = NodeUtil.createBlockNode(nodeFactory);
    Node ifFalseBlock = NodeUtil.createBlockNode(nodeFactory);
    Node exitNode = NodeUtil.createReturnNode(nodeFactory);

    minimizeExitPoints.tryMinimizeIfBlockExits(ifTrueBlock, ifFalseBlock, ifNode, Token.RETURN, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeIfBlockExitsWithNullIfNode() {
    Node srcBlock = NodeUtil.createBlockNode(nodeFactory);
    Node destBlock = NodeUtil.createBlockNode(nodeFactory);

    minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, null, Token.RETURN, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeIfBlockExitsWithNullSrcBlock() {
    Node ifNode = NodeUtil.createIfNode(nodeFactory);
    Node destBlock = NodeUtil.createBlockNode(nodeFactory);

    minimizeExitPoints.tryMinimizeIfBlockExits(null, destBlock, ifNode, Token.RETURN, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeIfBlockExitsWithNullDestBlock() {
    Node ifNode = NodeUtil.createIfNode(nodeFactory);
    Node srcBlock = NodeUtil.createBlockNode(nodeFactory);

    minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, null, ifNode, Token.RETURN, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeIfBlockExitsWithNullExitType() {
    Node ifNode = NodeUtil.createIfNode(nodeFactory);
    Node ifTrueBlock = NodeUtil.createBlockNode(nodeFactory);
    Node ifFalseBlock = NodeUtil.createBlockNode(nodeFactory);

    minimizeExitPoints.tryMinimizeIfBlockExits(ifTrueBlock, ifFalseBlock, ifNode, (byte) 0, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeIfBlockExitsWithNullLabelName() {
    Node ifNode = NodeUtil.createIfNode(nodeFactory);
    Node ifTrueBlock = NodeUtil.createBlockNode(nodeFactory);
    Node ifFalseBlock = NodeUtil.createBlockNode(nodeFactory);
    Node exitNode = NodeUtil.createBreakNode(nodeFactory, "TestLabel");

    minimizeExitPoints.tryMinimizeIfBlockExits(ifTrueBlock, ifFalseBlock, ifNode, Token.BREAK, null);
  }

  private Node getNodeWithName(Node parentNode, String nodeName) {
    for (Node n = parentNode.getFirstChild(); n != null; n = n.getNext()) {
      if (n.isLabel() && n.getFirstChild().getString().equals(nodeName)) {
        return n;
      }
      if (n.isIdentifier() && n.getString().equals(nodeName)) {
        return n;
      }
      if (n.isFunction() && nodeName.equals(JSDocInfo.getFunctionName(n))) {
        return n;
      }
      if (n.isBlock() && getNodeWithName(n, nodeName) != null) {
        return n;
      }
    }
    return null;
  }
}
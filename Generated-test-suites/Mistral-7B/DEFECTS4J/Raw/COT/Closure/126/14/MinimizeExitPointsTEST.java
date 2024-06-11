package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;

/**
 * Test class for MinimizeExitPoints class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MinimizeExitPointsTest {

  @Mock
  private AbstractCompiler compiler;

  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  /**
   * Test for visit method with Label node.
   */
  @Test
  public void testVisitLabel() {
    Node labelNode = Node.newLabel("label1");
    Node breakNode = Node.newBreak("label1");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(labelNode);
    parentNode.addChildToBack(breakNode);

    minimizeExitPoints.visit(NodeTraversal.EMPTY, parentNode, null);

    assertNull(parentNode.getLastChild());
  }

  /**
   * Test for visit method with For node.
   */
  @Test
  public void testVisitFor() {
    Node forNode = Node.newFor();
    Node codeBlock = NodeUtil.newBlock(Arrays.asList(Node.newPrint("1")));
    minimizeExitPoints.visit(NodeTraversal.EMPTY, forNode, null);
    NodeUtil.setLoopCodeBlock(forNode, codeBlock);

    minimizeExitPoints.tryMinimizeExits(codeBlock, Token.CONTINUE, null);

    assertNull(codeBlock.getLastChild());
  }

  /**
   * Test for visit method with Do-While node.
   */
  @Test
  public void testVisitDoWhile() {
    Node doNode = Node.newDo();
    Node condition = Node.newBooleanLiteral(false);
    Node codeBlock = NodeUtil.newBlock(Arrays.asList(Node.newPrint("1")));
    minimizeExitPoints.visit(NodeTraversal.EMPTY, doNode, null);
    NodeUtil.setConditionExpression(doNode, condition);
    NodeUtil.setLoopCodeBlock(doNode, codeBlock);

    minimizeExitPoints.tryMinimizeExits(NodeUtil.getLoopCodeBlock(doNode),
        Token.BREAK, null);

    // Normally, breaks inside a loop can't be optimized, but
    // since we know the condition will always be false in this test case,
    // it's treated as a continue.
    minimizeExitPoints.tryMinimizeExits(doNode.getFirstChild(),
        Token.BREAK, null);

    assertNull(codeBlock.getLastChild());
  }

  /**
   * Test for visit method with Function node.
   */
  @Test
  public void testVisitFunction() {
    Node functionNode = Node.newFunction(
        "testFunction", NodeUtil.newBlock(Arrays.asList(Node.newPrint("1"))));
    minimizeExitPoints.visit(NodeTraversal.EMPTY, functionNode, null);

    minimizeExitPoints.tryMinimizeExits(functionNode.getLastChild(),
        Token.RETURN, null);

    assertNull(functionNode.getLastChild().getNext());
  }
}
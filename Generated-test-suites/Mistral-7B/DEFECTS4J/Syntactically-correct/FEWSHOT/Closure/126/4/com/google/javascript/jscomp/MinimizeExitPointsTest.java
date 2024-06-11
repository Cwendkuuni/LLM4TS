package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

/**
 * Test for MinimizeExitPoints class
 *
 * @author johnlenz
 */
@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private Node createTestNode(Node parent) {
    Node n = IR.block();
    Node function = IR.function("test", null);
    Node body = IR.block();
    Node ifStatement = IR.ifStatement(
        IR.booleanLiteral(true),
        IR.block(),
        IR.returnStatement(IR.stringLiteral("exit1"))
    );
    Node ifElseStatement = IR.ifElseStatement(ifStatement, IR.block());
    Node loopStatement = IR.whileStatement(IR.booleanLiteral(true), IR.block());
    Node breakStatement = IR.breakStatement();

    body.addChild(ifElseStatement);
    body.addChild(loopStatement);
    function.setBody(body);
    parent.addChildToBack(function);
    return function;
  }

  @Test
  public void testMinimizeExitPoints() {
    AbstractCompiler compiler = new Compiler();
    Node root = IR.program();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(null, root);

    Node testFunction = createTestNode(root);
    assertEquals(0, testFunction.getLastChildCount());
    Node functionBody = testFunction.getFirstChild();

    Node ifBlock = functionBody.getChildAt(0);
    Node ifStatement = ifBlock.getFirstChild();
    Node ifCondition = ifStatement.getFirstChild();
    Node ifTrueBlock = ifStatement.getChildAt(1);
    Node ifFalseBlock = ifBlock.getChildAt(1);

    Node whileBlock = functionBody.getChildAt(1);
    Node whileCondition = whileBlock.getFirstChild();

    // Expected exit points before optimization
    assertEquals(2, functionBody.getLastChildCount());
    assertEquals(1, ifFalseBlock.getLastChildCount());
    assertEquals(1, whileBlock.getLastChildCount());
    assertFalse(ifTrueBlock.getLastChild().isReturnStatement());
    assertNull(ifFalseBlock.getLastChild());
    assertFalse(whileBlock.getLastChild().isReturnStatement());

    minimizeExitPoints.tryMinimizeExits(ifCondition, Token.BREAK, null);
    minimizeExitPoints.tryMinimizeExits(whileCondition, Token.CONTINUE, null);

    // Expected exit points after optimization
    assertEquals(1, functionBody.getLastChildCount());
    assertEquals(1, ifFalseBlock.getLastChildCount());
    assertEquals(1, whileBlock.getLastChildCount());
    assertTrue(ifTrueBlock.getLastChild().isReturnStatement());
    assertNull(ifFalseBlock.getLastChild());
    assertFalse(whileBlock.getLastChild().isReturnStatement());

    // Test ifMinimizeIfBlockExits
    Node ifBlock2 = IR.block().addChild(IR.ifStatement(
        IR.booleanLiteral(false),
        IR.block().addChild(IR.returnStatement(IR.stringLiteral("exit2"))),
        null
    ));

    minimizeExitPoints.tryMinimizeExits(ifBlock2, Token.BREAK, "label");

    // Expected exit points after optimization with a label
    assertEquals(1, ifBlock2.getLastChildCount());
    assertEquals(1, ifBlock2.getChildAt(0).getLastChildCount());
    assertFalse(ifBlock2.getChildAt(0).getLastChild().isReturnStatement());
    assertTrue(ifBlock2.getLastChild().isLabelStatement());
    assertNull(ifBlock2.getLastChild().getLastChild());
  }
}
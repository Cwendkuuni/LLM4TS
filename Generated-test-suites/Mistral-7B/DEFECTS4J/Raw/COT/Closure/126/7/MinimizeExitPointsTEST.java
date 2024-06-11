package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Unit tests for MinimizeExitPoints class.
 */
@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_EmptyAST() {
    Node root = IR.block();
    minimizeExitPoints.process(null, root);
    assertEquals(0, root.getNumChildren());
  }

  @Test
  public void testMinimizeExitPoints_LabelExit() {
    String labelName = "testLabel";
    Node root = IR.block();
    Node labelNode = nodeFactory.createLabel(labelName);
    Node exitNode = nodeFactory.createToken(Token.BREAK);
    Node labelBlock = IR.block().add(labelNode);
    labelBlock.add(exitNode);
    root.addChildToBack(labelBlock);

    minimizeExitPoints.process(null, root);
    assertTrue(labelBlock.isEmpty());

    // Check that BREAK has been removed.
    for (Node node : root.getChildren()) {
      if (node.isLabel() && node.getString().equals(labelName)) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.BREAK));
        break;
      }
    }
  }

  @Test
  public void testMinimizeExitPoints_IfExit() {
    String ifCondition = "x";
    Node root = IR.block();
    Node ifNode = nodeFactory.createIf(nodeFactory.createIdentifier(ifCondition));
    Node exitNode = nodeFactory.createToken(Token.RETURN);
    Node ifBlock = IR.block().add(exitNode);
    ifNode.setLastChild(ifBlock);
    root.addChildToBack(ifNode);

    minimizeExitPoints.process(null, root);
    assertTrue(ifBlock.isEmpty());

    // Check that RETURN has been removed.
    for (Node node : root.getChildren()) {
      if (node.isIf()) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.RETURN));
        break;
      }
    }
  }

  @Test
  public void testMinimizeExitPoints_IfElseExit() {
    String ifCondition = "x";
    String elseExpression = "y";
    Node root = IR.block();
    Node ifNode = nodeFactory.createIf(nodeFactory.createIdentifier(ifCondition));
    Node ifBlock = IR.block();
    Node elseBlock = IR.block();
    Node elseIfNode = nodeFactory.createIf(nodeFactory.createLiteral(Token.FALSE));
    Node exitNode = nodeFactory.createToken(Token.RETURN);
    ifBlock.add(exitNode);
    elseBlock.add(nodeFactory.createToken(Token.IDENTIFIER, "doSomething"));
    ifNode.setLastChild(ifBlock);
    elseIfNode.setLastChild(elseBlock);
    ifNode.addChildToBack(elseIfNode);
    root.addChildToBack(ifNode);

    minimizeExitPoints.process(null, root);
    assertTrue(ifBlock.isEmpty());
    assertTrue(elseBlock.isEmpty());

    // Check that RETURN has been removed from ifBlock.
    for (Node node : ifNode.getChildren()) {
      if (node.isIf()) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.RETURN));
        break;
      }
    }
  }

  @Test
  public void testMinimizeExitPoints_ForExit() {
    String forCondition = "x";
    Node root = IR.block();
    Node forNode = nodeFactory.createToken(Token.WHILE, "for");
    Node loopCondition = nodeFactory.createIdentifier(forCondition);
    Node forBlock = IR.block();
    Node exitNode = nodeFactory.createToken(Token.BREAK);
    forBlock.add(exitNode);
    forNode.setLastChild(NodeUtil.getLoopCodeBlock(forNode));
    forNode.setFirstChild(loopCondition);
    forNode.addChildToBack(forBlock);
    root.addChildToBack(forNode);

    minimizeExitPoints.process(null, root);
    assertTrue(forBlock.isEmpty());

    // Check that BREAK has been removed.
    for (Node node : root.getChildren()) {
      if (node.isToken(Token.WHILE) && node.getFirstChild().getString().equals("for")) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.BREAK));
        break;
      }
    }
  }

  @Test
  public void testMinimizeExitPoints_DoWhileExit() {
    String doCondition = "x";
    Node root = IR.block();
    Node doNode = nodeFactory.createToken(Token.WHILE, "do");
    Node loopCondition = nodeFactory.createIdentifier(doCondition);
    Node doBlock = IR.block();
    Node exitNode = nodeFactory.createToken(Token.BREAK);
    doBlock.add(exitNode);
    doNode.setLastChild(NodeUtil.getLoopCodeBlock(doNode));
    doNode.setFirstChild(loopCondition);
    doNode.addChildToBack(doBlock);
    root.addChildToBack(doNode);

    minimizeExitPoints.process(null, root);
    assertTrue(doBlock.isEmpty());

    // Check that BREAK has been removed.
    for (Node node : root.getChildren()) {
      if (node.isToken(Token.WHILE) && node.getFirstChild().getString().equals("do")) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.BREAK));
        break;
      }
    }
  }

  @Test
  public void testMinimizeExitPoints_FunctionExit() {
    String functionName = "testFunction";
    Node root = IR.block();
    Node functionNode = nodeFactory.createFunction("testFunction", ImmutableList.of(), null, null, null);
    Node exitNode = nodeFactory.createToken(Token.RETURN);
    Node functionBlock = IR.block().add(exitNode);
    functionNode.setLastChild(functionBlock);
    root.addChildToBack(functionNode);

    minimizeExitPoints.process(null, root);
    assertTrue(functionBlock.isEmpty());

    // Check that RETURN has been removed.
    for (Node node : root.getChildren()) {
      if (node.isFunction()) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.RETURN));
        break;
      }
    }
  }

  @Test
  public void testMinimizeExitPoints_IfElseIfExit() {
    String ifCondition1 = "x";
    String elifCondition = "y";
    String elseExpression = "z";
    Node root = IR.block();
    Node ifNode = nodeFactory.createIf(nodeFactory.createIdentifier(ifCondition1));
    Node ifBlock1 = IR.block();
    Node elifNode = nodeFactory.createIf(nodeFactory.createLiteral(Token.FALSE));
    Node ifBlock2 = IR.block();
    Node elseBlock = IR.block();
    Node exitNode = nodeFactory.createToken(Token.RETURN);
    ifBlock1.add(exitNode);
    ifNode.setLastChild(ifBlock1);
    elifNode.setLastChild(ifBlock2);
    elseBlock.add(nodeFactory.createToken(Token.IDENTIFIER, "doSomething"));
    ifNode.addChildToBack(elifNode);
    ifNode.addChildToBack(elseBlock);
    root.addChildToBack(ifNode);

    minimizeExitPoints.process(null, root);
    assertTrue(ifBlock1.isEmpty());
    assertTrue(ifBlock2.isEmpty());

    // Check that RETURN has been removed from ifBlock1.
    for (Node node : ifNode.getChildren()) {
      if (node.isIf()) {
        Node lastChild = node.getLastChild();
        assertFalse(lastChild.isToken(Token.RETURN));
        break;
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPoints_InvalidAST() {
    Node root = IR.block();
    minimizeExitPoints.process(null, root);
    fail("Expected an AssertionError as the AST was empty.");
  }
}
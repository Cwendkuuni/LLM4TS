package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private NodeFactory nodeFactory;
  private AbstractCompiler compiler;
  private NodeTraversal traverser;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    compiler = mock(AbstractCompiler.class);
    traverser = new NodeTraversal(compiler);
  }

  @Test
  public void testMinimizeExitPoints_simpleIfStatement() {
    Node root = NodeUtil.createSimpleFunction(nodeFactory, "test");
    Node ifNode = NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, true));
    Node exitNode = NodeUtil.createReturnStatement(nodeFactory, root, NodeUtil.createNullLiteral(nodeFactory));
    Node nextNode = NodeUtil.createExpressionStatement(nodeFactory, root, NodeUtil.createAddExpression(nodeFactory, NodeUtil.createNumberLiteral(nodeFactory, 1), NodeUtil.createNumberLiteral(nodeFactory, 1)));
    ifNode.addChildToBack(exitNode);
    ifNode.addChildToBack(nextNode);

    new MinimizeExitPoints(compiler).process(nodeFactory.getExterns(), root);

    Node expected = NodeUtil.createBlockStatement(nodeFactory, root);
    expected.addChildToBack(NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, true), NodeUtil.createBlockStatement(nodeFactory, root), NodeUtil.createBlockStatement(nodeFactory, root)));
    expected.addChildToBack(NodeUtil.createExpressionStatement(nodeFactory, root, nextNode));

    assertThat(NodeUtil.isTreeEqual(root, expected)).isTrue();
  }

  @Test
  public void testMinimizeExitPoints_labeledBreakStatement() {
    Node root = NodeUtil.createSimpleFunction(nodeFactory, "test");
    Node labelNode = NodeUtil.createLabelStatement(nodeFactory, root, "label");
    Node loopNode = NodeUtil.createWhileStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, true));
    Node bodyNode = NodeUtil.createBlockStatement(nodeFactory, loopNode);
    Node breakNode = NodeUtil.createLabeledStatement(nodeFactory, root, labelNode, NodeUtil.createBreakStatement(nodeFactory, root));
    bodyNode.addChildToBack(breakNode);

    new MinimizeExitPoints(compiler).process(nodeFactory.getExterns(), root);

    Node expected = NodeUtil.createBlockStatement(nodeFactory, root);
    expected.addChildToBack(labelNode);
    expected.addChildToBack(NodeUtil.createBlockStatement(nodeFactory, loopNode));
    expected.addChildToBack(expected.getLastChild().getLastChild()); // the body of while statement

    assertThat(NodeUtil.isTreeEqual(root, expected)).isTrue();
  }

  @Test
  public void testMinimizeExitPoints_nestedIfStatements() {
    Node root = NodeUtil.createSimpleFunction(nodeFactory, "test");
    Node ifNode1 = NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, true));
    Node ifNode2 = NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, false));
    Node exitNode = NodeUtil.createReturnStatement(nodeFactory, root, NodeUtil.createStringLiteral(nodeFactory, "Exit"));
    Node nextNode1 = NodeUtil.createExpressionStatement(nodeFactory, root, NodeUtil.createAddExpression(nodeFactory, NodeUtil.createNumberLiteral(nodeFactory, 1), NodeUtil.createNumberLiteral(nodeFactory, 1)));
    Node nextNode2 = NodeUtil.createExpressionStatement(nodeFactory, root, NodeUtil.createAddExpression(nodeFactory, NodeUtil.createNumberLiteral(nodeFactory, 2), NodeUtil.createNumberLiteral(nodeFactory, 2)));

    ifNode1.addChildToBack(ifNode2);
    ifNode1.addChildToBack(exitNode);
    ifNode1.addChildToBack(nextNode1);
    ifNode1.addChildToBack(nextNode2);

    new MinimizeExitPoints(compiler).process(nodeFactory.getExterns(), root);

    Node expected = NodeUtil.createBlockStatement(nodeFactory, root);
    Node ifBlock1 = NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, true), NodeUtil.createBlockStatement(nodeFactory, root), NodeUtil.createBlockStatement(nodeFactory, root));
    Node ifBlock2 = NodeUtil.createBlockStatement(nodeFactory, root);
    Node elseBlock1 = NodeUtil.createBlockStatement(nodeFactory, root);
    Node elseBlock2 = NodeUtil.createBlockStatement(nodeFactory, root);

    ifBlock1.addChildToBack(NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, false), elseBlock1, NodeUtil.createBlockStatement(nodeFactory, root)));
    ifBlock1.addChildToBack(ifBlock2);
    elseBlock1.addChildToBack(nextNode1);
    elseBlock1.addChildToBack(nextNode2);
    elseBlock1.addChildToBack(exitNode);

    expected.addChildToBack(ifBlock1);

    assertThat(NodeUtil.isTreeEqual(root, expected)).isTrue();
  }

  @Test
  public void testMinimizeExitPoints_doWhileStatement() {
    Node root = NodeUtil.createSimpleFunction(nodeFactory, "test");
    Node doNode = NodeUtil.createDoStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, true));
    Node loopNode = NodeUtil.createWhileStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, false));
    Node bodyNode = NodeUtil.createBlockStatement(nodeFactory, loopNode);
    Node exitNode = NodeUtil.createReturnStatement(nodeFactory, root, NodeUtil.createStringLiteral(nodeFactory, "Exit"));
    Node nextNode = NodeUtil.createExpressionStatement(nodeFactory, root, NodeUtil.createAddExpression(nodeFactory, NodeUtil.createNumberLiteral(nodeFactory, 1), NodeUtil.createNumberLiteral(nodeFactory, 1)));

    doNode.addChildToBack(loopNode);
    doNode.addChildToBack(bodyNode);
    bodyNode.addChildToBack(exitNode);
    bodyNode.addChildToBack(nextNode);

    new MinimizeExitPoints(compiler).process(nodeFactory.getExterns(), root);

    Node expected = NodeUtil.createBlockStatement(nodeFactory, root);
    Node doWhileBlock = NodeUtil.createDoWhileStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, false), NodeUtil.createBlockStatement(nodeFactory, root));
    Node ifBlock = NodeUtil.createBlockStatement(nodeFactory, root);
    Node elseBlock = NodeUtil.createBlockStatement(nodeFactory, root);

    doWhileBlock.addChildToBack(NodeUtil.createIfStatement(nodeFactory, root, NodeUtil.createBooleanLiteral(nodeFactory, false), elseBlock, NodeUtil.createBlockStatement(nodeFactory, root)));
    doWhileBlock.addChildToBack(ifBlock);
    elseBlock.addChildToBack(nextNode);
    elseBlock.addChildToBack(exitNode);

    expected.addChildToBack(doWhileBlock);

    assertThat(NodeUtil.isTreeEqual(root, expected)).isTrue();
  }
}
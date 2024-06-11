package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MinimizeExitPointsTest {
  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testProcess_EmptyNode() {
    Node emptyNode = Node.NULL;
    minimizeExitPoints.process(Node.NULL, emptyNode);
    // No assertions here, as no code change is expected
  }

  @Test
  public void testProcess_SimpleFunction() {
    Node root = Node.newFunction("test", new Node[0], Node.newBlock(2));
    minimizeExitPoints.process(Node.NULL, root);
    // No assertions here, as no code change is expected
  }

  @Test
  public void testProcess_FunctionWithReturn() {
    Node returnStatement = Node.newExpressionStatement(
        Node.newReturnStatement(Node.newNumberLiteral(5)));
    Node root = Node.newFunction("test", new Node[0], Node.newBlock(2)
        .addChild(Node.newStatement(returnStatement)));
    minimizeExitPoints.process(Node.NULL, root);

    ArgumentCaptor<Node> codeChangeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).reportCodeChange();
    verify(compiler).processAfterCodeChange(eq(root), codeChangeCaptor.capture());
    Node codeChange = codeChangeCaptor.getValue();

    assertSame(returnStatement, codeChange);
  }

  @Test
  public void testProcess_FunctionWithExitLabel() {
    Node label = Node.newLabel("exit");
    Node loop = NodeUtil.newForLoopStatement(
        Node.newIdentifier("i"), Node.newNumberLiteral(0),
        Node.newNumberLiteral(10), Node.newBlock(3),
        Node.newBlock(2).addChild(label));
    Node root = Node.newBlock(2).addChild(label);
    root.addChild(loop);
    root.addChild(Node.newStatement(
        Node.newBreakStatement(label.getId())));
    minimizeExitPoints.process(Node.NULL, root);

    ArgumentCaptor<Node> codeChangeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).reportCodeChange();
    verify(compiler).processAfterCodeChange(eq(root), codeChangeCaptor.capture());
    Node codeChange = codeChangeCaptor.getValue();

    assertNull(codeChange);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_InvalidNodeType() {
    Node invalidNode = Node.newExpressionStatement(
        Node.newExpression(Token.NULL, new Node[0]));
    minimizeExitPoints.process(Node.NULL, invalidNode);
  }
}
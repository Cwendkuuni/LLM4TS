package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    minimizer = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_SimpleIf() {
    Node root = Mockito.mock(Node.class);
    Node ifNode = Mockito.mock(Node.class);
    Node ifBlock = Mockito.mock(Node.class);
    Node elseBlock = Mockito.mock(Node.class);

    when(root.getType()).thenReturn(Token.FUNCTION);
    when(root.getLastChild()).thenReturn(ifNode);
    when(ifNode.getType()).thenReturn(Token.IF);
    when(ifNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    when(ifNode.getFirstChild().getNext()).thenReturn(ifBlock);
    when(ifBlock.isBlock()).thenReturn(true);
    when(ifBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));
    when(ifNode.getNext()).thenReturn(elseBlock);
    when(elseBlock.isBlock()).thenReturn(true);
    when(elseBlock.isEmpty()).thenReturn(false);
    when(elseBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));

    minimizer.process(Mockito.mock(Node.class), root);

    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testMinimizeExitPoints_IfElse() {
    Node root = Mockito.mock(Node.class);
    Node ifNode = Mockito.mock(Node.class);
    Node ifBlock = Mockito.mock(Node.class);
    Node elseBlock = Mockito.mock(Node.class);

    when(root.getType()).thenReturn(Token.FUNCTION);
    when(root.getLastChild()).thenReturn(ifNode);
    when(ifNode.getType()).thenReturn(Token.IF);
    when(ifNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    when(ifNode.getFirstChild().getNext()).thenReturn(ifBlock);
    when(ifBlock.isBlock()).thenReturn(true);
    when(ifBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));
    when(ifNode.getNext()).thenReturn(elseBlock);
    when(elseBlock.isBlock()).thenReturn(true);
    when(elseBlock.isEmpty()).thenReturn(false);
    when(elseBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));

    minimizer.process(Mockito.mock(Node.class), root);

    verify(compiler, times(1)).reportCodeChange();
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPoints_InvalidStatement() {
    Node root = Mockito.mock(Node.class);
    Node node = Mockito.mock(Node.class);

    when(root.getType()).thenReturn(Token.FUNCTION);
    when(root.getLastChild()).thenReturn(node);
    when(node.getType()).thenReturn(Token.INVALID);

    minimizer.process(Mockito.mock(Node.class), root);
  }

  @Test
  public void testMinimizeExitPoints_LabeledStatement() {
    Node root = Mockito.mock(Node.class);
    Node labelNode = Mockito.mock(Node.class);
    Node body = Mockito.mock(Node.class);

    when(root.getType()).thenReturn(Token.FUNCTION);
    when(root.getLastChild()).thenReturn(labelNode);
    when(labelNode.getType()).thenReturn(Token.LABEL);
    when(labelNode.getLastChild()).thenReturn(body);

    String labelName = "testLabel";
    when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    when(labelNode.getFirstChild().getString()).thenReturn(labelName);

    Node loopNode = Mockito.mock(Node.class);
    Node loopCodeBlock = Mockito.mock(Node.class);
    Node breakNode = Mockito.mock(Node.class);

    when(loopNode.getType()).thenReturn(Token.WHILE);
    when(loopNode.getFirstChild()).thenReturn(loopCodeBlock);
    when(loopNode.getLastChild()).thenReturn(breakNode);

    when(loopCodeBlock.isBlock()).thenReturn(true);
    when(loopCodeBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));

    when(breakNode.getType()).thenReturn(Token.BREAK);

    minimizer.process(Mockito.mock(Node.class), root);

    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testMinimizeExitPoints_LabeledStatement_NamedBreak() {
    Node root = Mockito.mock(Node.class);
    Node labelNode = Mockito.mock(Node.class);
    Node body = Mockito.mock(Node.class);
    Node breakNode = Mockito.mock(Node.class);

    when(root.getType()).thenReturn(Token.FUNCTION);
    when(root.getLastChild()).thenReturn(labelNode);
    when(labelNode.getType()).thenReturn(Token.LABEL);
    when(labelNode.getLastChild()).thenReturn(body);

    String labelName = "testLabel";
    when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    when(labelNode.getFirstChild().getString()).thenReturn(labelName);

    Node loopNode = Mockito.mock(Node.class);
    Node loopCodeBlock = Mockito.mock(Node.class);
    Node breakNodeWithLabel = Mockito.mock(Node.class);

    when(loopNode.getType()).thenReturn(Token.WHILE);
    when(loopNode.getFirstChild()).thenReturn(loopCodeBlock);
    when(loopNode.getLastChild()).thenReturn(breakNodeWithLabel);

    when(loopCodeBlock.isBlock()).thenReturn(true);
    when(loopCodeBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));

    when(breakNodeWithLabel.getType()).thenReturn(Token.BREAK);
    when(breakNodeWithLabel.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    when(breakNodeWithLabel.getFirstChild().getString()).thenReturn(labelName);

    minimizer.process(Mockito.mock(Node.class), root);

    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testMinimizeExitPoints_LabeledStatement_NoBreak() {
    Node root = Mockito.mock(Node.class);
    Node labelNode = Mockito.mock(Node.class);
    Node body = Mockito.mock(Node.class);

    when(root.getType()).thenReturn(Token.FUNCTION);
    when(root.getLastChild()).thenReturn(labelNode);
    when(labelNode.getType()).thenReturn(Token.LABEL);
    when(labelNode.getLastChild()).thenReturn(body);

    String labelName = "testLabel";
    when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    when(labelNode.getFirstChild().getString()).thenReturn(labelName);

    Node loopNode = Mockito.mock(Node.class);
    Node loopCodeBlock = Mockito.mock(Node.class);

    when(loopNode.getType()).thenReturn(Token.WHILE);
    when(loopNode.getFirstChild()).thenReturn(loopCodeBlock);
    when(loopNode.getLastChild()).thenReturn(Mockito.mock(Node.class));

    when(loopCodeBlock.isBlock()).thenReturn(true);
    when(loopCodeBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));

    minimizer.process(Mockito.mock(Node.class), root);

    verify(compiler, times(0)).reportCodeChange();
  }
}
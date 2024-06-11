package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback.GatherSideEffectSubexpressionsCallback;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback.GatherSideEffectSubexpressionsCallback.CopySideEffectSubexpressions;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;
  private RemoveConstantRValuesCallback removeConstantRValuesCallback;
  private AbstractPostOrderCallback gatherSideEffectSubexpressionsCallback;
  private CopySideEffectSubexpressions copySideEffectSubexpressions;
  private Result result;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantRValuesCallback = new RemoveConstantRValuesCallback();
    gatherSideEffectSubexpressionsCallback = Mockito.spy(
        new GatherSideEffectSubexpressionsCallback(null, copySideEffectSubexpressions));
    copySideEffectSubexpressions = Mockito.mock(CopySideEffectSubexpressions.class);
    result = new Result();
    when(compiler.getConstants()).thenReturn(Collections.emptyMap());
  }

  @Test
  public void testProcess_EmptyNode() {
    RemoveConstantRValuesCallback spyRemoveConstantRValuesCallback = spy(removeConstantRValuesCallback);
    Node root = new Node(Token.ROOT, null);
    removeConstantExpressions.process(null, root);
    verify(spyRemoveConstantRValuesCallback, times(0)).notifyCompiler(compiler);
  }

  @Test
  public void testProcess_SimpleExpression() {
    Node root = new Node(Token.EXPR_STATEMENT, null);
    Node expression = new Node(Token.ADD, null,
        new Node(Token.NUMBER, null, 1),
        new Node(Token.NAME, null, "foo"),
        new Node(Token.NAME, null, "bar"));
    root.setFirstChild(expression);

    ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    when(compiler.getASTRoot()).thenReturn(root);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.addPass(Mockito.any(CompilerPass.class))).thenAnswer(
        new Answer<Void>() {
          @Override
          public Void answer(AnswerContext answerContext) throws CompilationFailedException {
            removeConstantExpressions.process(null, root);
            return null;
          }
        });

    removeConstantExpressions.process(null, root);
    verify(compiler).addPass(eq(removeConstantExpressions));
    verify(removeConstantRValuesCallback).notifyCompiler(compiler);
    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_AssignedExpression() {
    Node root = new Node(Token.ASSIGN, null,
        new Node(Token.NAME, null, "x"),
        new Node(Token.EXPR_STATEMENT, null,
            new Node(Token.ADD, null,
                new Node(Token.NUMBER, null, 1),
                new Node(Token.NAME, null, "foo"),
                new Node(Token.NAME, null, "bar"))));

    ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    when(compiler.getASTRoot()).thenReturn(root);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.addPass(Mockito.any(CompilerPass.class))).thenAnswer(
        new Answer<Void>() {
          @Override
          public Void answer(AnswerContext answerContext) throws CompilationFailedException {
            removeConstantExpressions.process(null, root);
            return null;
          }
        });

    removeConstantExpressions.process(null, root);
    verify(compiler).addPass(eq(removeConstantExpressions));
    verify(removeConstantRValuesCallback).notifyCompiler(compiler);
    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_ConstantExpression() {
    Node root = new Node(Token.EXPR_STATEMENT, null,
        new Node(Token.NUMBER, null, 1));

    ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    when(compiler.getASTRoot()).thenReturn(root);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.addPass(Mockito.any(CompilerPass.class))).thenAnswer(
        new Answer<Void>() {
          @Override
          public Void answer(AnswerContext answerContext) throws CompilationFailedException {
            removeConstantExpressions.process(null, root);
            return null;
          }
        });

    removeConstantExpressions.process(null, root);
    verify(compiler).addPass(eq(removeConstantExpressions));
    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_ConstantExpression_WithSideEffect() {
    Node root = new Node(Token.EXPR_STATEMENT, null,
        new Node(Token.NAME, null, "console"),
        new Node(Token.DOT, null, null,
            new Node(Token.NAME, null, "log"),
            new Node(Token.NUMBER, null, 1)));

    ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    when(compiler.getASTRoot()).thenReturn(root);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.addPass(Mockito.any(CompilerPass.class))).thenAnswer(
        new Answer<Void>() {
          @Override
          public Void answer(AnswerContext answerContext) throws CompilationFailedException {
            removeConstantExpressions.process(null, root);
            return null;
          }
        });

    removeConstantExpressions.process(null, root);
    verify(compiler).addPass(eq(removeConstantExpressions));
    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testGetSideEffectNodes_EmptyNode() {
    Node node = new Node(Token.EMPTY, null);
    List<Node> sideEffectNodes = removeConstantRValuesCallback.getSideEffectNodes(node);
    assertEquals(0, sideEffectNodes.size());
  }

  @Test
  public void testGetSideEffectNodes_ConstantNode() {
    Node constantNode = new Node(Token.NUMBER, null, 1);
    List<Node> sideEffectNodes = removeConstantRValuesCallback.getSideEffectNodes(constantNode);
    assertEquals(0, sideEffectNodes.size());
  }

  @Test
  public void testGetSideEffectNodes_NonConstantNode() {
    Node nonConstantNode = new Node(Token.NAME, null, "foo");
    List<Node> sideEffectNodes = removeConstantRValuesCallback.getSideEffectNodes(nonConstantNode);
    assertEquals(1, sideEffectNodes.size());
    assertEquals(nonConstantNode, sideEffectNodes.get(0));
  }

  @Test
  public void testGetSideEffectNodes_MultipleNodes() {
    Node node = new Node(Token.EXPR_STATEMENT, null,
        new Node(Token.ASSIGN, null,
            new Node(Token.NAME, null, "x"),
            new Node(Token.ADD, null,
                new Node(Token.NUMBER, null, 1),
                new Node(Token.NAME, null, "foo"),
                new Node(Token.NAME, null, "bar"))));

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    when(gatherSideEffectSubexpressionsCallback.getInputNode()).thenReturn(node);
    when(gatherSideEffectSubexpressionsCallback.getCopy()).thenReturn(copySideEffectSubexpressions);
    when(copySideEffectSubexpressions.copy(node)).thenAnswer(new Answer<Node>() {
      @Override
      public Node answer(AnswerContext answerContext) throws CompilationFailedException {
        Node copy = new Node(node.getType(), node.getParent(), node.getFirstChild(), node.getLastChild());
        copy.setFirstChild(null);
        copy.setLastChild(null);
        return copy;
      }
    });

    List<Node> sideEffectNodes = removeConstantRValuesCallback.getSideEffectNodes(node);
    assertEquals(2, sideEffectNodes.size());
    assertEquals(node.getFirstChild(), sideEffectNodes.get(0));
    assertEquals(node.getLastChild(), sideEffectNodes.get(1));
  }

  @Test
  public void testReplaceWith_EmptyList() {
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    Node node = new Node(Token.NUMBER, null, 1);
    removeConstantRValuesCallback.replaceWith(parent, node, Lists.newArrayList());
    assertEquals(node, parent.getFirstChild());
  }

  @Test
  public void testReplaceWith_OneNode() {
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    Node nodeToReplace = new Node(Token.NUMBER, null, 1);
    Node replacement = new Node(Token.NAME, null, "foo");

    removeConstantRValuesCallback.replaceWith(parent, nodeToReplace, Lists.newArrayList(replacement));
    assertEquals(replacement, parent.getFirstChild());
  }

  @Test
  public void testReplaceWith_MultipleNodes() {
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    Node nodeToReplace = new Node(Token.EXPR_STATEMENT, null,
        new Node(Token.ADD, null,
            new Node(Token.NUMBER, null, 1),
            new Node(Token.NAME, null, "foo"),
            new Node(Token.NAME, null, "bar")));

    Node replacement1 = new Node(Token.NAME, null, "x");
    Node replacement2 = new Node(Token.NAME, null, "y");

    removeConstantRValuesCallback.replaceWith(parent, nodeToReplace, Lists.newArrayList(replacement1, replacement2));
    assertEquals(replacement1, parent.getFirstChild());
    assertEquals(replacement2, parent.getLastChild());
  }

  @Test
  public void testReplaceWith_NullNode() {
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    Node nodeToReplace = null;

    removeConstantRValuesCallback.replaceWith(parent, nodeToReplace, Lists.newArrayList());
    assertNull(parent.getFirstChild());
  }
}
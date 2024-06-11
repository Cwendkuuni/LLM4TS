package com.google.javascript.jscomp;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RemoveConstantExpressionsTest {
  private RemoveConstantExpressions pass;
  private AbstractCompiler compilerMock;
  private Node rootMock;

  @Before
  public void setup() {
    compilerMock = mock(AbstractCompiler.class);
    rootMock = new Node(Token.PROGRAM, null);
    pass = new RemoveConstantExpressions(compilerMock);
  }

  @Test
  public void testProcess() {
    Result result = new Result();
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    when(compilerMock.getTraversal()).thenReturn(traversalMock);

    pass.process(null, rootMock);

    ArgumentCaptor<AbstractPostOrderCallback> callbackCaptor =
        ArgumentCaptor.forClass(AbstractPostOrderCallback.class);
    verify(traversalMock).traverse(any(), rootMock, callbackCaptor.capture());

    AbstractPostOrderCallback cb = callbackCaptor.getValue();
    assertEquals(pass.removeConstantRValuesCallback, cb);
  }

  @Test
  public void testTrySimplifyWithNoSideEffectExpression() {
    Node parentMock = new Node(Token.ASSIGN, null);
    Node inputNode = new Node(Token.EXPR_RESULT, null);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    cb.trySimplify(parentMock, inputNode);

    assertNull(cb.changeProxy.getReplacementNode());
  }

  @Test
  public void testTrySimplifyWithSideEffectExpression() {
    Node parentMock = new Node(Token.ASSIGN, null);
    Node sideEffectExpression = new Node(Token.EXPR_RESULT, new Node(Token.NAME, "console.log"));

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    cb.trySimplify(parentMock, sideEffectExpression);

    Node replacementNode = new Node(Token.CALL, null, new Node(Token.NAME, "console.log"));
    assertEquals(replacementNode, cb.changeProxy.getReplacementNode());
  }

  @Test
  public void testGetSideEffectNodesWithNoSideEffectExpressions() {
    Node inputNode = new Node(Token.NUMBER, 1);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    List<Node> sideEffectNodes = cb.getSideEffectNodes(inputNode);

    assertEquals(0, sideEffectNodes.size());
  }

  @Test
  public void testGetSideEffectNodesWithSideEffectExpressions() {
    Node sideEffectExpression1 = new Node(Token.CALL, null, new Node(Token.NAME, "console.log"));
    Node sideEffectExpression2 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"));

    Node inputNode = new Node(Token.EXPR_RESULT, Lists.newArrayList(sideEffectExpression1, sideEffectExpression2));

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    List<Node> sideEffectNodes = cb.getSideEffectNodes(inputNode);

    List<Node> expectedSideEffectNodes = newArrayList(sideEffectExpression1, sideEffectExpression2);
    assertEquals(expectedSideEffectNodes, sideEffectNodes);
  }
}
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {
  private RemoveConstantExpressions removeConstantExpressions;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testProcess() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);
    RemoveConstantExpressions.RemoveConstantRValuesCallback cb = mock(RemoveConstantExpressions.RemoveConstantRValuesCallback.class);

    when(cb.getResult()).thenReturn(mock(Result.class));

    removeConstantExpressions.process(externs, root);

    verify(compiler).reportCodeChanged(false);
    verify(cb).getResult();
  }

  @Test
  public void testVisit_withNoParentNodeDoesNotTrySimplify() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = null;

    removeConstantExpressions.new RemoveConstantRValuesCallback().visit(traversal, node, parent);

    verify(node, never()).getType();
  }

  @Test
  public void testVisit_withParentNodeDoesNotTrySimplify() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);

    when(node.getType()).thenReturn(Token.VAR);

    removeConstantExpressions.new RemoveConstantRValuesCallback().visit(traversal, node, parent);

    verify(parent, never()).getFirstChild();
  }

  @Test
  public void testVisit_withParentNodeOfTypeExprResultAndNoSideEffectsCallsReplaceWith() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Node exprBody = mock(Node.class);
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
    RemoveConstantExpressions.AstChangeProxy changeProxy = callback.changeProxy;

    when(node.getType()).thenReturn(Token.EXPR_RESULT);
    when(node.getFirstChild()).thenReturn(exprBody);
    when(exprBody.getType()).thenReturn(Token.SUB);

    callback.visit(traversal, node, parent);

    verify(changeProxy).replaceWith(eq(parent), eq(node), anyList());
  }

  @Test
  public void testVisit_withParentNodeOfTypeExprResultAndHasSideEffectsDoesNotCallReplaceWith() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Node exprBody = mock(Node.class);
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
    RemoveConstantExpressions.AstChangeProxy changeProxy = callback.changeProxy;

    when(node.getType()).thenReturn(Token.EXPR_RESULT);
    when(node.getFirstChild()).thenReturn(exprBody);
    when(exprBody.getType()).thenReturn(Token.CALL);

    callback.visit(traversal, node, parent);

    verify(changeProxy, never()).replaceWith(eq(parent), eq(node), anyList());
  }

  @Test
  public void testGetSideEffectNodes() {
    Node node = mock(Node.class);
    NodeTraversal traversal = mock(NodeTraversal.class);
    List<Node> subexpressions = mock(List.class);
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
    RemoveConstantExpressions.GatherSideEffectSubexpressionsCallback gatherCallback = callback.new GatherSideEffectSubexpressionsCallback(traversal, subexpressions);
    RemoveConstantExpressions.GatherSideEffectSubexpressionsCallback.CopySideEffectSubexpressions copyCallback = gatherCallback.new CopySideEffectSubexpressions(traversal, subexpressions);

    when(traversal.traverse(eq(null), eq(node), eq(gatherCallback))).thenReturn(null);
    when(subexpressions.add(any(Node.class))).thenReturn(true);

    List<Node> result = callback.getSideEffectNodes(node);

    assertEquals(subexpressions, result);
    verify(traversal).traverse(eq(null), eq(node), eq(gatherCallback));
    verify(subexpressions).add(any(Node.class));
  }

  @Test
  public void testGetResult() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
    Result expectedResult = mock(Result.class);

    callback.result = expectedResult;

    Result result = callback.getResult();

    assertEquals(expectedResult, result);
  }

  @Test
  public void testAstChangeProxy_nodeRemoved() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback.Result result = mock(RemoveConstantExpressions.RemoveConstantRValuesCallback.Result.class);
    RemoveConstantExpressions.ReportCodeHasChangedListener listener = removeConstantExpressions.new ReportCodeHasChangedListener(result);
    Node node = mock(Node.class);

    listener.nodeRemoved(node);

    verify(result).changed = true;
  }
}
import org.junit.Test;

public class RemoveConstantExpressionsTest {

    @Test
    public void testProcess() {
        RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new AbstractCompiler());
        Node externs = new Node();
        Node root = new Node();
        removeConstantExpressions.process(externs, root);
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        removeConstantRValuesCallback.visit(traversal, node, parent);
    }

    @Test
    public void testTrySimplify() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node();
        Node node = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node();
        removeConstantRValuesCallback.trySimplify(parent, node, exprBody);
    }

    @Test
    public void testGetSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node node = new Node();
        List<Node> sideEffectNodes = removeConstantRValuesCallback.getSideEffectNodes(node);
    }

    @Test
    public void testReportCodeHasChangedListener() {
        RemoveConstantExpressions.ReportCodeHasChangedListener listener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());
        Node node = new Node();
        listener.nodeRemoved(node);
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class RemoveConstantExpressionsTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        pass.process(externs, root);
        //TODO: add assertions for the output
    }
    
    @Test
    public void testRemoveConstantRValuesCallback() {
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.ROOT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        callback.visit(traversal, node, parent);
        //TODO: add assertions for the changes made by the callback method
    }
    
    @Test
    public void testTrySimplify() {
        Node parent = new Node(Token.ROOT);
        Node node = new Node(Token.EXPR_RESULT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        callback.trySimplify(parent, node);
        //TODO: add assertions for the changes made by the trySimplify method
    }
    
    @Test
    public void testGetSideEffectNodes() {
        Node node = new Node(Token.EXPR_RESULT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        List<Node> sideEffectNodes = callback.getSideEffectNodes(node);
        //TODO: add assertions for the sideEffectNodes
    }
    
    @Test
    public void testReportCodeHasChangedListener() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback.Result result = 
                new RemoveConstantExpressions.RemoveConstantRValuesCallback.Result();
        RemoveConstantExpressions.ReportCodeHasChangedListener listener = 
                new RemoveConstantExpressions.ReportCodeHasChangedListener(result);
        Node node = new Node(Token.EXPR_RESULT);
        listener.nodeRemoved(node);
        //TODO: assert that the result.changed flag is set to true
    }
}
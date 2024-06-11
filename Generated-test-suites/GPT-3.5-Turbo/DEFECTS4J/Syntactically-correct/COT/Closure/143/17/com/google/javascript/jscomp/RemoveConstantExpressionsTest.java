import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.rhino.Node;

public class RemoveConstantExpressionsTest {
    
    // Test removeConstantExpressions.process(Node externs, Node root)
    @Test
    public void testProcess() {
        RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new AbstractCompiler());
        Node externs = new Node();
        Node root = new Node();
        removeConstantExpressions.process(externs, root);
        // TODO: Assert expected behavior
    }
    
    // Test RemoveConstantRValuesCallback.visit(NodeTraversal traversal, Node node, Node parent)
    @Test
    public void testVisit() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        removeConstantRValuesCallback.visit(traversal, node, parent);
        // TODO: Assert expected behavior
    }
    
    // Test RemoveConstantRValuesCallback.trySimplify(Node parent, Node node)
    @Test
    public void testTrySimplify() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node();
        Node node = new Node();
        removeConstantRValuesCallback.trySimplify(parent, node);
        // TODO: Assert expected behavior
    }
    
    // Test RemoveConstantRValuesCallback.getSideEffectNodes(Node node)
    @Test
    public void testGetSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node node = new Node();
        removeConstantRValuesCallback.getSideEffectNodes(node);
        // TODO: Assert expected behavior
    }
    
    // Test RemoveConstantRValuesCallback.getResult()
    @Test
    public void testGetResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        removeConstantRValuesCallback.getResult();
        // TODO: Assert expected behavior
    }
    
    // Test RemoveConstantRValuesCallback.RemoveConstantRValuesCallback()
    @Test
    public void testRemoveConstantRValuesCallback() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        // TODO: Assert expected behavior
    }
    
    // Test ReportCodeHasChangedListener.nodeRemoved(Node node)
    @Test
    public void testNodeRemoved() {
        RemoveConstantExpressions.ReportCodeHasChangedListener reportCodeHasChangedListener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new Result());
        Node node = new Node();
        reportCodeHasChangedListener.nodeRemoved(node);
        // TODO: Assert expected behavior
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    Node externs = new Node();
    Node root = new Node();
    removeConstantExpressions.process(externs, root);
    // TODO: Add assertions
  }

  @Test
  public void testRemoveConstantRValuesCallback() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node();
    Node parent = new Node();
    callback.visit(traversal, node, parent);
    // TODO: Add assertions
  }

  @Test
  public void testTrySimplify() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node();
    Node node = new Node();
    callback.trySimplify(parent, node);
    // TODO: Add assertions
  }

  @Test
  public void testGetSideEffectNodes() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node node = new Node();
    List<Node> sideEffectNodes = callback.getSideEffectNodes(node);
    // TODO: Add assertions
  }

  @Test
  public void testReportCodeHasChangedListener() {
    RemoveConstantExpressions.ReportCodeHasChangedListener listener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());
    listener.nodeRemoved(new Node());
    // TODO: Add assertions
  }
}
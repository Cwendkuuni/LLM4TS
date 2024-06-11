import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {
  
  @Test
  public void testProcess() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new AbstractCompiler());
    Node externs = new Node();
    Node root = new Node();
    
    removeConstantExpressions.process(externs, root);
    
    // assertions
  }
  
  @Test
  public void testTrySimplify_WithNoSideEffects() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node();
    Node node = new Node(Token.EXPR_RESULT);
    
    // node body has no side effects
    Node exprBody = new Node();
    node.addChildToFront(exprBody);
    
    callback.trySimplify(parent, node);
    
    // assertions
  }
  
  @Test
  public void testTrySimplify_WithSideEffects() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node();
    Node node = new Node(Token.EXPR_RESULT);
    
    // node body has side effects
    Node exprBody = new Node(Token.FOO);
    node.addChildToFront(exprBody);
    
    callback.trySimplify(parent, node);
    
    // assertions
  }
  
  @Test
  public void testGetSideEffectNodes() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node node = new Node();
    Node subexpression = new Node();
    List<Node> subexpressions = new ArrayList<>();
    subexpressions.add(subexpression);
    
    List<Node> result = callback.getSideEffectNodes(node);
    
    // assertions
  }
  
  @Test
  public void testGetResult() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    
    RemoveConstantExpressions.Result result = callback.getResult();
    
    // assertions
  }
  
  @Test
  public void testNodeRemoved() {
    RemoveConstantExpressions.ReportCodeHasChangedListener listener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());
    Node node = new Node();
    
    listener.nodeRemoved(node);
    
    // assertions
  }
}

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {
  
  @Test
  public void testProcess() {
    RemoveConstantExpressions re = new RemoveConstantExpressions(new AbstractCompiler());
    
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    
    re.process(externs, root);
    
    // Assert something here
  }
  
  @Test
  public void testRemoveConstantRValuesCallback() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback rcr = 
      new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    
    NodeTraversal traversal = null;
    Node node = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.BLOCK);
    
    rcr.visit(traversal, node, parent);
    
    // Assert something here
  }
  
  @Test
  public void testTrySimplify() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback rcr = 
      new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    
    Node parent = new Node(Token.BLOCK);
    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NAME, "testNode"));
    
    rcr.trySimplify(parent, node);
    
    // Assert something here
  }
  
  @Test
  public void testGetSideEffectNodes() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback rcr = 
      new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    
    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NAME, "testNode"));
    
    rcr.getSideEffectNodes(node);
    
    // Assert something here
  }
  
  @Test
  public void testReportCodeHasChangedListener() {
    RemoveConstantExpressions.ReportCodeHasChangedListener rcl = 
      new RemoveConstantExpressions.ReportCodeHasChangedListener(new Result());
    
    Node node = new Node(Token.EXPR_RESULT);
    
    rcl.nodeRemoved(node);
    
    // Assert something here
  }
  
}

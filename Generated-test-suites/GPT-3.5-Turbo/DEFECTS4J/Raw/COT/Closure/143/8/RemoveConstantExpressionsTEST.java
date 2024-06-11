import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RemoveConstantExpressionsTest {
  
  // 
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    
    Node externs = new Node();
    Node root = new Node();
    pass.process(externs, root);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testRemoveConstantRValuesCallback() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    
    List<Node> subexpressions = callback.getSideEffectNodes(new Node(Token.NUMBER));
    assertEquals(1, subexpressions.size());
  }
  
  @Test
  public void testTrySimplify() {
    Node parent = new Node();
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node();
    node.addChildToBack(exprBody);
    
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    callback.trySimplify(parent, node);
    
    // TODO: Add assertions
  }
  
  // TODO: Add more test cases for other methods in the class
  
  // 
}
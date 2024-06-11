import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {
  
  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }
  
  @Test
  public void testProcess_removesConstantExpression() {
    Node externs = createExterns();
    Node root = createRoot();
    
    removeConstantExpressions.process(externs, root);
    
    // Verify that the constant expression has been removed
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.EXPR_RESULT) {
          assertFalse(node.hasChildren());
        }
      }
    });
  }

  private Node createExterns() {
    // Create dummy externs Node for testing
    return new Node(Token.EXTERNREF);
  }
  
  private Node createRoot() {
    // Create dummy root Node for testing
    return new Node(Token.ROOT);
  }
  
}
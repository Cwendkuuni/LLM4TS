import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, new TestCallback());
    Node n = new Node(Token.FUNCTION);
    
    boolean result = check.shouldTraverse(t, n, null);
    
    assertTrue(result);
  }

  @Test
  public void testVisit() {
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, new TestCallback());
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    check.visit(t, n, parent);
    
    // Check if error reported
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);

    JSDocInfo result = check.getFunctionJsDocInfo(n);

    assertNull(result);
  }

  class TestCallback implements NodeTraversal.Callback {
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      return true;
    }

    public void visit(NodeTraversal t, Node n, Node parent) {
      // Empty implementation for testing
    }
  }

}
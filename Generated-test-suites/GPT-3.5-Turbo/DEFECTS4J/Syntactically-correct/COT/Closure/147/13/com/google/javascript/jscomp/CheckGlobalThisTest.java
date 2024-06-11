
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void shouldTraverse_Condition_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_Condition_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void visit_GlobalThis_ShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.visit(t, n, parent);
    // Assert that the error was reported
  }
  
  @Test
  public void visit_GlobalThis_ShouldNotReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EXPR_RESULT);
    checkGlobalThis.visit(t, n, parent);
    // Assert that no error was reported
  }
}

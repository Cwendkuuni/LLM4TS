import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test shouldTraverse() method

    // Non-global context, should return false
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(new Callback() {
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return false;
      }

      public void visit(NodeTraversal t, Node n, Node parent) {}
    });
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean actual = checker.shouldTraverse(t, n, parent);
    boolean expected = false;
    assertEquals(expected, actual);

    // Global context, should return true
    n = new Node(Token.FUNCTION);
    parent = new Node(Token.NAME);
    actual = checker.shouldTraverse(t, n, parent);
    expected = true;
    assertEquals(expected, actual);

    // Block context, should return true
    parent = new Node(Token.BLOCK);
    actual = checker.shouldTraverse(t, n, parent);
    assertEquals(expected, actual);

    // Script context, should return true
    parent = new Node(Token.SCRIPT);
    actual = checker.shouldTraverse(t, n, parent);
    assertEquals(expected, actual);

    // Assign context, should return true
    parent = new Node(Token.ASSIGN);
    actual = checker.shouldTraverse(t, n, parent);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisit() {
    // Test visit() method

    // Node is THIS and should report
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(new Callback() {
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      public void visit(NodeTraversal t, Node n, Node parent) {
        assertEquals(n.getType(), Token.THIS);
      }
    });
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    checker.visit(t, n, parent);

    // Node is not THIS, should not report
    parent = new Node(Token.ASSIGN);
    n = new Node(Token.FALSE);
    checker.visit(t, n, parent);
  }

  @Test
  public void testShouldReportThis() {
    // Test shouldReportThis() method

    // parent is null, should not report
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(new Callback() {
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      public void visit(NodeTraversal t, Node n, Node parent) {}
    });
    Node n = new Node(Token.THIS);
    Node parent = null;

    boolean actual = checker.shouldReportThis(n, parent);
    boolean expected = false;
    assertEquals(expected, actual);

    // assignLhsChild is not null, should report
    checker.assignLhsChild = new Node(Token.FALSE);

    actual = checker.shouldReportThis(n, parent);
    expected = true;
    assertEquals(expected, actual);

    // parent is GET, should report
    parent = new Node(Token.GETPROP);
    n = new Node(Token.THIS);
  
    actual = checker.shouldReportThis(n, parent);
    assertEquals(expected, actual);
  }
}
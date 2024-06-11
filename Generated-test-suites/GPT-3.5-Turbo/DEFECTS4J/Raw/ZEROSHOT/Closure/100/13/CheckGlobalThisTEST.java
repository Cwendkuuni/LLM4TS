import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_functionConstructor_hasThisAnnotation_false() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", null);
    Node parent = null;

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(shouldTraverse);
  }

  @Test
  public void shouldTraverse_functionConstructor_hasThisAnnotation_true() {
    NodeTraversal t = new NodeTraversal();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    jsDoc.setThisType(true);
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", jsDoc);
    Node parent = null;

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(shouldTraverse);
  }

  @Test
  public void shouldTraverse_functionConstructor_noAnnotations_true() {
    NodeTraversal t = new NodeTraversal();
    JSDocInfo jsDoc = new JSDocInfo();
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", jsDoc);
    Node parent = null;

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(shouldTraverse);
  }

  @Test
  public void visit_this_shouldReportThis_true() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    assertTrue(shouldReportThis);
  }

  @Test
  public void visit_this_shouldReportThis_false() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = null;

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);

    assertFalse(shouldReportThis);
  }
}
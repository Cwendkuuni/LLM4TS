import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverse_WithGlobalKeyword() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_WithFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_WithNonGlobalContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void testVisit_WithThisAndParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    checkGlobalThis.visit(t, n, parent);

    // Assert that there was a report made
    // compiler.report should be called with t.makeError(n, level, GLOBAL_THIS)
  }

  @Test
  public void testVisit_WithThisAndNullParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = null;

    checkGlobalThis.visit(t, n, parent);

    // Assert that there was a report made
    // compiler.report should be called with t.makeError(n, level, GLOBAL_THIS)
  }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(null, null, null);
  }

  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    scopedAliases.process(externs, root);
  }

  @Test
  public void testHotSwapScript() {
    Node root = null;
    Node originalRoot = null;
    scopedAliases.hotSwapScript(root, originalRoot);
  }

  @Test
  public void testEnterScope() {
    NodeTraversal t = null;
    scopedAliases.enterScope(t);
  }

  @Test
  public void testExitScope() {
    NodeTraversal t = null;
    scopedAliases.exitScope(t);
  }

  @Test
  public void testShouldTraverse() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    boolean result = scopedAliases.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testVisit() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    scopedAliases.visit(t, n, parent);
  }
}
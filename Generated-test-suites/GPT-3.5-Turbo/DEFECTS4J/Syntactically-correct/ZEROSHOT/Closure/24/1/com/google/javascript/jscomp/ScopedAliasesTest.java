import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {
  
  private ScopedAliases scopedAliases;
  
  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases();
  }
  
  @Test
  public void testHotSwapScript() {
    Node root = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    scopedAliases.hotSwapScript(root, originalRoot);
    // assert something here
  }
  
  @Test
  public void testEnterScope() {
    NodeTraversal t = new NodeTraversal();
    scopedAliases.enterScope(t);
    // assert something here
  }
  
  @Test
  public void testExitScope() {
    NodeTraversal t = new NodeTraversal();
    scopedAliases.exitScope(t);
    // assert something here
  }
  
  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    boolean result = scopedAliases.shouldTraverse(t, n, parent);
    // assert something here
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    scopedAliases.visit(t, n, parent);
    // assert something here
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    scopedAliases.process(externs, root);
    // assert something here
  }
  
}
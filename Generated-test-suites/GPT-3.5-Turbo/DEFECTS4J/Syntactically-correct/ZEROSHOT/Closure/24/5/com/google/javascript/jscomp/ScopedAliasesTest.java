import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  
  private ScopedAliases scopedAliases;
  
  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases();
  }
  
  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    scopedAliases.process(externs, root);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testHotSwapScript() {
    Node root = null;
    Node originalRoot = null;
    scopedAliases.hotSwapScript(root, originalRoot);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testEnterScope() {
    NodeTraversal t = null;
    scopedAliases.enterScope(t);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testExitScope() {
    NodeTraversal t = null;
    scopedAliases.exitScope(t);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testShouldTraverse() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    boolean result = scopedAliases.shouldTraverse(t, n, parent);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    scopedAliases.visit(t, n, parent);
    
    // TODO: Add assertions
  }
  
  // TODO: Add tests for other methods in the ScopedAliases class
  
}
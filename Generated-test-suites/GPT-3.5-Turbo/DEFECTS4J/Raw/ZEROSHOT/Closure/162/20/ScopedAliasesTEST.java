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
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    
    scopedAliases.process(externs, root);
  }
  
  @Test
  public void testHotSwapScript() {
    Node root = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    
    scopedAliases.hotSwapScript(root, originalRoot);
  }
  
  @Test
  public void testEnterScope() {
    NodeTraversal t = new NodeTraversal();
    
    scopedAliases.enterScope(t);
  }
  
  @Test
  public void testExitScope() {
    NodeTraversal t = new NodeTraversal();
    
    scopedAliases.exitScope(t);
  }
  
  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    
    scopedAliases.shouldTraverse(t, n, parent);
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    
    scopedAliases.visit(t, n, parent);
  }
  
  @Test
  public void testFixTypeNode() {
    Node typeNode = new Node(Token.SCRIPT);
    
    scopedAliases.fixTypeNode(typeNode);
  }
}
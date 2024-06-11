import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {
  
  private SyntacticScopeCreator scopeCreator;
  
  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }
  
  @Test
  public void testCreateScope() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    Scope scope = scopeCreator.createScope(n, parent);
    assertNotNull(scope);
  }
  
  @Test
  public void testScanVars_var() {
    Node n = new Node(Token.VAR);
    Node child = new Node(Token.NAME, new Node(Token.STRING, "x"));
    n.addChildToBack(child);
    Node parent = null;
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.getScope();
    assertTrue(scope.isDeclared("x", false));
  }
  
  @Test
  public void testScanVars_function() {
    Node n = new Node(Token.FUNCTION);
    Node child = new Node(Token.NAME, new Node(Token.STRING, "x"));
    n.addChildToBack(child);
    Node parent = null;
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.getScope();
    assertTrue(scope.isDeclared("x", false));
  }
  
  @Test
  public void testScanVars_catch() {
    Node n = new Node(Token.CATCH);
    Node catchVar = new Node(Token.NAME, new Node(Token.STRING, "e"));
    n.addChildToBack(catchVar);
    Node var = new Node(Token.VAR);
    Node child = new Node(Token.NAME, new Node(Token.STRING, "x"));
    var.addChildToBack(child);
    n.addChildToBack(var);
    Node parent = null;
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.getScope();
    assertTrue(scope.isDeclared("e", false));
    assertTrue(scope.isDeclared("x", false));
  }
  
  @Test
  public void testScanVars_script() {
    Node n = new Node(Token.SCRIPT);
    Node var = new Node(Token.VAR);
    Node child = new Node(Token.NAME, new Node(Token.STRING, "x"));
    var.addChildToBack(child);
    n.addChildToBack(var);
    Node parent = null;
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.getScope();
    assertTrue(scope.isDeclared("x", false));
  }
  
}
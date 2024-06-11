import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {
  
  @Test
  public void testCreateScope() {
    // Test case for creating a scope with null parent
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.FUNCTION);
    Scope parent = null;
    Scope scope = scopeCreator.createScope(n, parent);
    assertNotNull(scope);
  }
  
  @Test
  public void testScanRoot() {
    // Test case for scanning root node
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    scopeCreator.createScope(n, parent);
    assertNotNull(scopeCreator.getSourceName());
  }
  
  @Test
  public void testScanVars() {
    // Test case for scanning variables
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    scopeCreator.scanVars(n, parent);
    assertEquals(0, scopeCreator.getScope().getVarCount());
  }
  
  @Test
  public void testRedeclarationHandler() {
    // Test case for redeclaring a variable
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    scopeCreator.declareVar("x", n, parent, null, null, n);
    assertEquals(1, scope.getVarCount());
  }
}
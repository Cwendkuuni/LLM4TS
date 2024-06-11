import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  
  SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
  
  @Test
  public void testCreateScope() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    Scope scope = scopeCreator.createScope(n, parent);
    assertNotNull(scope);
  }
  
  @Test
  public void testScanVars_VarDeclaration() {
    Node n = new Node(Token.VAR);
    Node varNode = new Node(Token.NAME, "x");
    n.addChildToBack(varNode);
    Scope parent = null;
    scopeCreator.createScope(n, parent);
    Scope scope = scopeCreator.createScope(varNode, null);
    assertNotNull(scope.getVar("x"));
  }
  
  @Test
  public void testScanVars_FunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node name = new Node(Token.NAME, "foo");
    n.addChildToBack(name);
    Scope parent = null;
    scopeCreator.createScope(n, parent);
    Scope scope = scopeCreator.createScope(name, null);
    assertNotNull(scope.getVar("foo"));
  }
  
  @Test
  public void testScanVars_Catch() {
    Node n = new Node(Token.CATCH);
    Node varNode = new Node(Token.NAME, "e");
    n.addChildToBack(varNode);
    Scope parent = null;
    scopeCreator.createScope(n, parent);
    Scope scope = scopeCreator.createScope(varNode, null);
    assertNotNull(scope.getVar("e"));
  }
}
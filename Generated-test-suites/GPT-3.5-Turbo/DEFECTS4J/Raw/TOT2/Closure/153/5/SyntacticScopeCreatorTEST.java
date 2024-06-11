import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    // Test creating a scope with a simple node
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.EMPTY);
    Scope scope = scopeCreator.createScope(n, null);
    assertNotNull(scope);
  }

  @Test
  public void testDeclareVar() {
    // Test declaring a variable in a scope
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = new Node(Token.FUNCTION);
    JSType declaredType = null;

    scopeCreator.declareVar("testVar", n, parent, gramps, declaredType, parent);
    Scope scope = scopeCreator.createScope(parent, null);
    assertTrue(scope.isDeclared("testVar", false));
  }

  @Test
  public void testRedeclarationHandler() {
    // Test handling redeclaration of a variable
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = new Node(Token.FUNCTION);
    JSType declaredType = null;

    scopeCreator.declareVar("testVar", n, parent, gramps, declaredType, parent);
    
    // Try declaring the same variable again
    scopeCreator.redeclarationHandler.onRedeclaration(scopeCreator.scope, "testVar", n, parent, gramps, parent);
    Scope scope = scopeCreator.createScope(parent, null);
    assertTrue(scope.isDeclared("testVar", false));
  }

}
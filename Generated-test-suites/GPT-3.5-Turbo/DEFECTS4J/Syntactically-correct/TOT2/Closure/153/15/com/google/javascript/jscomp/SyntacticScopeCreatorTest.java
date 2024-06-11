import static org.junit.Assert.*;

import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node n = new Node(Token.FUNCTION);
    Scope parent = null;

    Scope scope = creator.createScope(n, parent);

    assertNotNull(scope);
  }

  @Test
  public void testScanRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node n = new Node(Token.FUNCTION);
    Scope parent = null;

    creator.createScope(n, parent);

    assertNull(creator.getSourceName());
  }

  @Test
  public void testScanVars() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.FUNCTION);

    Scope scope = new Scope(parent, compiler);

    creator.scanVars(n, parent);

    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testDeclateVar() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.FUNCTION);

    Scope scope = new Scope(parent, compiler);

    creator.declareVar("testVar", n, parent, null, null, n);

    Scope.Var var = scope.getVar("testVar");

    assertNotNull(var);
    assertEquals("testVar", var.name);
  }

}
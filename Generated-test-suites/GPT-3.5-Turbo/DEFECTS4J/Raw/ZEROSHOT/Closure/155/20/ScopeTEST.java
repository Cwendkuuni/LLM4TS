import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    assertSame(var, scope.getVar("testVar"));
    assertNull(scope.getVar("nonExistentVar"));
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    scope.undeclare(var);
    assertNull(scope.getVar("testVar"));
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, null);
    assertEquals(0, scope.getVarCount());
    scope.declare("testVar1", new Node(Token.NAME), null, null);
    assertEquals(1, scope.getVarCount());
    scope.declare("testVar2", new Node(Token.NAME), null, null);
    assertEquals(2, scope.getVarCount());
    scope.undeclare(scope.getVar("testVar1"));
    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isDeclared("testVar", false));
    scope.declare("testVar", new Node(Token.NAME), null, null);
    assertTrue(scope.isDeclared("testVar", false));
    assertFalse(scope.isDeclared("nonExistentVar", false));
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    assertTrue(globalScope.isGlobal());
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    assertFalse(globalScope.isLocal());
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    assertTrue(localScope.isLocal());
  }
}
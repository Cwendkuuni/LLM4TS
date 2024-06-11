import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

  private Scope scope;
  private Node rootNode;

  @Before
  public void setUp() {
    rootNode = new Node(Token.FUNCTION);
    scope = new Scope(rootNode);
  }

  @Test
  public void testDeclare() {
    Var var = scope.declare("x", new Node(Token.NAME), null, null);
    
    assertNotNull(var);
    assertNotNull(var.nameNode);
    assertEquals("x", var.name);
    assertEquals(scope, var.scope);
  }

  @Test
  public void testUndeclare() {
    Var var = scope.declare("x", new Node(Token.NAME), null, null);
    
    scope.undeclare(var);

    assertNull(scope.getVar("x"));
  }

  @Test
  public void testIsDeclared() {
    scope.declare("x", new Node(Token.NAME), null, null);
    
    assertTrue(scope.isDeclared("x", true));
    assertFalse(scope.isDeclared("y", true));
  }

  @Test
  public void testGetVars() {
    scope.declare("x", new Node(Token.NAME), null, null);
    scope.declare("y", new Node(Token.NAME), null, null);
    
    Iterator<Var> vars = scope.getVars();
    
    assertTrue(vars.hasNext());
    vars.next();
    assertTrue(vars.hasNext());
    vars.next();
    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    scope.declare("x", new Node(Token.NAME), null, null);
    scope.declare("y", new Node(Token.NAME), null, null);
    
    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    
    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    
    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }

}
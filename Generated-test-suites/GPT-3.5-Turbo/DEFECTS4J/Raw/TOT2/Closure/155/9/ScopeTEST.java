import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {
  
  @Test
  public void testVarDeclaration() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION));
    Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    assertEquals("testVar", var.getName());
    assertEquals(JSType.UNKNOWN, var.getType());
    assertNull(var.getParentNode());
    assertFalse(var.isGlobal());
    assertTrue(var.isLocal());
  }

  @Test
  public void testUndeclareVariable() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION));
    Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    assertNotNull(scope.getVar("testVar"));
    
    scope.undeclare(var);
    
    assertNull(scope.getVar("testVar"));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION));
    scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    assertTrue(scope.isDeclared("testVar", true));
    assertFalse(scope.isDeclared("undefinedVar", true));
  }

  @Test
  public void testIsGlobalScope() {
    Scope globalScope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    
    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testGetTypeOfThis() {
    Scope scope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
    ObjectType thisType = scope.getTypeOfThis();
    
    assertNotNull(thisType);
    assertEquals(GLOBAL_THIS, thisType.toString());
  }
}
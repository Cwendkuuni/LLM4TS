import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {
  
  @Test
  public void testDeclareVariable() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));
    Var var = scope.declare("myVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertNotNull(var);
    assertEquals("myVar", var.getName());
    assertEquals(JSType.UNKNOWN, var.getType());
  }
  
  @Test
  public void testUndeclareVariable() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));
    Var var = scope.declare("myVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertNotNull(scope.getVar("myVar"));
    scope.undeclare(var);
    assertNull(scope.getVar("myVar"));
  }
  
  @Test
  public void testGetVar() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parentScope, new Node(Token.BLOCK));
    Var var = parentScope.declare("myVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertEquals(var, scope.getVar("myVar"));
  }
  
  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));
    scope.declare("myVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertTrue(scope.isDeclared("myVar", true));
    assertFalse(scope.isDeclared("nonExistentVar", true));
  }
}
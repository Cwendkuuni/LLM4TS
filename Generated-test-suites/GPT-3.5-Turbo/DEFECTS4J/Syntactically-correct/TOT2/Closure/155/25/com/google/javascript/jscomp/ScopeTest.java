import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
    assertEquals("testVar", var.getName());
    assertEquals(JSType.UNKNOWN, var.getType());
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
    scope.undeclare(var);
    assertNull(scope.getVar("testVar"));
  }

  @Test
  public void testGetVar() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, new Node(Token.FUNCTION));
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.BOOLEAN, null);
    assertEquals(var, parentScope.getVar("testVar"));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.NULL, null);
    assertTrue(scope.isDeclared("testVar", true));
    assertFalse(scope.isDeclared("nonExistentVar", false));
  }
}
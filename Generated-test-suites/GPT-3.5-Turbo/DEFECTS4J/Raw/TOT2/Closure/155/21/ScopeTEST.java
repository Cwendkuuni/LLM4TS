import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

  // Test for declare method
  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
  }

  // Test for undeclare method
  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
    assertNotNull(var);
    scope.undeclare(var);
    assertNull(scope.getVar("testVar"));
  }

  // Test for getVar method
  @Test
  public void testGetVar() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
    Var retrievedVar = scope.getVar("testVar");
    assertEquals(var, retrievedVar);
  }

  // Test for isDeclared method
  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
    assertTrue(scope.isDeclared("testVar", false));
  }
}
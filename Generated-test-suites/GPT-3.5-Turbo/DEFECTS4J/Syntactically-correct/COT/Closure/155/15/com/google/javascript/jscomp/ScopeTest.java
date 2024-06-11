
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("name", null, null, null);
    assertNotNull(var);
    assertEquals("name", var.getName());
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("name", null, null, null);
    assertNotNull(var);
    assertEquals("name", var.getName());
    scope.undeclare(var);
    Var undeclaredVar = scope.getVar("name");
    assertNull(undeclaredVar);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isDeclared("name", false));
    Var var = scope.declare("name", null, null, null);
    assertTrue(scope.isDeclared("name", false));
  }

  // Add additional test cases for other methods in the Scope class

  //...

}


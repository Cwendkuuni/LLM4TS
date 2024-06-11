import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

  private Scope scope;

  @Before
  public void setUp() {
    scope = new Scope(null, null);
  }

  @Test
  public void testDeclare() {
    Var var = scope.declare("var", null, null, null);
    assertNotNull(var);
    assertEquals("var", var.getName());
  }

  @Test
  public void testUndeclare() {
    Var var = scope.declare("var", null, null, null);
    assertNotNull(var);
    assertEquals("var", var.getName());
  
    scope.undeclare(var);
    assertNull(scope.getVar("var"));
  }

  @Test
  public void testIsDeclared_true() {
    scope.declare("var", null, null, null);
    assertTrue(scope.isDeclared("var", false));
  }

  @Test
  public void testIsDeclared_false() {
    assertFalse(scope.isDeclared("var", false));
  }

  @Test
  public void testGetVar() {
    Var var = scope.declare("var", null, null, null);
    assertEquals(var, scope.getVar("var"));
  }

  @Test
  public void testGetVar_parentScope() {
    Scope parentScope = new Scope(null, null);
    Var var = parentScope.declare("var", null, null, null);
    scope.setParent(parentScope);
    assertEquals(var, scope.getVar("var"));
  }

  @Test
  public void testIsGlobal_true() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsGlobal_false() {
    Scope parentScope = new Scope(null, null);
    scope.setParent(parentScope);
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsLocal_true() {
    Scope parentScope = new Scope(null, null);
    scope.setParent(parentScope);
    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsLocal_false() {
    assertFalse(scope.isLocal());
  }
}
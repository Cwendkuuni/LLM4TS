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
    Var var = scope.declare("test", null, null);
    assertNotNull(var);
    assertEquals("test", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testGetVar() {
    Var var = scope.declare("test", null, null);
    Var result = scope.getVar("test");
    assertEquals(var, result);
  }

  @Test
  public void testGetVar_null() {
    Var result = scope.getVar("test");
    assertNull(result);
  }

  @Test
  public void testIsDeclared() {
    scope.declare("test", null, null);
    boolean result = scope.isDeclared("test", true);
    assertTrue(result);
  }

  @Test
  public void testIsDeclared_recurse() {
    Scope parentScope = new Scope(null, null);
    parentScope.declare("test", null, null);
    scope = new Scope(parentScope, null);

    boolean result = scope.isDeclared("test", true);
    assertTrue(result);
  }

  @Test
  public void testIsDeclared_recurse_false() {
    Scope parentScope = new Scope(null, null);
    parentScope.declare("test", null, null);
    scope = new Scope(parentScope, null);

    boolean result = scope.isDeclared("test", false);
    assertFalse(result);
  }

  @Test
  public void testIsDeclared_false() {
    boolean result = scope.isDeclared("test", false);
    assertFalse(result);
  }

  @Test
  public void testIsGlobal() {
    boolean result = scope.isGlobal();
    assertTrue(result);
  }

  @Test
  public void testIsLocal() {
    boolean result = scope.isLocal();
    assertFalse(result);
  }

  @Test
  public void testGetVars() {
    Var var1 = scope.declare("test1", null, null);
    Var var2 = scope.declare("test2", null, null);
    Iterator<Var> iterator = scope.getVars();
    assertTrue(iterator.hasNext());
    assertEquals(var1, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(var2, iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testGetVarCount() {
    scope.declare("test1", null, null);
    scope.declare("test2", null, null);
    int result = scope.getVarCount();
    assertEquals(2, result);
  }

  @Test
  public void testGetParentScope() {
    Scope result = scope.getParentScope();
    assertNull(result);
  }
}
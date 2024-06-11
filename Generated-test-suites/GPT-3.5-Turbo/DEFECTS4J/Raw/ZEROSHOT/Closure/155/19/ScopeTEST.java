import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("name", null, null, null);
    assertEquals(var, scope.getVar("name"));
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("name", null, null, null);
    scope.undeclare(var);
    assertNull(scope.getVar("name"));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isDeclared("name", false));
    Var var = scope.declare("name", null, null, null);
    assertTrue(scope.isDeclared("name", false));
    scope.undeclare(var);
    assertFalse(scope.isDeclared("name", false));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("name1", null, null, null);
    Var var2 = scope.declare("name2", null, null, null);
    assertEquals(2, scope.getVarCount());
    assertTrue(scope.getVars().contains(var1));
    assertTrue(scope.getVars().contains(var2));
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, null);
    assertTrue(scope.isGlobal());
    Scope nestedScope = new Scope(scope, null);
    assertFalse(nestedScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isLocal());
    Scope nestedScope = new Scope(scope, null);
    assertTrue(nestedScope.isLocal());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType objectType = new ObjectType();
    Scope scope = new Scope(null, objectType);
    assertEquals(objectType, scope.getTypeOfThis());
  }

}
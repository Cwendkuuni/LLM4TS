import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("x", null, null, null);

    assertNotNull(var);
    assertEquals("x", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertNull(var.getJSDocInfo());
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, null, null);
    Var var2 = scope.declare("y", null, null, null);

    scope.undeclare(var1);
    assertNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("x", null, null, null);
    scope.declare("y", null, null, null);

    assertTrue(scope.isDeclared("x", true));
    assertFalse(scope.isDeclared("z", true));
    assertFalse(scope.isDeclared("z", false));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, null);
    scope.declare("x", null, null, null);
    scope.declare("y", null, null, null);

    Iterator<Var> iterator = scope.getVars();

    assertTrue(iterator.hasNext());
    assertEquals("x", iterator.next().getName());

    assertTrue(iterator.hasNext());
    assertEquals("y", iterator.next().getName());

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, null);
    scope.declare("x", null, null, null);
    scope.declare("y", null, null, null);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, null);

    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, null);

    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }
}
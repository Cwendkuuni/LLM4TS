import org.junit.Assert;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("x", null, null, null, true);
    Assert.assertNotNull(var);
    Assert.assertEquals("x", var.getName());
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("x", null, null, null, true);
    scope.undeclare(var);
    Assert.assertNull(scope.getVar("x"));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("x", null, null, null, true);
    Assert.assertTrue(scope.isDeclared("x", true));
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, null);
    Assert.assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope scope = new Scope(new Scope(null, null), null);
    Assert.assertTrue(scope.isLocal());
  }
}
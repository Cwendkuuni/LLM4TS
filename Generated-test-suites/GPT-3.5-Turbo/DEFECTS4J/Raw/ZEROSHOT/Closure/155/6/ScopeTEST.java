import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    scope.declare("var1", null, null, null);
    scope.declare("var2", null, null, null);
    scope.declare("var3", null, null, null);

    assert(scope.isDeclared("var1", false));
    assert(scope.isDeclared("var2", false));
    assert(scope.isDeclared("var3", false));
    assert(!scope.isDeclared("var4", false));
  }

  @Test
  public void testGetVar() {
    Scope scope = new Scope(null, null);
    scope.declare("var1", null, null, null);
    scope.declare("var2", null, null, null);
    scope.declare("var3", null, null, null);

    assert(scope.getVar("var1") != null);
    assert(scope.getVar("var2") != null);
    assert(scope.getVar("var3") != null);
    assert(scope.getVar("var4") == null);
  }

  @Test
  public void testIsGlobal() {
    Scope scope1 = new Scope(null, null);
    Scope scope2 = new Scope(scope1, null);

    assert(scope1.isGlobal());
    assert(!scope2.isGlobal());
  }
}
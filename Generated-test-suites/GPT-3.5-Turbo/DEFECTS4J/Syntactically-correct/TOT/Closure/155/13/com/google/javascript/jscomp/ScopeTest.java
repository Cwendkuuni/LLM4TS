import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  private Scope scope;
  private Scope.Var var1;
  private Scope.Var var2;
  private Scope.Var var3;

  @Before
  public void setUp() {
    scope = new Scope(null, null);
    var1 = scope.declare("var1", null, null, null);
    var2 = scope.declare("var2", null, null, null);
    var3 = scope.declare("var3", null, null, null);
  }

  @Test
  public void testDeclare() {
    assertEquals("var1", var1.getName());
    assertEquals("var2", var2.getName());
    assertEquals("var3", var3.getName());

    assertEquals(null, var1.getType());
    assertEquals(null, var2.getType());
    assertEquals(null, var3.getType());

    assertEquals(null, var1.getNameNode());
    assertEquals(null, var2.getNameNode());
    assertEquals(null, var3.getNameNode());

    assertFalse(var1.isTypeInferred());
    assertFalse(var2.isTypeInferred());
    assertFalse(var3.isTypeInferred());
  }

  @Test
  public void testUndeclare() {
    scope.undeclare(var1);
    assertNull(scope.getVar("var1"));
    assertNotNull(scope.getVar("var2"));
    assertNotNull(scope.getVar("var3"));

    scope.undeclare(var2);
    assertNull(scope.getVar("var2"));
    assertNotNull(scope.getVar("var3"));

    scope.undeclare(var3);
    assertNull(scope.getVar("var3"));
  }

  @Test
  public void testIsDeclared() {
    assertTrue(scope.isDeclared("var1", true));
    assertTrue(scope.isDeclared("var2", true));
    assertTrue(scope.isDeclared("var3", true));

    assertFalse(scope.isDeclared("var4", true));
    assertFalse(scope.isDeclared("var4", false));
  }

  @Test
  public void testGetVars() {
    assertEquals(3, scope.getVarCount());

    // Iterate and make sure we get all the declared variables
    int count = 0;
    for (Scope.Var var : scope.getVars()) {
      if (var.getName().equals("var1")) {
        count++;
      } else if (var.getName().equals("var2")) {
        count++;
      } else if (var.getName().equals("var3")) {
        count++;
      }
    }
    assertEquals(3, count);
  }
}
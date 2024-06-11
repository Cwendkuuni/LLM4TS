import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  
  private Scope scope;

  @Before
  public void setUp() {
    scope = new Scope(null, null);
  }

  @After
  public void tearDown() {
    scope = null;
  }

  @Test
  public void testDeclareVariable() {
    Var var = scope.declare("var", null, null, null);
    assertNotNull(var);
    assertEquals("var", var.getName());
  }

  @Test
  public void testUndeclareVariable() {
    Var var = scope.declare("var", null, null, null);
    assertNotNull(var);
    
    scope.undeclare(var);
    assertNull(scope.getVar("var"));
  }

  @Test
  public void testGetVar() {
    Var var = scope.declare("var", null, null, null);
    assertNotNull(var);
    
    Var result = scope.getVar("var");
    assertNotNull(result);
    assertEquals(var, result);
  }

  @Test
  public void testGetVarNull() {
    Var result = scope.getVar("var");
    assertNull(result);
  }

  @Test
  public void testIsDeclared() {
    scope.declare("var", null, null, null);
    
    assertTrue(scope.isDeclared("var", true));
  }

  @Test
  public void testIsDeclaredNotRecurse() {
    scope.declare("var", null, null, null);
    
    assertTrue(scope.isDeclared("var", false));
  }

  @Test
  public void testIsDeclaredNotDeclared() {
    assertFalse(scope.isDeclared("var", true));
  }

  @Test
  public void testIsDeclaredNotDeclaredNotRecurse() {
    assertFalse(scope.isDeclared("var", false));
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
  }

}
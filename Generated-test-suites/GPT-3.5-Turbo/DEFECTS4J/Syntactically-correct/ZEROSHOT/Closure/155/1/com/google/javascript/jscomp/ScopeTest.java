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
    Var var = scope.declare("name", null, null, null);
    assertNotNull(var);
    assertEquals("name", var.getName());
  }
  
  @Test
  public void testUndeclare() {
    Var var = scope.declare("name", null, null, null);
    scope.undeclare(var);
    assertNull(scope.getVar("name"));
  }
  
  @Test
  public void testGetVar() {
    Var var = scope.declare("name", null, null, null);
    Var result = scope.getVar("name");
    assertEquals(var, result);
  }
  
  @Test
  public void testIsDeclared() {
    scope.declare("name", null, null, null);
    boolean result = scope.isDeclared("name", false);
    assertTrue(result);
  }
  
  @Test
  public void testIsDeclaredRecursive() {
    scope.declare("name", null, null, null);
    boolean result = scope.isDeclared("name", true);
    assertTrue(result);
  }
  
  @Test
  public void testIsDeclaredNotRecursive() {
    scope.declare("name", null, null, null);
    boolean result = scope.isDeclared("name", false);
    assertTrue(result);
  }
  
  @Test
  public void testGetVars() {
    scope.declare("name1", null, null, null);
    scope.declare("name2", null, null, null);
    Iterator<Var> iterator = scope.getVars();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testGetVarCount() {
    scope.declare("name1", null, null, null);
    scope.declare("name2", null, null, null);
    int count = scope.getVarCount();
    assertEquals(2, count);
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
}
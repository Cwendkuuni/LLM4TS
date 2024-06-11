import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {
  
  private Scope scope;
  
  @Before
  public void setUp() {
    scope = new Scope(null, null);
  }
  
  @Test
  public void testGetDepth() {
    assertEquals(0, scope.getDepth());
  }
  
  @Test
  public void testIsBottom() {
    assertFalse(scope.isBottom());
  }
  
  @Test
  public void testGetRootNode() {
    assertNull(scope.getRootNode());
  }
  
  @Test
  public void testGetParent() {
    assertNull(scope.getParent());
  }
  
  @Test
  public void testGetTypeOfThis() {
    assertNull(scope.getTypeOfThis());
  }
  
  @Test
  public void testDeclare() {
    Scope.Var var = scope.declare("var1", null, null, null);
    assertNotNull(var);
    assertEquals("var1", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertNull(var.getJSDocInfo());
  }
  
  @Test
  public void testGetVar() {
    Scope.Var var = scope.declare("var1", null, null, null);
    Scope.Var result = scope.getVar("var1");
    assertNotNull(result);
    assertEquals(var, result);
  }
  
  @Test
  public void testGetVar_notFound() {
    Scope.Var result = scope.getVar("var1");
    assertNull(result);
  }
  
  @Test
  public void testIsDeclared() {
    scope.declare("var1", null, null, null);
    assertTrue(scope.isDeclared("var1", true));
  }
  
  @Test
  public void testIsDeclared_notFound() {
    assertFalse(scope.isDeclared("var1", true));
  }
  
  @Test
  public void testGetVars() {
    scope.declare("var1", null, null, null);
    scope.declare("var2", null, null, null);
    Iterator<Scope.Var> iterator = scope.getVars();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    Scope.Var var1 = iterator.next();
    assertNotNull(var1);
    assertEquals("var1", var1.getName());
    assertTrue(iterator.hasNext());
    Scope.Var var2 = iterator.next();
    assertNotNull(var2);
    assertEquals("var2", var2.getName());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testGetVarCount() {
    scope.declare("var1", null, null, null);
    scope.declare("var2", null, null, null);
    assertEquals(2, scope.getVarCount());
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
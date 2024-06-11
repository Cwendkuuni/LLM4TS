import org.junit.*;
import static org.junit.Assert.*;

public class ScopeTest {
  
  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("x", null, null, null);
    assertEquals("x", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertNull(var.getScope());
    assertFalse(var.isGlobals);
    assertEquals(0, var.getIndex());
    assertNull(var.getInput());
    assertFalse(var.isDefine());
    assertNull(var.getJSDocInfo());
  }
  
  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("x", null, null, null);
    scope.undeclare(var);
    assertNull(scope.getVar("x"));
  }
  
  @Test
  public void testGetVar() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, null, null);
    Var var2 = scope.declare("y", null, null, null);
    
    assertEquals(var1, scope.getVar("x"));
    assertEquals(var2, scope.getVar("y"));
    assertNull(scope.getVar("z"));
  }
  
  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("x", null, null, null);
    assertTrue(scope.isDeclared("x", false));
    assertFalse(scope.isDeclared("y", false));
    assertFalse(scope.isDeclared("z", false));
  }
  
  @Test
  public void testIsDeclaredWithRecurse() {
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);
    parentScope.declare("x", null, null, null);
    assertTrue(childScope.isDeclared("x", true));
    assertFalse(childScope.isDeclared("y", true));
    assertFalse(childScope.isDeclared("z", true));
  }
  
  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, null);
    assertTrue(scope.isGlobal());
    
    Scope childScope = new Scope(scope, null);
    assertFalse(childScope.isGlobal());
  }
  
  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isLocal());
    
    Scope childScope = new Scope(scope, null);
    assertTrue(childScope.isLocal());
  }
  
  // Add more test cases here
  
}
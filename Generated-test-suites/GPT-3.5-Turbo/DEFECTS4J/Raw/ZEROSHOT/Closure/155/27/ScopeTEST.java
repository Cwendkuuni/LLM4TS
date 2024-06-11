import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {
  
  private Scope scope;
  
  @Before
  public void setUp() {
    scope = new Scope(null, null);
  }
  
  @Test
  public void testDeclare() {
    Var var = scope.declare("x", null, null, null);
    Var expectedVar = new Scope.Var(true, "x", null, null, scope, scope.getVarCount(), null, false, null);
    assertEquals(expectedVar, var);
  }
  
  @Test
  public void testUndeclare() {
    Var var = scope.declare("x", null, null, null);
    scope.undeclare(var);
    assertNull(scope.getVar("x"));
  }
  
  @Test
  public void testGetVar() {
    Var var1 = scope.declare("x", null, null, null);
    Var var2 = scope.declare("y", null, null, null);
    
    assertEquals(var1, scope.getVar("x"));
    assertEquals(var2, scope.getVar("y"));
    assertNull(scope.getVar("z"));
    
    Scope parentScope = new Scope(scope, null);
    Var var3 = parentScope.declare("z", null, null, null);
    
    assertEquals(var3, parentScope.getVar("z"));
    assertEquals(var1, parentScope.getVar("x"));
    assertNull(parentScope.getVar("y"));
  }
  
  @Test
  public void testIsDeclared() {
    scope.declare("x", null, null, null);
    assertTrue(scope.isDeclared("x", true));
    assertFalse(scope.isDeclared("y", true));
    
    Scope parentScope = new Scope(scope, null);
    parentScope.declare("y", null, null, null);
    
    assertTrue(parentScope.isDeclared("y", true));
    assertFalse(scope.isDeclared("y", true));
  }
  
  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
    
    Scope childScope = new Scope(scope, null);
    assertFalse(childScope.isGlobal());
  }
  
  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
    
    Scope childScope = new Scope(scope, null);
    assertTrue(childScope.isLocal());
  }
}
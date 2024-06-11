import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {
  
  // Test variables
  private Scope scope;
  private Var var;
  
  @Test
  public void testDeclare() {
    // Create scope and declare variable
    scope = new Scope(null, null);
    var = scope.declare("testVar", null, null, null);
    
    // Check if variable was declared
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertNull(var.getType());
  }
  
  @Test
  public void testUndeclare() {
    // Create scope and declare variable
    scope = new Scope(null, null);
    var = scope.declare("testVar", null, null, null);
    
    // Check if variable was declared
    assertNotNull(scope.getVar("testVar"));
    
    // Undeclare variable
    scope.undeclare(var);
    
    // Check if variable was undeclared
    assertNull(scope.getVar("testVar"));
  }
  
  @Test
  public void testGetVar() {
    // Create scope and declare variable
    scope = new Scope(null, null);
    var = scope.declare("testVar", null, null, null);
    
    // Check if variable can be retrieved
    assertEquals(var, scope.getVar("testVar"));
  }
  
  @Test
  public void testIsDeclared() {
    // Create scope and declare variable
    scope = new Scope(null, null);
    var = scope.declare("testVar", null, null, null);
    
    // Check if variable is declared
    assertTrue(scope.isDeclared("testVar", true));
  }
  
  @Test
  public void testGetVars() {
    // Create scope and declare variables
    scope = new Scope(null, null);
    Var var1 = scope.declare("var1", null, null, null);
    Var var2 = scope.declare("var2", null, null, null);
    Var var3 = scope.declare("var3", null, null, null);
    
    // Check if all variables are present
    Iterator<Var> iterator = scope.getVars();
    assertNotNull(iterator.next());
    assertNotNull(iterator.next());
    assertNotNull(iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testGetVarCount() {
    // Create scope and declare variables
    scope = new Scope(null, null);
    scope.declare("var1", null, null, null);
    scope.declare("var2", null, null, null);
    scope.declare("var3", null, null, null);
    
    // Check if the correct number of variables is returned
    assertEquals(3, scope.getVarCount());
  }
  
  @Test
  public void testIsGlobal() {
    // Create global scope
    scope = new Scope(null, null);
    
    // Check if scope is global
    assertTrue(scope.isGlobal());
  }
  
  @Test
  public void testIsLocal() {
    // Create local scope
    scope = new Scope(new Scope(null, null), null);
    
    // Check if scope is local
    assertTrue(scope.isLocal());
  }
  
  @Test
  public void testGetParentScope() {
    // Create parent and child scopes
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);
    
    // Check if parent scope can be retrieved
    assertEquals(parentScope, childScope.getParentScope());
  }
}
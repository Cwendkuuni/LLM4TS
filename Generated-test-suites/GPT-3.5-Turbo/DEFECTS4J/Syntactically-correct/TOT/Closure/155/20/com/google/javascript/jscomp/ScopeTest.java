import org.junit.*;
import static org.junit.Assert.*;

public class ScopeTest {

  @Test
  public void testDeclare() {
    // Create a global scope
    Scope globalScope = new Scope(null, rootNode);
    
    // Create a new scope
    Scope scope = new Scope(globalScope, rootNode);
    
    // Declare a variable in the scope
    Var var = scope.declare("testVar", nameNode, null, input);
    
    // Verify that the variable is declared in the scope
    assertTrue(scope.isDeclared("testVar", false));
    assertFalse(scope.isDeclared("nonExistentVar", false));
  }

  @Test
  public void testUndeclare() {
    // Create a global scope
    Scope globalScope = new Scope(null, rootNode);
    
    // Create a new scope
    Scope scope = new Scope(globalScope, rootNode);
    
    // Declare a variable in the scope
    Var var = scope.declare("testVar", nameNode, null, input);
    
    // Verify that the variable is declared in the scope
    assertTrue(scope.isDeclared("testVar", false));
    
    // Undeclare the variable
    scope.undeclare(var);
    
    // Verify that the variable is no longer declared in the scope
    assertFalse(scope.isDeclared("testVar", false));
  }

  @Test
  public void testGetVar() {
    // Create a global scope
    Scope globalScope = new Scope(null, rootNode);
    
    // Create a new scope
    Scope scope = new Scope(globalScope, rootNode);
    
    // Declare a variable in the scope
    Var var = scope.declare("testVar", nameNode, null, input);
    
    // Get the variable by name
    Var retrievedVar = scope.getVar("testVar");
    
    // Verify that the retrieved variable is the same as the declared variable
    assertEquals(var, retrievedVar);
    
    // Get a non-existent variable by name
    Var nonExistentVar = scope.getVar("nonExistentVar");
    
    // Verify that the non-existent variable is null
    assertNull(nonExistentVar);
  }
  
  @Test
  public void testIsGlobal() {
    // Create a global scope
    Scope globalScope = new Scope(null, rootNode);
    
    // Create a new scope
    Scope scope = new Scope(globalScope, rootNode);
    
    // Verify that the global scope is indeed global
    assertTrue(globalScope.isGlobal());
    
    // Verify that the new scope is not global
    assertFalse(scope.isGlobal());
  }
  
  @Test
  public void testIsLocal() {
    // Create a global scope
    Scope globalScope = new Scope(null, rootNode);
    
    // Create a new scope
    Scope scope = new Scope(globalScope, rootNode);
    
    // Verify that the global scope is not local
    assertFalse(globalScope.isLocal());
    
    // Verify that the new scope is local
    assertTrue(scope.isLocal());
  }
}
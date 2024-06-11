import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {
  
  @Test
  public void testDeclareVariable() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertEquals(JSType.UNKNOWN, var.getType());
  }
  
  @Test
  public void testUndeclareVariable() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    scope.undeclare(var);
    Var undeclaredVar = scope.getVar("testVar");
    
    assertNull(undeclaredVar);
  }
  
  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    assertTrue(scope.isDeclared("testVar", false));
  }
  
  @Test
  public void testGetVar() {
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION));
    
    Var var = parentScope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    
    Var retrievedVar = childScope.getVar("testVar");
    
    assertNotNull(retrievedVar);
    assertEquals(var, retrievedVar);
  }
  
  @Test
  public void testParentScope() {
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION));
    
    assertEquals(parentScope, childScope.getParent());
  }
}
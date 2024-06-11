
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class ScopeTest {
  
  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
  }
  
  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, null);
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    assertNotNull(var);
    scope.undeclare(var);
    assertNull(scope.getVar("testVar"));
  }
  
  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isDeclared("testVar", false));
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    assertNotNull(var);
    assertTrue(scope.isDeclared("testVar", false));
  }
  
  @Test
  public void testIsDeclaredRecursively() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);
    assertFalse(scope.isDeclared("testVar", true));
    Var var = parentScope.declare("testVar", new Node(Token.NAME), null, null);
    assertNotNull(var);
    assertTrue(scope.isDeclared("testVar", true));
  }
}

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclareVar() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));
    Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertEquals(JSType.UNKNOWN, var.getType());
  }

  @Test
  public void testUndeclareVar() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));
    Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    scope.undeclare(var);
    assertNull(scope.getVar("testVar"));
  }

  // Add more test cases for other methods in the Scope class
  
}
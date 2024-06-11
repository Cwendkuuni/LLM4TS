import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclareVar() {
    Scope scope = new Scope(null, rootNode); // Create a scope
    String name = "testVar";
    Node nameNode = new Node(Token.NAME, "testVar");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    Var var = scope.declare(name, nameNode, type, input);

    assertNotNull(var);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(input, var.input);
  }

  @Test
  public void testUndeclareVar() {
    Scope scope = new Scope(null, rootNode); // Create a scope
    String name = "testVar";
    Node nameNode = new Node(Token.NAME, "testVar");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    Var var = scope.declare(name, nameNode, type, input);

    assertNotNull(var);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(input, var.input);

    scope.undeclare(var);
    assertNull(scope.getVar(name));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, rootNode); // Create a scope
    String name = "testVar";
    Node nameNode = new Node(Token.NAME, "testVar");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    Var var = scope.declare(name, nameNode, type, input);

    assertTrue(scope.isDeclared(name, false));
  }

  // Add more test cases for other methods as needed

}
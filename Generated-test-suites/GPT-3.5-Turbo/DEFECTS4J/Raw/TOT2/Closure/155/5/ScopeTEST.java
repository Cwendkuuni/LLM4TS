import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclareVar() {
    Scope scope = new Scope(null, null);
    String name = "testVar";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.STRING_TYPE;
    CompilerInput input = new CompilerInput("testFile");
    Var var = scope.declare(name, nameNode, type, input);
    
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(input, var.getInput());
    assertEquals(true, var.isTypeInferred());
    assertEquals(false, var.isDefine());
    assertEquals(type, var.getType());
  }

  @Test
  public void testUndeclareVar() {
    Scope scope = new Scope(null, null);
    String name = "testVar";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.NUMBER_TYPE;
    CompilerInput input = new CompilerInput("testFile");
    Var var = scope.declare(name, nameNode, type, input);
    
    scope.undeclare(var);
    assertNull(scope.getVar(name));
  }

  @Test
  public void testIsDeclared() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);
    String name = "testVar";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.BOOLEAN_TYPE;
    CompilerInput input = new CompilerInput("testFile");
    Var var = parentScope.declare(name, nameNode, type, input);
    
    assertTrue(scope.isDeclared(name, true));
  }

}
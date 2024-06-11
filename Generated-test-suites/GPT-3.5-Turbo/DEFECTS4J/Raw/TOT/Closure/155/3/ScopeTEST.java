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
  public void testDeclareAndGetVar_Success() {
    Node nameNode = new Node(Token.NAME, "var1");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    scope.declare("var1", nameNode, type, input);
    Var var = scope.getVar("var1");
    assertNotNull(var);
    assertEquals("var1", var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(input, var.getInput());
  }

  @Test
  public void testDeclareAndUndeclare_Success() {
    Node nameNode = new Node(Token.NAME, "var1");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    Var var = scope.declare("var1", nameNode, type, input);
    assertTrue(scope.isDeclared("var1", false));
    scope.undeclare(var);
    assertFalse(scope.isDeclared("var1", false));
  }

  @Test
  public void testIsDeclared_Recurse_Success() {
    Node nameNode = new Node(Token.NAME, "var1");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    scope.declare("var1", nameNode, type, input);
    Scope childScope = new Scope(scope, null);
    assertTrue(childScope.isDeclared("var1", true));
  }

  @Test
  public void testGetVars_ReturnsAllVars() {
    Node nameNode1 = new Node(Token.NAME, "var1");
    Node nameNode2 = new Node(Token.NAME, "var2");
    Node nameNode3 = new Node(Token.NAME, "var3");
    JSType type = new JSType();
    CompilerInput input = new CompilerInput();
    scope.declare("var1", nameNode1, type, input);
    scope.declare("var2", nameNode2, type, input);
    scope.declare("var3", nameNode3, type, input);
    assertEquals(3, scope.getVarCount());
    assertTrue(scope.getVars().hasNext());
    for (Var var : scope.getVars()) {
      assertNotNull(var);
    }
  }

  @Test
  public void testGetTypeOfThis_ReturnsCorrectType() {
    ObjectType thisType = new ObjectType();
    Scope scope = new Scope(null, thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

}
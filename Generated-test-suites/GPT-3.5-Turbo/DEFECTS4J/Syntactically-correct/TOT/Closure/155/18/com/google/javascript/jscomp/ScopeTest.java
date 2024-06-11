import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  private Scope scope;
  private Node rootNode;

  @Before
  public void setUp() {
    rootNode = new Node(Token.BLOCK);
    scope = new Scope(rootNode);
  }

  @Test
  public void testDeclare() {
    Var var = scope.declare("x", null, null, null);

    assertNotNull(var);
    assertEquals("x", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertFalse(var.isTypeInferred());
  }

  @Test
  public void testUndeclare() {
    Var var = scope.declare("x", null, null, null);
    scope.undeclare(var);

    assertNull(var.getScope());
    assertNull(scope.getVar("x"));
  }

  @Test
  public void testGetVar() {
    Var var1 = scope.declare("x", null, null, null);
    Var var2 = scope.declare("y", null, null, null);
    
    Var foundVar = scope.getVar("x");
    assertEquals(var1, foundVar);

    foundVar = scope.getVar("y");
    assertEquals(var2, foundVar);

    assertNull(scope.getVar("z"));
  }

  @Test
  public void testIsDeclared() {
    scope.declare("x", null, null, null);
    scope.declare("y", null, null, null);

    assertTrue(scope.isDeclared("x", true));
    assertTrue(scope.isDeclared("y", true));
    assertFalse(scope.isDeclared("z", true));

    assertFalse(scope.isDeclared("x", false));
    assertFalse(scope.isDeclared("y", false));
    assertFalse(scope.isDeclared("z", false));
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(rootNode, null);

    assertTrue(globalScope.isGlobal());
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(rootNode, null);

    assertFalse(globalScope.isLocal());
    assertTrue(scope.isLocal());
  }

}
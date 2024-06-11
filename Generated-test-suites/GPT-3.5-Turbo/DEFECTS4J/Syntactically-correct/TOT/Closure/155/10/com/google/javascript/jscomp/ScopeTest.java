import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
  }

  @Test
  public void testUndeclare() {
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
    assertNotNull(var);
    scope.undeclare(var);
    assertNull(scope.getVar("testVar"));
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    scope.declare("testVar", new Node(Token.NAME), null, null);
    assertTrue(scope.isDeclared("testVar", false));
  }

  @Test
  public void testIsDeclared_recurse() {
    Scope parentScope = new Scope(null, new Node(Token.SCRIPT));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION));
    parentScope.declare("testVar", new Node(Token.NAME), null, null);
    assertTrue(childScope.isDeclared("testVar", true));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    scope.declare("testVar1", new Node(Token.NAME), null, null);
    scope.declare("testVar2", new Node(Token.NAME), null, null);
    assertEquals(2, scope.getVarCount());
    assertTrue(scope.getVars().hasNext());
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(null, new Node(Token.SCRIPT));
    assertTrue(globalScope.isGlobal());

    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, new Node(Token.SCRIPT));
    assertFalse(globalScope.isLocal());

    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    assertTrue(localScope.isLocal());
  }

  @Test
  public void testGetRootNode() {
    Node rootNode = new Node(Token.SCRIPT);
    Scope scope = new Scope(null, rootNode);
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = new ObjectType();
    Scope scope = new Scope(null, new Node(Token.SCRIPT), thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }
}
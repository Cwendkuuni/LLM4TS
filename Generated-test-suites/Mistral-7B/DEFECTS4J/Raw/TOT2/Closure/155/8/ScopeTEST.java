import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.compiler.AbstractCompiler;
import com.google.javascript.rhino.compiler.CompilerInput;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Map;

/**
 * Tests for {@link Scope}.
 */
public class ScopeTest {
  private static AbstractCompiler compiler;
  private static CompilerInput input;

  static {
    compiler = new AbstractCompiler();
    input = new CompilerInput("test", null, null, null, null);
  }

  /**
   * Test for constructor with parent and rootNode.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testScopeConstructor() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    assertEquals(ObjectType.getInstance(GLOBAL_THIS), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  /**
   * Test for constructor with parent, rootNode, and thisType.
   */
  @Test
  public void testScopeConstructorWithThisType() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList());
    Scope scope = new Scope(null, rootNode, ObjectType.getInstance(IDENT("MyType")));
    assertEquals(ObjectType.getInstance(IDENT("MyType")), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  /**
   * Test for constructor with rootNode and thisType.
   */
  @Test
  public void testScopeConstructorWithRootNodeAndThisType() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList());
    Scope scope = new Scope(rootNode, ObjectType.getInstance(IDENT("MyType")));
    assertEquals(ObjectType.getInstance(IDENT("MyType")), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  /**
   * Test for constructor with parent and rootNode (global scope).
   */
  @Test
  public void testGlobalScope() {
    Scope scope = new Scope(null, null);
    assertEquals(ObjectType.getInstance(GLOBAL_THIS), scope.getTypeOfThis());
    assertTrue(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  /**
   * Test for method getDepth().
   */
  @Test
  public void testGetDepth() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    assertEquals(1, scope.getDepth());

    Scope parentScope = new Scope(scope, null);
    assertEquals(2, parentScope.getDepth());
  }

  /**
   * Test for method isBottom().
   */
  @Test
  public void testIsBottom() {
    // Test global scope
    Scope scope = new Scope(null, null);
    assertTrue(scope.isBottom());

    // Test non-bottom scope
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    scope = new Scope(null, rootNode);
    assertFalse(scope.isBottom());
  }

  /**
   * Test for method getParent().
   */
  @Test
  public void testGetParent() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, null);
    assertEquals(parentScope, childScope.getParent());
  }

  /**
   * Test for method getRootNode().
   */
  @Test
  public void testGetRootNode() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    assertEquals(rootNode, scope.getRootNode());
  }

  /**
   * Test for method getTypeOfThis().
   */
  @Test
  public void testgetTypeOfThis() {
    // Test global scope
    Scope scope = new Scope(null, null);
    assertEquals(ObjectType.getInstance(GLOBAL_THIS), scope.getTypeOfThis());

    // Test non-global scope
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    scope = new Scope(null, rootNode);
    assertNotNull(scope.getTypeOfThis());
  }

  /**
   * Test for method declare().
   */
  @Test
  public void testDeclare() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    Var yVar = scope.declare("y", ID("y"), INT, input);

    assertEquals("x", xVar.getName());
    assertEquals("y", yVar.getName());
    assertEquals(INT, xVar.getType());
    assertEquals(INT, yVar.getType());
    assertEquals(scope, xVar.getScope());
    assertEquals(scope, yVar.getScope());
  }

  /**
   * Test for method undeclare().
   */
  @Test
  public void testUndeclare() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    scope.undeclare(xVar);
    assertNull(scope.getVar("x"));
  }

  /**
   * Test for method getSlot().
   */
  @Test
  public void testGetSlot() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    assertEquals(xVar, scope.getSlot("x"));
  }

  /**
   * Test for method getOwnSlot().
   */
  @Test
  public void testGetOwnSlot() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    assertEquals(xVar, scope.getOwnSlot("x"));
  }

  /**
   * Test for method getVar().
   */
  @Test
  public void testGetVar() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    Var yVar = scope.declare("y", ID("y"), INT, input);
    assertEquals(xVar, scope.getVar("x"));
    assertEquals(yVar, scope.getVar("y"));
  }

  /**
   * Test for method isDeclared().
   */
  @Test
  public void testIsDeclared() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    Var yVar = scope.declare("y", ID("y"), INT, input);

    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("x", true));
    assertTrue(scope.isDeclared("y", false));
    assertTrue(scope.isDeclared("y", true));
    assertFalse(scope.isDeclared("z", false));
    assertFalse(scope.isDeclared("z", true));

    assertFalse(scope.getParent().isDeclared("x", false));
    assertFalse(scope.getParent().isDeclared("x", true));
    assertFalse(scope.getParent().isDeclared("y", false));
    assertFalse(scope.getParent().isDeclared("y", true));
    assertFalse(scope.getParent().isDeclared("z", false));
    assertFalse(scope.getParent().isDeclared("z", true));
  }

  /**
   * Test for method getVars().
   */
  @Test
  public void testGetVars() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    Var yVar = scope.declare("y", ID("y"), INT, input);

    ImmutableMap<String, Var> variables = ImmutableMap.of("x", xVar, "y", yVar);
    assertEquals(variables, scope.getVars());
  }

  /**
   * Test for method getVarCount().
   */
  @Test
  public void testGetVarCount() {
    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", ID("x"), INT, input);
    Var yVar = scope.declare("y", ID("y"), INT, input);

    assertEquals(2, scope.getVarCount());
  }

  /**
   * Test for method isGlobal().
   */
  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, null);
    assertTrue(scope.isGlobal());

    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    scope = new Scope(null, rootNode);
    assertFalse(scope.isGlobal());
  }

  /**
   * Test for method isLocal().
   */
  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isLocal());

    Node rootNode = newFunction(VOID, IDENT("testFunction"), newNodeList(ID("x"), ID("y")));
    scope = new Scope(null, rootNode);
    assertTrue(scope.isLocal());
  }
}
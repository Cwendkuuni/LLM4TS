import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest implements StaticScope<JSType> {
  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Node rootNode;

  @Before
  public void setup() {
    errorReporter = new ErrorReporter();
    input = new CompilerInput("test.js", new String[] {}, new String[] {});
    rootNode = NodeUtil.parseStatement(
        "function test() {"
            + "var a = 1;"
            + "var b = 2;"
            + "var c = function() {};"
            + "var d = this;"
            + "}", errorReporter);
  }

  @Test
  public void testCreateScope() {
    Scope scope = new Scope(null, rootNode);
    assertEquals(ObjectType.getObjectType(JSTypeNative.GLOBAL_THIS), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testCreateFunctionScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, rootNode);
    assertEquals(parentScope.getTypeOfThis(), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
    assertEquals(parentScope.getDepth() + 1, scope.getDepth());
  }

  @Test
  public void testCreateEmptyScope() {
    Node rootNode = NodeUtil.parseStatement("{};", errorReporter);
    Scope scope = new Scope(rootNode, ObjectType.VOID);
    assertTrue(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testDeclareVariable() {
    Scope scope = new Scope(null, rootNode);
    Var a = scope.declare("a", null, IntType.INT, input);
    Var b = scope.declare("b", null, IntType.INT, input);
    Var c = scope.declare("c", null, FunctionType.createFunctionType(null, null), input);
    Var d = scope.declare("d", null, ObjectType.getObjectType(JSTypeNative.GLOBAL_THIS), input);

    // Check basic functionality
    assertEquals("a", a.getName());
    assertEquals("b", b.getName());
    assertEquals("c", c.getName());
    assertEquals("d", d.getName());

    // Check parent scope
    assertEquals(scope, a.getScope());
    assertEquals(scope, b.getScope());
    assertEquals(scope, c.getScope());
    assertEquals(scope, d.getScope());

    // Check getSlot
    assertEquals(a, scope.getSlot("a"));
    assertEquals(b, scope.getSlot("b"));
    assertEquals(c, scope.getSlot("c"));
    assertEquals(d, scope.getSlot("d"));

    // Check getOwnSlot
    assertEquals(a, scope.getOwnSlot("a"));
    assertEquals(b, scope.getOwnSlot("b"));
    assertEquals(c, scope.getOwnSlot("c"));
    assertEquals(d, scope.getOwnSlot("d"));

    // Check getVar
    assertNull(scope.getVar("e"));
    assertEquals(a, scope.getVar("a"));
    assertEquals(b, scope.getVar("b"));
    assertEquals(c, scope.getVar("c"));
    assertEquals(d, scope.getVar("d"));

    // Check isDeclared
    assertTrue(scope.isDeclared("a", false));
    assertTrue(scope.isDeclared("a", true));
    assertTrue(scope.isDeclared("b", false));
    assertTrue(scope.isDeclared("b", true));
    assertFalse(scope.isDeclared("e", false));
    assertFalse(scope.isDeclared("e", true));

    // Check getVars
    Iterator<Var> vars = scope.getVars();
    assertTrue(vars.hasNext());
    assertEquals(a, vars.next());
    assertTrue(vars.hasNext());
    assertEquals(b, vars.next());
    assertTrue(vars.hasNext());
    assertEquals(c, vars.next());
    assertFalse(vars.hasNext());

    // Check getVarCount
    assertEquals(3, scope.getVarCount());

    // Check isLocal
    assertTrue(scope.isLocal());

    // Check isGlobal
    assertFalse(scope.isGlobal());

    // Check isBottom
    assertFalse(scope.isBottom());
  }

  @Test
  public void testUndelecareVariable() {
    Scope scope = new Scope(null, rootNode);
    Var a = scope.declare("a", null, IntType.INT, input);
    scope.undeclare(a);
    assertNull(scope.getVar("a"));
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    Scope scope = new Scope(null, rootNode);
    Var a = scope.declare("a", null, IntType.INT, input, true);
    JSDocInfo info = a.getJSDocInfo();
    assertNotNull(info);
    assertTrue(info.isNamePresent());
    assertEquals("a", info.getName());
    assertTrue(info.isTypePresent());
    assertEquals("number", info.getType());
  }

  @Test
  public void testScopeNesting() {
    Scope globalScope = new Scope(null, NodeUtil.parseStatement("{};", errorReporter));
    Node testFunctionNode = NodeUtil.parseStatement(
        "function test() {"
            + "var a = 1;"
            + "}", errorReporter);
    Scope functionScope = new Scope(globalScope, testFunctionNode);
    Var a = functionScope.getVar("a");
    assertEquals(functionScope, a.getScope());
    assertEquals(globalScope, a.getScope().getParent());
  }

  @Test
  public void testTypeInference() {
    Scope scope = new Scope(null, rootNode);
    Var a = scope.declare("a", null, IntType.INT, input);
    Var b = scope.declare("b", NodeUtil.parseStatement("var b = a;", errorReporter), a.getType(), input);
    assertEquals(a.getType(), b.getType());
  }

  @Test
  public void testTypeInferenceWithFunction() {
    Scope scope = new Scope(null, rootNode);
    Var a = scope.declare("a", null, IntType.INT, input);
    Var func = scope.declare("func", null, FunctionType.createFunctionType(new JSType[] {IntType.INT}, IntType.INT), input);
    Var c = scope.declare("c", NodeUtil.parseStatement("var c = func(a);", errorReporter), func.getType().getReturnType(), input);
    assertEquals(func.getType().getParameterTypes()[0], a.getType());
    assertEquals(func.getType().getReturnType(), c.getType());
  }

  @Test
  public void testUndeclareVariableWithNameNode() {
    Scope scope = new Scope(null, rootNode);
    Var a = scope.declare("a", null, IntType.INT, input);
    scope.undeclare(a);

    Node nameNode = NodeUtil.parseName("a");
    assertNull(scope.getVar(nameNode));
  }

  @Test
  public void testGetTypeOfThisWithFunctionScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() {}", errorReporter));
    assertEquals(parentScope.getTypeOfThis(), scope.getTypeOfThis());
  }

  @Test
  public void testGetTypeOfThisWithGlobalScope() {
    Scope scope = new Scope(null, NodeUtil.parseStatement("{};", errorReporter));
    assertEquals(ObjectType.getObjectType(JSTypeNative.GLOBAL_THIS), scope.getTypeOfThis());
  }

  @Test
  public void testIsDeclaredWithNullNameNode() {
    Scope scope = new Scope(null, rootNode);
    assertFalse(scope.isDeclared(null, false));
    assertFalse(scope.isDeclared(null, true));
  }

  @Test
  public void testIsDeclaredWithNonExistentName() {
    Scope scope = new Scope(null, rootNode);
    assertFalse(scope.isDeclared("e", false));
    assertFalse(scope.isDeclared("e", true));
  }

  @Test
  public void testIsDeclaredWithExistentNameInParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() { var a = 1; }", errorReporter));
    assertFalse(scope.isDeclared("a", false));
    assertTrue(scope.isDeclared("a", true));
  }

  @Test
  public void testGetVarWithNullName() {
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getVar(null));
  }

  @Test
  public void testGetVarWithNonExistentName() {
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getVar("e"));
  }

  @Test
  public void testGetVarWithExistentNameInParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() { var a = 1; }", errorReporter));
    Var a = parentScope.getVar("a");
    assertEquals(a, scope.getVar("a"));
  }

  @Test
  public void testGetSlotWithNullName() {
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getSlot(null));
  }

  @Test
  public void testGetSlotWithNonExistentName() {
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getSlot("e"));
  }

  @Test
  public void testGetSlotWithExistentNameInParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() { var a = 1; }", errorReporter));
    Var a = parentScope.getVar("a");
    assertEquals(a, scope.getSlot("a"));
  }

  @Test
  public void testGetOwnSlotWithNullName() {
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getOwnSlot(null));
  }

  @Test
  public void testGetOwnSlotWithNonExistentName() {
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getOwnSlot("e"));
  }

  @Test
  public void testGetOwnSlotWithExistentNameInParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() { var a = 1; }", errorReporter));
    Var a = parentScope.getVar("a");
    assertEquals(a, scope.getOwnSlot("a"));
  }

  @Test
  public void testGetVarsWithNullName() {
    Scope scope = new Scope(null, rootNode);
    Iterator<Var> vars = scope.getVars();
    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarsWithNonExistentName() {
    Scope scope = new Scope(null, rootNode);
    Iterator<Var> vars = scope.getVars();
    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarsWithExistentNameInParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() { var a = 1; }", errorReporter));
    Var a = parentScope.getVar("a");
    Iterator<Var> vars = scope.getVars();
    assertTrue(vars.hasNext());
    assertEquals(a, vars.next());
    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCountWithNullName() {
    Scope scope = new Scope(null, rootNode);
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testGetVarCountWithNonExistentName() {
    Scope scope = new Scope(null, rootNode);
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testGetVarCountWithExistentNameInParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope, NodeUtil.parseStatement("function test() { var a = 1; }", errorReporter));
    Var a = parentScope.getVar("a");
    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, rootNode);
    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, NodeUtil.parseStatement("{};", errorReporter));
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsBottom() {
    Scope scope = new Scope(null, NodeUtil.parseStatement("{};", errorReporter), ObjectType.VOID);
    assertTrue(scope.isBottom());
  }

  @Test
  public void testToString() {
    Scope scope = new Scope(null, rootNode);
    assertEquals("Scope{}", scope.toString());
  }

  @Test
  public void testGetType() {
    Scope scope = new Scope(null, rootNode);
    assertEquals(StaticScope.class, scope.getClass());
  }

  @Test
  public void testGetParentScope() {
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope.rootNode, parentScope);
    assertEquals(parentScope, scope.getParentScope());
  }
}
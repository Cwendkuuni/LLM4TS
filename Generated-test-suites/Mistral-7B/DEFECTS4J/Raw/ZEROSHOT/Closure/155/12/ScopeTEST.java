import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerInput;

import java.util.Map;

public class ScopeTest {

  @SuppressWarnings("unchecked")
  private static <T> T newScope(Node rootNode, CompilerInput input) {
    return (T) new Scope(rootNode, input) {
      @Override
      public StaticScope<JSType> getParentScope() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @org.junit.Test
  public void testNewScope() {
    Scope scope = newScope(null, null);
    assertNull(scope.getParent());
    assertFalse(scope.isBottom());
    assertEquals(0, scope.getDepth());
    assertNull(scope.getTypeOfThis());
  }

  @org.junit.Test
  public void testNewFunctionScope() {
    Node rootNode = NodeUtil.createFunctionDeclaration("foo", null, null, null, null, null, null);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    assertNotNull(scope.getParent());
    assertFalse(scope.isBottom());
    assertEquals(1, scope.getDepth());
    assertNotNull(scope.getTypeOfThis());
  }

  @org.junit.Test
  public void testNewVariables() {
    Node rootNode = NodeUtil.createBlock("var x = 1, y = 2, z;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);
    Var yVar = scope.declare("y", rootNode.getChild(1), IntegerType.INSTANCE, input);
    Var zVar = scope.declare("z", rootNode.getChild(2), null, input);

    assertEquals("x", xVar.getName());
    assertEquals("y", yVar.getName());
    assertEquals("z", zVar.getName());
    assertEquals(0, xVar.getIndex());
    assertEquals(1, yVar.getIndex());
    assertEquals(2, zVar.getIndex());
    assertEquals(scope, xVar.getScope());
    assertEquals(scope, yVar.getScope());
    assertEquals(scope, zVar.getScope());
  }

  @org.junit.Test
  public void testVarGetType() {
    Node rootNode = NodeUtil.createBlock("var x = 1, y = 2, z;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);
    Var yVar = scope.declare("y", rootNode.getChild(1), IntegerType.INSTANCE, input);
    Var zVar = scope.declare("z", rootNode.getChild(2), null, input);

    assertEquals(IntegerType.INSTANCE, xVar.getType());
    assertEquals(IntegerType.INSTANCE, yVar.getType());
    assertNull(zVar.getType());
  }

  @org.junit.Test
  public void testVarSetType() {
    Node rootNode = NodeUtil.createBlock("var x = 1, y = 2, z;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);

    xVar.setType(StringType.INSTANCE);

    assertEquals(StringType.INSTANCE, xVar.getType());
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testVarSetTypeTwice() {
    Node rootNode = NodeUtil.createBlock("var x = 1, y = 2, z;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);

    xVar.setType(IntegerType.INSTANCE);
    xVar.setType(StringType.INSTANCE);
  }

  @org.junit.Test
  public void testVarGetParentNode() {
    Node rootNode = NodeUtil.createBlock("var x = 1, y = 2, z;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);
    Var yVar = scope.declare("y", rootNode.getChild(1), IntegerType.INSTANCE, input);

    assertEquals(rootNode, xVar.getParentNode());
    assertEquals(null, yVar.getParentNode());
  }

  @org.junit.Test
  public void testVarIsBleedingFunction() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = function bar() {}; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(1), null, input);

    assertTrue(xVar.isBleedingFunction());
  }

  @org.junit.Test
  public void testVarIsGlobal() {
    Node rootNode = NodeUtil.createScript("window.foo = 1;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var fooVar = scope.declare("foo", null, NumberType.INSTANCE, input);

    assertTrue(fooVar.isGlobal());
  }

  @org.junit.Test
  public void testVarIsLocal() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(1), IntegerType.INSTANCE, input);

    assertTrue(xVar.isLocal());
  }

  @org.junit.Test
  public void testVarIsExtern() {
    Node rootNode = NodeUtil.createBlock("/** @extern */ var foo = 1;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var fooVar = scope.declare("foo", null, NumberType.INSTANCE, input);

    assertTrue(fooVar.isExtern());
  }

  @org.junit.Test
  public void testVarIsConst() {
    Node rootNode = NodeUtil.createBlock("/** @const */ var x = 1;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);

    assertTrue(xVar.isConst());
  }

  @org.junit.Test
  public void testVarIsDefine() {
    Node rootNode = NodeUtil.createBlock("/** @define */ var x = 1;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);

    assertTrue(xVar.isDefine());
  }

  @org.junit.Test
  public void testVarGetJSDocInfo() {
    Node rootNode = NodeUtil.createBlock("/** @param {number} x */ function foo(x) { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(1), IntegerType.INSTANCE, input);

    assertEquals("x", xVar.getName());
    assertEquals(NodeUtil.getInfoForNameNode(rootNode.getChild(1)), xVar.getJSDocInfo());
  }

  @org.junit.Test
  public void testVarGetInputName() {
    Node rootNode = NodeUtil.createScript("window.foo = 1;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var fooVar = scope.declare("foo", null, NumberType.INSTANCE, input);

    assertEquals("test.js", fooVar.getInputName());
  }

  @org.junit.Test
  public void testVarIsNoShadow() {
    Node rootNode = NodeUtil.createBlock("/** @noShadow */ var x = 1;");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(0), IntegerType.INSTANCE, input);

    assertTrue(xVar.isNoShadow());
  }

  @org.junit.Test
  public void testVarGetTypeFromParent() {
    Node rootNode = NodeUtil.createBlock("function foo(x) { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Scope functionScope = scope.getParent();

    assertEquals(FunctionType.INSTANCE, functionScope.getTypeOfThis());
  }

  @org.junit.Test
  public void testScopeGetVar() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(1), IntegerType.INSTANCE, input);

    assertEquals(xVar, scope.getVar("x"));
  }

  @org.junit.Test
  public void testScopeGetVarFromParent() {
    Node rootNode = NodeUtil.createBlock("function foo(x) { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope functionScope = newScope(rootNode, input);
    Scope globalScope = functionScope.getParent();

    assertEquals(globalScope.getVar("x"), functionScope.getVar("x"));
  }

  @org.junit.Test
  public void testScopeGetVarNotFound() {
    Node rootNode = NodeUtil.createBlock("function foo() { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    assertNull(scope.getVar("x"));
  }

  @org.junit.Test
  public void testScopeGetVarDeclared() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    Var xVar = scope.getVar("x");

    assertNotNull(xVar);
    assertEquals("x", xVar.getName());
    assertEquals(IntegerType.INSTANCE, xVar.getType());
  }

  @org.junit.Test
  public void testScopeGetVarNotFoundParent() {
    Node rootNode = NodeUtil.createBlock("function foo() { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Scope functionScope = scope.getParent();

    assertNull(functionScope.getVar("x"));
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testScopeGetVarNullParent() {
    Node rootNode = NodeUtil.createBlock("function foo() { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Scope functionScope = null;

    scope.getVar("x", functionScope);
  }

  @org.junit.Test
  public void testScopeGetSlot() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(1), IntegerType.INSTANCE, input);

    assertEquals(xVar, scope.getSlot("x"));
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testScopeGetSlotNull() {
    Node rootNode = NodeUtil.createBlock("function foo() { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    scope.getSlot(null);
  }

  @org.junit.Test
  public void testScopeGetOwnSlot() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);
    Var xVar = scope.declare("x", rootNode.getChild(1), IntegerType.INSTANCE, input);

    assertEquals(xVar, scope.getOwnSlot("x"));
  }

  @org.junit.Test
  public void testScopeGetOwnSlotNotFound() {
    Node rootNode = NodeUtil.createBlock("function foo() { }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    assertNull(scope.getOwnSlot("x"));
  }

  @org.junit.Test
  public void testScopeGetVars() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1, y = 2, z; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    Iterator<Var> it = scope.getVars();
    Var xVar = it.next();
    Var yVar = it.next();
    Var zVar = it.next();

    assertEquals("x", xVar.getName());
    assertEquals("y", yVar.getName());
    assertEquals("z", zVar.getName());
  }

  @org.junit.Test
  public void testScopeGetVarCount() {
    Node rootNode = NodeUtil.createBlock("function foo() { var x = 1, y = 2, z; }");
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Scope scope = newScope(rootNode, input);

    assertEquals(3, scope.getVarCount());
  }
}
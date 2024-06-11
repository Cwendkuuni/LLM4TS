import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;
import static com.google.javascript.rhino.JSDocInfo.createInfo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Node rootNode = Node.makeString("function foo() {}");
    Scope scope = new Scope(null, rootNode);
    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
    assertTrue(scope.isLocal());
    assertNull(scope.getTypeOfThis());

    Scope parentScope = new Scope(scope, Node.makeString("function bar() { var x = 1; function foo() {} }"));
    Scope globalScope = new Scope(null, Node.makeString("(function() { var x = 1; })()));"));

    assertEquals(1, scope.getDepth());
    assertFalse(scope.isBottom());
    assertTrue(scope.isLocal());
    assertNotNull(scope.getTypeOfThis());

    assertEquals(2, parentScope.getDepth());
    assertFalse(parentScope.isBottom());
    assertTrue(parentScope.isLocal());
    assertNotNull(parentScope.getTypeOfThis());

    assertEquals(0, globalScope.getDepth());
    assertTrue(globalScope.isBottom());
    assertFalse(globalScope.isLocal());
    assertNotNull(globalScope.getTypeOfThis());
  }

  @Test
  public void testVariableDeclaration() {
    Node rootNode = Node.makeString("function foo() { var x = 1; }");
    Scope scope = new Scope(null, rootNode);
    Var x = scope.declare("x", Node.makeName("x"), JSType.NUMBER, null);
    assertEquals("x", x.getName());
    assertNotNull(x.getType());
    assertNull(x.getParentNode());
    assertEquals(0, x.getIndex());
    assertFalse(x.isBleedingFunction());
    assertFalse(x.isGlobal());
    assertFalse(x.isLocal());
    assertFalse(x.isExtern());
    assertFalse(x.isConst());
    assertFalse(x.isDefine());
    assertNull(x.getInitialValue());
    assertEquals(JSType.NUMBER, x.getType());
    assertNull(x.getJSDocInfo());
    assertFalse(x.isTypeInferred());
    assertEquals("<non-file>", x.getInputName());
    assertFalse(x.isNoShadow());
  }

  @Test
  public void testVariableDeclarationWithJSDoc() {
    Node rootNode = Node.makeString("/** @define foo */ function foo() { /** @type {number} */ var x = 1; }");
    Scope scope = new Scope(null, rootNode);
    Var x = scope.declare("x", Node.makeName("x"), JSType.NUMBER, null);
    assertEquals("x", x.getName());
    assertNotNull(x.getType());
    assertNull(x.getParentNode());
    assertEquals(0, x.getIndex());
    assertFalse(x.isBleedingFunction());
    assertFalse(x.isGlobal());
    assertFalse(x.isLocal());
    assertFalse(x.isExtern());
    assertFalse(x.isConst());
    assertTrue(x.isDefine());
    assertNull(x.getInitialValue());
    assertEquals(JSType.NUMBER, x.getType());
    assertEquals(createInfo("@type", "number"), x.getJSDocInfo());
    assertFalse(x.isTypeInferred());
    assertEquals("<non-file>", x.getInputName());
    assertFalse(x.isNoShadow());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateVariableDeclaration() {
    Node rootNode = Node.makeString("function foo() { var x = 1; var x = 2; }");
    Scope scope = new Scope(null, rootNode);
    scope.declare("x", Node.makeName("x"), JSType.NUMBER, null);
    scope.declare("x", Node.makeName("x"), JSType.NUMBER, null);
  }

  @Test
  public void testVariableResolution() {
    Node rootNode = Node.makeString("function foo() { var x = 1; } function bar() { var x = 2; foo(); }");
    Scope globalScope = new Scope(null, Node.makeString("(function() { })()));"));
    Scope parentScope = new Scope(globalScope, Node.makeString("function foo() { var x = 1; }"));
    Scope scope = new Scope(parentScope, Node.makeString("function bar() { var x = 2; foo(); }"));

    Var parentX = parentScope.getVar("x");
    Var globalX = globalScope.getVar("x");
    Var childX = scope.getVar("x");

    assertNull(globalX);
    assertNotNull(parentX);
    assertNotNull(childX);
    assertEquals(parentX, childX);
  }

  @Test
  public void testVariableResolutionWithDifferentScopes() {
    Node rootNode = Node.makeString("function foo() { var x = 1; } function bar() { function baz() { var x = 2; foo(); } }");
    Scope globalScope = new Scope(null, Node.makeString("(function() { })()));"));
    Scope parentScope = new Scope(globalScope, Node.makeString("function foo() { var x = 1; }"));
    Scope childScope = new Scope(parentScope, Node.makeString("function bar() { function baz() { var x = 2; foo(); } }"));

    Var parentX = parentScope.getVar("x");
    Var childX = childScope.getVar("x");

    assertEquals(parentX, childX);
  }

  @Test
  public void testVariableResolutionWithGlobalVariable() {
    Node rootNode = Node.makeString("function foo() { var x = 1; } function bar() { var x = 2; foo(); } var x = 3;");
    Scope globalScope = new Scope(null, Node.makeString("(function() { var x = 3; })()));"));
    Scope parentScope = new Scope(globalScope, Node.makeString("function foo() { var x = 1; }"));
    Scope scope = new Scope(parentScope, Node.makeString("function bar() { var x = 2; foo(); }"));

    Var parentX = parentScope.getVar("x");
    Var globalX = globalScope.getVar("x");
    Var childX = scope.getVar("x");

    assertNull(parentX);
    assertNotNull(childX);
    assertNotNull(globalX);
    assertEquals(globalX, childX);
  }

  @Test
  public void testVariableResolutionWithShadowedGlobalVariable() {
    Node rootNode = Node.makeString("function foo() { var x = 1; } function bar() { var x = 2; var x = 3; foo(); }");
    Scope globalScope = new Scope(null, Node.makeString("(function() { var x = 3; })()));"));
    Scope parentScope = new Scope(globalScope, Node.makeString("function foo() { var x = 1; }"));
    Scope scope = new Scope(parentScope, Node.makeString("function bar() { var x = 2; var x = 3; foo(); }"));

    Var parentX = parentScope.getVar("x");
    Var globalX = globalScope.getVar("x");
    Var childX = scope.getVar("x");

    assertNull(parentX);
    assertNotNull(childX);
    assertNotNull(globalX);
    assertNotEquals(globalX, childX);
  }

  @Test
  public void testVariableResolutionWithFunctionVariable() {
    Node rootNode = Node.makeString("function foo() { function bar() { var x = 1; } } bar();");
    Scope globalScope = new Scope(null, Node.makeString("(function() { })()));"));
    Scope parentScope = new Scope(globalScope, Node.makeString("function foo() { function bar() { var x = 1; } }"));
    Scope childScope = new Scope(parentScope, Node.makeString(""));

    Var parentX = parentScope.getVar("bar");
    Var childX = childScope.getVar("x");

    assertNotNull(parentX);
    assertNotNull(childX);
    assertEquals(parentX, childX.getScope());
  }

  @Test
  public void testVariableResolutionWithBleedingFunction() {
    Node rootNode = Node.makeString("function foo() { function bar() { var x = 1; } } bar();");
    Scope globalScope = new Scope(null, Node.makeString("(function() { })()));"));
    Scope parentScope = new Scope(globalScope, Node.makeString("function foo() { function bar() { var x = 1; } }"));
    Scope childScope = new Scope(parentScope, Node.makeString(""));

    Var parentX = parentScope.getVar("bar");
    Var childX = childScope.getVar("x");

    assertNotNull(parentX);
    assertNotNull(childX);
    assertEquals(parentX, childX.getScope());
    assertTrue(childX.isBleedingFunction());
  }

  @Test
  public void testVariableResolutionWithExternVariable() {
    Node rootNode = Node.makeString("function foo() { var x = 1; }");
    CompilerInput input = new CompilerInput("test.js", "/** @extern */ var x = 1;");
    Scope scope = new Scope(null, rootNode, new ObjectType(JSType.ANY, new LinkedHashMap<String, Var>()));

    Var var = scope.getVar("x");
    assertNotNull(var);
    assertEquals(input, var.getInput());
  }

  @Test
  public void testVariableResolutionWithMissingVariable() {
    Node rootNode = Node.makeString("function foo() { var x = 1; }");
    Scope scope = new Scope(null, rootNode);

    Var var = scope.getVar("y");
    assertNull(var);
  }
}
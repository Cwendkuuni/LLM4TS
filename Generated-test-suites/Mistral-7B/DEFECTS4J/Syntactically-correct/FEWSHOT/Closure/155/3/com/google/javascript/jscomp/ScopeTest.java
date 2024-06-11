package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {
  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Scope scope;
  private Node rootNode;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    input = new CompilerInput("test", null, null);
    rootNode = new Node(Token.ROOT, null, null, null);
    scope = new Scope(null, rootNode);
  }

  @Test
  public void testNewScope() {
    assertFalse(scope.isBottom());
    assertNull(scope.getParent());
    assertNull(scope.getTypeOfThis());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testNewFunctionScope() {
    FunctionType functionType = new FunctionType(new ObjectType(JSType.VOID), null);
    scope = new Scope(null, rootNode, functionType);

    assertTrue(scope.isBottom());
    assertNull(scope.getParent());
    assertSame(functionType, scope.getTypeOfThis());
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testNewLocalScope() {
    scope = new Scope(null, rootNode);

    Var variable = scope.declare("x", null, JSType.NULL, input);

    assertFalse(scope.isBottom());
    assertNull(scope.getParent());
    assertSame(JSType.NULL, variable.getType());
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testNewNestedScope() {
    Scope parentScope = new Scope(null, rootNode);

    Var parentVariable = parentScope.declare("x", null, JSType.NUMBER, input);

    Scope childScope = new Scope(parentScope, rootNode);

    Var childVariable = childScope.declare("x", null, JSType.STRING, input);

    assertFalse(childScope.isBottom());
    assertSame(parentScope, childScope.getParent());
    assertSame(JSType.STRING, childVariable.getType());
    assertSame(parentVariable, childVariable.getScope());
    assertEquals(2, childScope.getDepth());
  }

  @Test
  public void testGetVar() {
    Var variable1 = scope.declare("x", null, JSType.NUMBER, input);
    Var variable2 = scope.declare("x", null, JSType.STRING, input);

    assertSame(variable1, scope.getVar("x"));
    assertSame(variable2, scope.getParent().getVar("x"));
  }

  @Test
  public void testGetVarNull() {
    assertNull(scope.getVar("x"));
  }

  @Test
  public void testGetVarRecursive() {
    Scope parentScope = new Scope(null, rootNode);
    Var parentVariable = parentScope.declare("x", null, JSType.NUMBER, input);

    Scope childScope = new Scope(parentScope, rootNode);
    Var childVariable = childScope.declare("x", null, JSType.STRING, input);

    Var recursiveVariable = childScope.getVar("x");

    assertSame(childVariable, recursiveVariable);
  }

  @Test
  public void testDeclareSameName() {
    String name = "x";

    scope.declare(name, null, JSType.NUMBER, input);
    Var variable1 = scope.getVar(name);

    scope.declare(name, null, JSType.STRING, input);
    Var variable2 = scope.getVar(name);

    assertSame(variable1, variable2);
  }

  @Test
  public void testDeclareSameNameRecursive() {
    String name = "x";

    Scope parentScope = new Scope(null, rootNode);
    Var parentVariable1 = parentScope.declare(name, null, JSType.NUMBER, input);

    Scope childScope = new Scope(parentScope, rootNode);
    Var childVariable1 = childScope.declare(name, null, JSType.STRING, input);

    Var recursiveVariable = childScope.declare(name, null, JSType.BOOLEAN, input);

    assertSame(childVariable1, recursiveVariable);
  }

  @Test
  public void testGetVarCount() {
    for (int i = 0; i < 10; i++) {
      scope.declare("x" + i, null, JSType.NUMBER, input);
    }

    assertEquals(10, scope.getVarCount());
  }

  @Test
  public void testGetVars() {
    for (int i = 0; i < 10; i++) {
      Var variable = scope.declare("x" + i, null, JSType.NUMBER, input);
      assertTrue(scope.getVars().hasNext());
    }

    Iterator<Var> iterator = scope.getVars();
    for (int i = 0; i < 10; i++) {
      Var variable = iterator.next();
      assertEquals("x" + i, variable.getName());
    }
  }

  @Test
  public void testIsDeclared() {
    scope.declare("x", null, JSType.NUMBER, input);

    assertTrue(scope.isDeclared("x", true));
    assertTrue(scope.isDeclared("x", false));
    assertFalse(scope.isDeclared("y", true));
    assertFalse(scope.isDeclared("y", false));
  }

  @Test
  public void testGetSlot() {
    Var variable = scope.declare("x", null, JSType.NUMBER, input);

    StaticSlot<JSType> slot = scope.getSlot("x");
    assertSame(variable, slot);
  }

  @Test
  public void testGetOwnSlot() {
    Var variable = scope.declare("x", null, JSType.NUMBER, input);

    StaticSlot<JSType> slot = scope.getOwnSlot("x");
    assertSame(variable, slot);
  }

  @Test
  public void testGetOwnSlotNull() {
    assertNull(scope.getOwnSlot("x"));
  }

  @Test
  public void testUndeclare() {
    String name = "x";
    Var variable = scope.declare(name, null, JSType.NUMBER, input);

    scope.undeclare(variable);
    assertNull(scope.getVar(name));
  }

  @Test
  public void testGetParent() {
    Scope parentScope = new Scope(null, rootNode);
    scope = new Scope(parentScope, rootNode);

    assertSame(parentScope, scope.getParent());
  }

  @Test
  public void testGetParentNull() {
    assertNull(scope.getParent());
  }

  @Test
  public void testGetRootNode() {
    assertSame(rootNode, scope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = new ObjectType(JSType.VOID);
    scope = new Scope(null, rootNode, thisType);

    assertSame(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testgetTypeOfThisNull() {
    assertNull(scope.getTypeOfThis());
  }

  @Test
  public void testGetDepth() {
    scope = new Scope(null, rootNode);
    assertEquals(0, scope.getDepth());

    Scope parentScope = new Scope(null, rootNode);
    scope = new Scope(parentScope, rootNode);

    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testIsBottom() {
    scope = new Scope(null, rootNode);
    assertFalse(scope.isBottom());

    scope = new Scope(null, rootNode, null);
    assertTrue(scope.isBottom());
  }
}
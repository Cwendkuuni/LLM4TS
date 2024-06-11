package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  private static final String TEST_CLASS_NAME = "TestClass";
  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Node rootNode;
  private Scope scope;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    input = new CompilerInput(TEST_CLASS_NAME, "test.js");
    rootNode = Node.parse("function TestClass() {}");
    scope = new Scope(null, rootNode, new AbstractCompiler(errorReporter));
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(null, rootNode, new AbstractCompiler(errorReporter));
    assertEquals(0, globalScope.getDepth());
    assertNull(globalScope.getParent());
    assertNotNull(globalScope.getTypeOfThis());
  }

  @Test
  public void testCreateScopeWithParent() {
    Scope parentScope = new Scope(null, rootNode, new AbstractCompiler(errorReporter));
    Scope childScope = new Scope(parentScope, rootNode.getLastChild());

    assertEquals(parentScope.getDepth() + 1, childScope.getDepth());
    assertEquals(parentScope, childScope.getParent());
    assertSame(parentScope.getTypeOfThis(), childScope.getTypeOfThis());
  }

  @Test
  public void testIsBottom() {
    Scope bottomScope = new Scope(rootNode, null);
    assertTrue(bottomScope.isBottom());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testGetParent() {
    assertNull(scope.getParent());
  }

  @Test
  public void testGetGlobalScope() {
    Scope globalScope = scope.getGlobalScope();
    assertEquals(globalScope, scope);
  }

  @Test
  public void testDeclareVariable() {
    String variableName = "testVariable";
    Node variableNode = Node.newVar("testVariable", null);
    JSType variableType = JSType.STRING;

    scope.declare(variableName, variableNode, variableType, input);

    Var var = scope.getVar(variableName);
    assertNotNull(var);
    assertEquals(variableName, var.getName());
    assertEquals(variableType, var.getType());
    assertEquals(input, var.getInput());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    String variableName = "testVariable";
    Node variableNode = Node.newVar("testVariable", null);
    JSType variableType = JSType.NUMBER;
    JSDocInfo jsDocInfo = NodeUtil.getInfoForNameNode(variableNode);

    scope.declare(variableName, variableNode, variableType, input, false, jsDocInfo);

    Var var = scope.getVar(variableName);
    assertNotNull(var);
    assertEquals(variableName, var.getName());
    assertEquals(variableType, var.getType());
    assertEquals(input, var.getInput());
    assertEquals(jsDocInfo, var.getJSDocInfo());
  }

  @Test
  public void testGetSlot() {
    String variableName = "testVariable";
    Node variableNode = Node.newVar("testVariable", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName, variableNode, variableType, input);

    StaticSlot<JSType> slot = scope.getSlot(variableName);
    assertEquals(variableName, slot.getName());
    assertEquals(variableType, slot.getValue());
  }

  @Test
  public void testGetOwnSlot() {
    String variableName = "testVariable";
    Node variableNode = Node.newVar("testVariable", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName, variableNode, variableType, input);

    Var var = scope.getVar(variableName);
    assertTrue(var instanceof StaticSlot);
    StaticSlot<JSType> slot = (StaticSlot<JSType>) var;
    assertEquals(variableName, slot.getName());
    assertEquals(variableType, slot.getValue());
  }

  @Test
  public void testGetVar() {
    String variableName1 = "testVariable1";
    String variableName2 = "testVariable2";
    Node variableNode1 = Node.newVar("testVariable1", null);
    Node variableNode2 = Node.newVar("testVariable2", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName1, variableNode1, variableType, input);
    scope.declare(variableName2, variableNode2, variableType, input);

    Var var1 = scope.getVar(variableName1);
    Var var2 = scope.getVar(variableName2);

    assertEquals(variableName1, var1.getName());
    assertEquals(variableName1, var1.getParentNode().getName());
    assertEquals(variableType, var1.getType());
    assertEquals(input, var1.getInput());

    assertEquals(variableName2, var2.getName());
    assertEquals(variableName2, var2.getParentNode().getName());
    assertEquals(variableType, var2.getType());
    assertEquals(input, var2.getInput());
  }

  @Test
  public void testGetVarFromParent() {
    String variableName = "testVariable";
    Node variableNode = Node.newVar("testVariable", null);
    JSType variableType = JSType.NUMBER;

    Scope parentScope = new Scope(null, rootNode, new AbstractCompiler(errorReporter));
    parentScope.declare(variableName, variableNode, variableType, input);

    Scope childScope = new Scope(parentScope, rootNode.getLastChild());

    Var var = childScope.getVar(variableName);
    assertEquals(variableName, var.getName());
    assertEquals(variableType, var.getType());
    assertEquals(parentScope, var.getScope());
  }

  @Test
  public void testIsDeclared() {
    String variableName1 = "testVariable1";
    String variableName2 = "testVariable2";
    Node variableNode1 = Node.newVar("testVariable1", null);
    Node variableNode2 = Node.newVar("testVariable2", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName1, variableNode1, variableType, input);
    scope.declare(variableName2, variableNode2, variableType, input);

    assertTrue(scope.isDeclared(variableName1, true));
    assertTrue(scope.isDeclared(variableName2, true));
  }

  @Test
  public void testIsDeclaredFalse() {
    String variableName = "testVariable";
    Node variableNode = Node.newVar("testVariable", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName, variableNode, variableType, input);

    assertFalse(scope.isDeclared(variableName, false));
  }

  @Test
  public void testGetVars() {
    String variableName1 = "testVariable1";
    String variableName2 = "testVariable2";
    Node variableNode1 = Node.newVar("testVariable1", null);
    Node variableNode2 = Node.newVar("testVariable2", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName1, variableNode1, variableType, input);
    scope.declare(variableName2, variableNode2, variableType, input);

    Iterator<Var> vars = scope.getVars();
    int counter = 0;
    while (vars.hasNext()) {
      Var var = vars.next();
      assertEquals(variableName1, var.getName());
      if (counter++ == 0) {
        assertEquals(variableNode1, var.getNameNode());
      } else {
        assertEquals(variableNode2, var.getNameNode());
      }
      assertEquals(variableType, var.getType());
      assertEquals(input, var.getInput());
    }
  }

  @Test
  public void testGetVarCount() {
    String variableName1 = "testVariable1";
    String variableName2 = "testVariable2";
    Node variableNode1 = Node.newVar("testVariable1", null);
    Node variableNode2 = Node.newVar("testVariable2", null);
    JSType variableType = JSType.NUMBER;

    scope.declare(variableName1, variableNode1, variableType, input);
    scope.declare(variableName2, variableNode2, variableType, input);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsGlobal() {
    assertFalse(scope.isGlobal());
  }
}
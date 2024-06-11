package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

  @Test
  public void testDeclareAndGetVariable() {
    Scope scope = new Scope(null, null);
    String variableName = "testVariable";
    JSType variableType = JSType.INT;

    Var declaredVariable = scope.declare(variableName, null, variableType, null);
    assertEquals(variableName, declaredVariable.getName());
    assertEquals(variableType, declaredVariable.getType());

    Var gottenVariable = scope.getVar(variableName);
    assertNotNull(gottenVariable);
    assertEquals(declaredVariable, gottenVariable);
  }

  @Test
  public void testGetVariable_Undefined() {
    Scope scope = new Scope(null, null);
    String variableName = "testVariable";

    Var gottenVariable = scope.getVar(variableName);
    assertNull(gottenVariable);
  }

  @Test
  public void testGetVariable_ParentScope() {
    Scope parentScope = new Scope(null, null);
    String variableName = "testVariable";
    Var parentVariable = parentScope.declare(variableName, null, JSType.INT, null);

    Scope childScope = new Scope(parentScope, null);
    Var childVariable = childScope.declare(variableName, null, JSType.BOOL, null);

    Var gottenVariable = childScope.getVar(variableName);
    assertEquals(childVariable, gottenVariable);
  }

  @Test
  public void testDeclareVariable_DuplicateName() {
    Scope scope = new Scope(null, null);
    String variableName = "testVariable";

    Var firstVariable = scope.declare(variableName, null, JSType.INT, null);
    assertNotNull(firstVariable);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      scope.declare(variableName, null, JSType.BOOL, null);
    });

    assertTrue(exception.getMessage().contains("Variable '" + variableName + "' already exists."));
  }

  @Test
  public void testGetVariableCount() {
    Scope scope = new Scope(null, null);

    String variable1Name = "testVariable1";
    Var variable1 = scope.declare(variable1Name, null, JSType.INT, null);

    String variable2Name = "testVariable2";
    Var variable2 = scope.declare(variable2Name, null, JSType.BOOL, null);

    int variableCount = scope.getVarCount();
    assertEquals(2, variableCount);
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, null);

    String variable1Name = "testVariable1";
    Var variable1 = scope.declare(variable1Name, null, JSType.INT, null);

    String variable2Name = "testVariable2";
    Var variable2 = scope.declare(variable2Name, null, JSType.BOOL, null);

    Iterator<Var> varsIterator = scope.getVars().iterator();
    Var firstVariable = varsIterator.next();
    Var secondVariable = varsIterator.next();

    assertEquals(variable1, firstVariable);
    assertEquals(variable2, secondVariable);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);

    String variable1Name = "testVariable1";
    Var variable1 = scope.declare(variable1Name, null, JSType.INT, null);

    String variable2Name = "testVariable2";

    assertTrue(scope.isDeclared(variable1Name, false));
    assertFalse(scope.isDeclared(variable2Name, false));

    assertTrue(scope.isDeclared(variable1Name, true));
    assertTrue(scope.isDeclared(variable2Name, true));
  }

  @Test
  public void testIsBottomScope() {
    Scope globalScope = new Scope(null, null);
    assertTrue(globalScope.isBottom());

    Scope localScope = new Scope(globalScope, null);
    assertFalse(localScope.isBottom());
  }

  @Test
  public void testGetParent() {
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);

    assertEquals(parentScope, childScope.getParent());
    assertNull(parentScope.getParent());
  }

  @Test
  public void testGetRootNode() {
    Node rootNode = new Node();
    Scope scope = new Scope(null, rootNode);

    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(null, null);
    scope.setTypeOfThis(thisType);

    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testDeclare_WithJSDocInfo() {
    Scope scope = new Scope(null, null);

    String variableName = "testVariable";
    JSType variableType = JSType.INT;
    JSDocInfo jsdocInfo = new JSDocInfo();

    Var declaredVariable = scope.declare(variableName, null, variableType, null, jsdocInfo);
    assertEquals(variableName, declaredVariable.getName());
    assertEquals(variableType, declaredVariable.getType());
    assertEquals(jsdocInfo, declaredVariable.getJSDocInfo());
  }
}
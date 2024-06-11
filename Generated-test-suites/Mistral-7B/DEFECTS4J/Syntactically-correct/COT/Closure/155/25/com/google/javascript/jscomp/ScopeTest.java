package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

@RunWith(JUnit4.class)
public class ScopeTest {

  @Test
  public void testScopeConstructor() {
    Scope scope = new Scope(null, null);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertEquals(null, scope.getTypeOfThis());
    assertTrue(scope.isBottom());

    Scope parentScope = new Scope(null, null);
    Scope scopeUnderParent = new Scope(parentScope, null);

    assertEquals(parentScope, scopeUnderParent.getParent());
    assertEquals(parentScope.getDepth() + 1, scopeUnderParent.getDepth());
    assertEquals(parentScope.getTypeOfThis(), scopeUnderParent.getTypeOfThis());
    assertFalse(scopeUnderParent.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Scope scope = new Scope(null, null);
    Var variable = scope.declare("test", null, JSType.getIntType(), null);
    assertNotNull(variable);
    assertEquals("test", variable.getName());
    assertEquals(JSType.getIntType(), variable.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullName() {
    Scope scope = new Scope(null, null);
    scope.declare(null, null, JSType.getIntType(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullType() {
    Scope scope = new Scope(null, null);
    scope.declare("test", null, null, null);
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    Scope scope = new Scope(null, null);
    Var variable = scope.declare("test", null, JSType.getIntType(), null, true);
    assertNotNull(variable.getJSDocInfo());
    assertTrue(variable.getJSDocInfo().isDefine());
  }

  @Test
  public void testGetVar() {
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);

    Var parentVariable = parentScope.declare("parent", null, JSType.getIntType(), null);
    Var childVariable = childScope.declare("child", null, JSType.getStringType(), null);

    Var parentVariableFetch = childScope.getVar("parent");
    Var childVariableFetch = childScope.getVar("child");

    assertEquals(parentVariable, parentVariableFetch);
    assertEquals(childVariable, childVariableFetch);
    assertNull(childScope.getVar("nonExistent"));
  }

  @Test
  public void testGetVarFromParentScope() {
    Scope parentScope = new Scope(null, null);
    Var parentVariable = parentScope.declare("parent", null, JSType.getIntType(), null);

    Scope childScope = new Scope(parentScope, null);
    Var childVariable = childScope.declare("child", null, JSType.getStringType(), null);

    Var parentVariableFetch = childScope.getVar("parent");
    assertEquals(parentVariable, parentVariableFetch);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    scope.declare("test", null, JSType.getIntType(), null);
    assertTrue(scope.isDeclared("test", true));
    assertFalse(scope.isDeclared("nonExistent", true));

    Scope parentScope = new Scope(null, null);
    Scope scopeUnderParent = new Scope(parentScope, null);
    scopeUnderParent.declare("test", null, JSType.getIntType(), null);
    assertTrue(scopeUnderParent.isDeclared("test", false));
    assertTrue(parentScope.isDeclared("test", true));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, null);
    scope.declare("test1", null, JSType.getIntType(), null);
    scope.declare("test2", null, JSType.getStringType(), null);

    LinkedHashMap<String, Var> expectedMap = new LinkedHashMap<>();
    expectedMap.put("test1", new Var(true, "test1", null, JSType.getIntType(), scope, 0, null, false, null));
    expectedMap.put("test2", new Var(true, "test2", null, JSType.getStringType(), scope, 1, null, false, null));

    assertEquals(expectedMap.size(), scope.getVars().size());
    for (Var variable : scope.getVars()) {
      Var expectedVariable = expectedMap.get(variable.getName());
      assertEquals(expectedVariable.getName(), variable.getName());
      assertEquals(expectedVariable.getType(), variable.getType());
    }
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, null);
    scope.declare("test1", null, JSType.getIntType(), null);
    scope.declare("test2", null, JSType.getStringType(), null);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(null, null);
    assertTrue(globalScope.isGlobal());

    Scope nonGlobalScope = new Scope(null, null);
    assertFalse(nonGlobalScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, null);
    assertFalse(globalScope.isLocal());

    Scope localScope = new Scope(null, null);
    assertTrue(localScope.isLocal());
  }
}
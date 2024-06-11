package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  @Test
  public void testScopeConstruction() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertEquals(GLOBAL_THIS, scope.getTypeOfThis());
    assertTrue(scope.isBottom());

    Scope parentScope = new Scope(scope, new Node(Token.FUNCTION, null));
    assertEquals(scope, parentScope.getParent());
    assertEquals(1, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    Var var = scope.declare("test", new Node(Token.IDENT, "test"), JSType.STRING, new CompilerInput("test.js"));
    Var declaredVar = scope.getVar("test");
    assertEquals(var, declaredVar);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithNullName() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    scope.declare(null, new Node(Token.IDENT, "test"), JSType.STRING, new CompilerInput("test.js"));
  }

  @Test
  public void testUndeclareVariable() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    Var var = scope.declare("test", new Node(Token.IDENT, "test"), JSType.STRING, new CompilerInput("test.js"));
    scope.undeclare(var);
    assertNull(scope.getVar("test"));
  }

  @Test
  public void testGetVariable() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    Var var = scope.declare("test", new Node(Token.IDENT, "test"), JSType.STRING, new CompilerInput("test.js"));
    Var declaredVar = scope.getVar("test");
    assertEquals(var, declaredVar);
  }

  @Test
  public void testGetVariableFromParentScope() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Var parentVar = parentScope.declare("test", new Node(Token.IDENT, "test"), JSType.STRING, new CompilerInput("test.js"));
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK, null));
    Var childVar = childScope.declare("test", new Node(Token.IDENT, "test"), JSType.NUMBER, new CompilerInput("test.js"));
    Var declaredVar = childScope.getVar("test");
    assertEquals(parentVar, declaredVar);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    assertTrue(scope.isDeclared("test", false));

    scope.declare("test", new Node(Token.IDENT, "test"), JSType.STRING, new CompilerInput("test.js"));
    assertTrue(scope.isDeclared("test", true));

    Scope parentScope = new Scope(scope, new Node(Token.BLOCK, null));
    parentScope.declare("test", new Node(Token.IDENT, "test"), JSType.NUMBER, new CompilerInput("test.js"));
    assertFalse(scope.isDeclared("test", false));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    scope.declare("test1", new Node(Token.IDENT, "test1"), JSType.STRING, new CompilerInput("test.js"));
    scope.declare("test2", new Node(Token.IDENT, "test2"), JSType.NUMBER, new CompilerInput("test.js"));

    LinkedHashMap<String, Var> vars = new LinkedHashMap<>();
    for (Var var : scope.getVars()) {
      vars.put(var.getName(), var);
    }

    assertEquals(2, vars.size());
    Var test1 = vars.get("test1");
    Var test2 = vars.get("test2");
    assertEquals(test1, scope.getVar("test1"));
    assertEquals(test2, scope.getVar("test2"));
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    scope.declare("test1", new Node(Token.IDENT, "test1"), JSType.STRING, new CompilerInput("test.js"));
    scope.declare("test2", new Node(Token.IDENT, "test2"), JSType.NUMBER, new CompilerInput("test.js"));

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    assertFalse(scope.isGlobal());

    Scope globalScope = new Scope(null, new Node(Token.BLOCK, null));
    assertTrue(globalScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, null));
    assertFalse(scope.isLocal());

    Scope localScope = new Scope(null, new Node(Token.BLOCK, null));
    assertTrue(localScope.isLocal());
  }
}
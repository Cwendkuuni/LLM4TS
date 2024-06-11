package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testDeclarationAndGetVar() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0));
    Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
    Var var2 = scope.declare("x", new Node(Token.NAME, "x"), JSType.NUMBER, null);
    Var var3 = scope.getVar("x");

    assertEquals(var1, var3);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0));
    Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
    Var var2 = scope.declare("x", new Node(Token.NAME, "x"), JSType.NUMBER, null);

    assertTrue(scope.isDeclared("x", true));
    assertFalse(scope.isDeclared("y", true));
    assertFalse(scope.isDeclared("x", false));
  }

  @Test
  public void testGetVarRecursive() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 0));
    Var parentVar = parentScope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);

    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 0));
    Var childVar = childScope.declare("x", new Node(Token.NAME, "x"), JSType.NUMBER, null);

    Var getVar = childScope.getVar("x");

    assertEquals(childVar, getVar);
    assertEquals(parentVar.getParentScope(), childVar.getParentScope());
    assertEquals(childScope, parentVar.getParentScope());
  }

  @Test
  public void testUndelecare() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0));
    Var var = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
    scope.undeclare(var);

    assertNull(scope.getVar("x"));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0));
    Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
    Var var2 = scope.declare("y", new Node(Token.NAME, "y"), JSType.NUMBER, null);

    Iterator<Var> varIterator = scope.getVars();

    Var var1FromIterator = varIterator.next();
    Var var2FromIterator = varIterator.next();

    assertEquals(var1, var1FromIterator);
    assertEquals(var2, var2FromIterator);
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0));
    Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
    Var var2 = scope.declare("y", new Node(Token.NAME, "y"), JSType.NUMBER, null);

    int varCount = scope.getVarCount();

    assertEquals(2, varCount);
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, new Node(Token.BLOCK, 0));
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION, 0));

    assertTrue(globalScope.isGlobal());
    assertTrue(localScope.isLocal());
  }
}
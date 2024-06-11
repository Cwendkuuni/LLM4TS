package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScopeTest {

  @org.junit.Test
  public void testScopeDeclaration() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));

    Var var1 = scope.declare("var1", new Node(Token.IDENT, "var1"), null, null);
    Var var2 = scope.declare("var2", new Node(Token.IDENT, "var2"), null, null);
    Var var3 = scope.declare("var3", new Node(Token.IDENT, "var3"), null, null);

    assertEquals(3, scope.getVarCount());
    assertEquals("var1", var1.getName());
    assertEquals("var2", var2.getName());
    assertEquals("var3", var3.getName());

    // Check that variables are unique
    assertNull(scope.getVar("var1"));
    assertNull(scope.getVar("var2"));
    assertNull(scope.getVar("var3"));
    assertNull(scope.getVar("nonExistingVar"));

    // Check that variables can be accessed by name
    Var accessedVar1 = scope.getVar("var1");
    Var accessedVar2 = scope.getVar("var2");
    Var accessedVar3 = scope.getVar("var3");

    assertEquals(var1, accessedVar1);
    assertEquals(var2, accessedVar2);
    assertEquals(var3, accessedVar3);
  }

  @org.junit.Test
  public void testScopeNestedDeclarations() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 0, null));
    Scope childScope = new Scope(parentScope, new Node(Token.IF, 0, null));

    Var parentVar1 = parentScope.declare("parentVar1", new Node(Token.IDENT, "parentVar1"), null, null);
    Var parentVar2 = parentScope.declare("parentVar2", new Node(Token.IDENT, "parentVar2"), null, null);

    Var childVar1 = childScope.declare("childVar1", new Node(Token.IDENT, "childVar1"), null, null);
    Var childVar2 = childScope.declare("childVar2", new Node(Token.IDENT, "childVar2"), null, null);

    assertEquals(4, parentScope.getVarCount());
    assertEquals(2, childScope.getVarCount());

    // Check that parent variables are accessible in child scope
    Var accessedParentVar1 = childScope.getVar("parentVar1");
    Var accessedParentVar2 = childScope.getVar("parentVar2");

    assertEquals(parentVar1, accessedParentVar1);
    assertEquals(parentVar2, accessedParentVar2);

    // Check that child variables are not accessible in parent scope
    assertNull(parentScope.getVar("childVar1"));
    assertNull(parentScope.getVar("childVar2"));

    // Check that child variables are accessible in child scope
    Var accessedChildVar1 = childScope.getVar("childVar1");
    Var accessedChildVar2 = childScope.getVar("childVar2");

    assertEquals(childVar1, accessedChildVar1);
    assertEquals(childVar2, accessedChildVar2);

    // Check that variables have unique names
    assertNull(childScope.getVar("parentVar1"));
    assertNull(childScope.getVar("parentVar2"));
    assertNull(parentScope.getVar("childVar1"));
    assertNull(parentScope.getVar("childVar2"));

    // Check that variables can be accessed by name in parent scope after child variable declaration
    Var accessedParentVar1ChildScope = parentScope.getVar("var1");
    assertEquals(parentVar1, accessedParentVar1ChildScope);
  }

  @org.junit.Test
  public void testScopeVariableType() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));

    Var var1 = scope.declare("var1", new Node(Token.IDENT, "var1"), int.class.getName(), null);
    Var var2 = scope.declare("var2", new Node(Token.IDENT, "var2"), double.class.getName(), null);

    Set<String> expectedTypes = new HashSet<>(Arrays.asList(int.class.getName(), double.class.getName()));

    for (Var variable : scope.getVars()) {
      assertEquals(expectedTypes.contains(variable.getType().getClass().getName()), variable.getType().equals(variable.getType()));
      expectedTypes.remove(variable.getType().getClass().getName());
    }

    assertTrue(expectedTypes.isEmpty());
  }

  @org.junit.Test
  public void testScopeUndefinedVariable() {
    Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));

    assertNull(scope.getVar("undefinedVar"));
  }

  @org.junit.Test
  public void testScopeScopeHierarchy() {
    Scope globalScope = new Scope(null, new Node(Token.SCRIPT, 0, null));
    Scope functionScope = new Scope(globalScope, new Node(Token.FUNCTION, 0, null));

    Var globalVar1 = globalScope.declare("globalVar1", new Node(Token.IDENT, "globalVar1"), null, null);
    Var functionVar1 = functionScope.declare("functionVar1", new Node(Token.IDENT, "functionVar1"), null, null);

    // Check that global variables are accessible in function scope
    Var accessedGlobalVar1 = functionScope.getVar("globalVar1");
    assertEquals(globalVar1, accessedGlobalVar1);

    // Check that function variables are not accessible in global scope
    assertNull(globalScope.getVar("functionVar1"));

    // Check that function variables are accessible in function scope
    Var accessedFunctionVar1 = functionScope.getVar("functionVar1");
    assertEquals(functionVar1, accessedFunctionVar1);

    // Check that global variables have unique names
    assertNull(functionScope.getVar("globalVar1"));

    // Check that function variables have unique names
    assertNull(globalScope.getVar("functionVar1"));
  }
}
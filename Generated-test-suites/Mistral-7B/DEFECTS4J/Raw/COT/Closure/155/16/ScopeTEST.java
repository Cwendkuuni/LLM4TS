package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.JSType;

import java.util.Map;

import org.junit.Test;

/**
 * Test cases for Scope class.
 */
public class ScopeTest {

  @Test
  public void testCreateScope() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));
    assertEquals(0, scope.getDepth());
    assertEquals(true, scope.isBottom());
  }

  @Test
  public void testCreateFunctionScope() {
    Node rootNode = new Node(Token.FUNCTION);
    Scope scope = new Scope(null, rootNode);
    assertEquals(1, scope.getDepth());
    assertEquals(false, scope.isBottom());
  }

  @Test
  public void testCreateGlobalScope() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    assertEquals(0, scope.getDepth());
    assertEquals(false, scope.isBottom());
  }

  @Test
  public void testGetParentScope() {
    Node rootNode = new Node(Token.FUNCTION);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));
    assertEquals(parentScope, childScope.getParentScope());
  }

  @Test
  public void testGetTypeOfThis() {
    Node rootNode = new Node(Token.FUNCTION);
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(null, rootNode, thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testIsBottom() {
    Node rootNode = new Node(Token.BLOCK);
    Scope globalScope = new Scope(rootNode, null);
    Scope bottomScope = new Scope(rootNode, null, null);

    assertFalse(globalScope.isBottom());
    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);

    String name = "testVar";
    Var var = scope.declare(name, new Node(Token.NAME, name), JSType.INT, null);
    assertEquals(name, var.getName());
    assertEquals(scope, var.getScope());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullName() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    scope.declare(null, new Node(Token.NAME, "testVar"), JSType.INT, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullNameNode() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    scope.declare("testVar", null, JSType.INT, null);
  }

  @Test
  public void testDeclareVariableExistingName() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    String name = "testVar";
    scope.declare(name, new Node(Token.NAME, name), JSType.INT, null);
    try {
      scope.declare(name, new Node(Token.NAME, name), JSType.STRING, null);
      fail("Expected exception");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testUndelecareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    String name = "testVar";
    Var var = scope.declare(name, new Node(Token.NAME, name), JSType.INT, null);
    scope.undeclare(var);
    assertNull(scope.getVar(name));
  }

  @Test
  public void testGetSlot() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    String name = "testVar";
    Var var = scope.declare(name, new Node(Token.NAME, name), JSType.INT, null);
    assertEquals(var, scope.getSlot(name));
  }

  @Test
  public void testGetOwnSlot() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    String name = "testVar";
    Var var = scope.declare(name, new Node(Token.NAME, name), JSType.INT, null);
    assertEquals(var, scope.getOwnSlot(name));
  }

  @Test
  public void testGetVar() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(rootNode, null);
    String parentName = "parentVar";
    Var parentVar = parentScope.declare(parentName, new Node(Token.NAME, parentName), JSType.INT, null);
    String childName = "childVar";
    Var childVar = parentScope.declare(childName, new Node(Token.NAME, childName), JSType.INT, null);

    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));
    Var childVarInChildScope = childScope.getVar(childName);
    assertEquals(childVar, childVarInChildScope);
  }

  @Test
  public void testGetVarNull() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);
    assertNull(scope.getVar("nonExistentVar"));
  }

  @Test
  public void testIsDeclared() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);

    String name1 = "testVar1";
    Var var1 = scope.declare(name1, new Node(Token.NAME, name1), JSType.INT, null);
    assertTrue(scope.isDeclared(name1, false));

    String name2 = "testVar2";
    Var var2 = scope.declare(name2, new Node(Token.NAME, name2), JSType.INT, null);
    assertTrue(scope.isDeclared(name2, false));

    String name3 = "testVar3";
    assertFalse(scope.isDeclared(name3, false));

    String name4 = "testVar1";
    assertTrue(scope.isDeclared(name4, true));

    Scope parentScope = new Scope(scope, new Node(Token.BLOCK));
    Var parentVar = parentScope.declare(name1, new Node(Token.NAME, name1), JSType.INT, null);
    assertTrue(parentScope.isDeclared(name1, false));
  }

  @Test
  public void testGetVars() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);

    String name1 = "testVar1";
    Var var1 = scope.declare(name1, new Node(Token.NAME, name1), JSType.INT, null);
    String name2 = "testVar2";
    Var var2 = scope.declare(name2, new Node(Token.NAME, name2), JSType.INT, null);

    Iterator<Var> vars = scope.getVars();
    assertEquals(2, vars.size());
    assertEquals(var1, vars.next());
    assertEquals(var2, vars.next());
  }

  @Test
  public void testGetVarCount() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(rootNode, null);

    String name1 = "testVar1";
    Var var1 = scope.declare(name1, new Node(Token.NAME, name1), JSType.INT, null);
    String name2 = "testVar2";
    Var var2 = scope.declare(name2, new Node(Token.NAME, name2), JSType.INT, null);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope globalScope = new Scope(rootNode, null);
    assertFalse(globalScope.isGlobal());

    Scope parentScope = new Scope(null, rootNode);
    assertTrue(parentScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope globalScope = new Scope(rootNode, null);
    assertFalse(globalScope.isLocal());

    Scope parentScope = new Scope(null, rootNode);
    assertFalse(parentScope.isLocal());

    Scope localScope = new Scope(parentScope, new Node(Token.BLOCK));
    assertTrue(localScope.isLocal());
  }
}
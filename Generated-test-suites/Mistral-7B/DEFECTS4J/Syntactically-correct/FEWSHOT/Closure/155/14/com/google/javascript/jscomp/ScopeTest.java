package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {
  @Test
  public void testNewScope() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope scope = new Scope(null, rootNode);
    ObjectType thisType = new ObjectType();
    Scope scope1 = new Scope(scope, new Node(Token.FUNCTION, 1));
    ObjectType thisType1 = new ObjectType();
    scope1.setTypeOfThis(thisType1);
    Scope scope2 = new Scope(scope1, new Node(Token.BLOCK, 2));
    ObjectType thisType2 = new ObjectType();
    scope2.setTypeOfThis(thisType2);

    assertEquals(scope1, scope1.getParent());
    assertEquals(scope, scope1.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertEquals(rootNode, scope1.getRootNode());
    assertEquals(thisType, scope.getTypeOfThis());
    assertEquals(thisType1, scope1.getTypeOfThis());
    assertEquals(null, scope.getParent());
  }

  @Test
  public void testGetParentScope() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope scope = new Scope(null, rootNode);
    Scope scope1 = new Scope(scope, new Node(Token.FUNCTION, 1));
    assertEquals(scope, scope1.getParentScope());
  }

  @Test
  public void testIsBottom() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope scope = new Scope(null, rootNode);
    Scope scope1 = new Scope(scope, new Node(Token.FUNCTION, 1));
    Node rootNode1 = new Node(Token.SCRIPT, 1);
    Scope scope2 = new Scope(null, rootNode1);
    ObjectType thisType = new ObjectType();
    scope = new Scope(scope, new Node(Token.BLOCK, 2), thisType);
    assertTrue(scope1.isBottom());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testGetDepth() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testGetRootNode() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope scope = new Scope(null, rootNode);
    Node functionNode = new Node(Token.FUNCTION, 1);
    Scope scope1 = new Scope(scope, functionNode);
    assertEquals(functionNode, scope1.getRootNode());
  }

  @Test
  public void testGetParent() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    assertEquals(rootScope, scope.getParent());
  }

  @Test
  public void testGetGlobalScope() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    Scope globalScope = rootScope;
    assertEquals(globalScope, scope.getGlobalScope());
  }

  @Test
  public void testDeclare() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    CompilerInput input = new CompilerInput("test.js");
    String name = "x";
    Var var = scope.declare(name, null, new IntType(), input);
    assertEquals(name, var.getName());
    assertEquals(scope, var.getScope());
    assertEquals(input, var.getInput());
  }

  @Test
  public void testUndeclare() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    Var var = new Var("x", null, null, null, scope, 0, null, false, null);
    scope.undeclare(var);
    assertNull(scope.getVar("x"));
  }

  @Test
  public void testIsDeclared() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    String name = "x";
    Var var1 = scope.declare(name, null, new IntType(), new CompilerInput("test.js"));
    Var var2 = scope.declare(name, null, new IntType(), new CompilerInput("test.js"));
    assertTrue(scope.isDeclared(name, true));
    assertTrue(scope.isDeclared(name, false));
    assertFalse(rootScope.isDeclared(name, true));
    assertFalse(rootScope.isDeclared(name, false));
  }

  @Test
  public void testGetVars() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    Var var1 = scope.declare("x", null, new IntType(), new CompilerInput("test.js"));
    Var var2 = scope.declare("y", null, new IntType(), new CompilerInput("test.js"));
    Iterator<Var> it = scope.getVars();
    assertTrue(it.hasNext());
    Var var = it.next();
    assertEquals(var1, var);
    assertTrue(it.hasNext());
    var = it.next();
    assertEquals(var2, var);
    assertFalse(it.hasNext());
  }

  @Test
  public void testGetVarCount() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    Var var1 = scope.declare("x", null, new IntType(), new CompilerInput("test.js"));
    Var var2 = scope.declare("y", null, new IntType(), new CompilerInput("test.js"));
    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsGlobal() {
    Node rootNode = new Node(Token.SCRIPT, 0);
    Scope rootScope = new Scope(null, rootNode);
    Scope scope = new Scope(rootScope, new Node(Token.FUNCTION, 1));
    assertFalse(scope.isGlobal());
  }
}
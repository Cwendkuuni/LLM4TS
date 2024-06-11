package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScopeTest {
  @Test
  public void testCreateScope() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    assertEquals(0, scope.getDepth());
    assertNull(scope.getParent());
    assertNull(scope.getTypeOfThis());
  }

  @Test
  public void testCreateFunctionScope() {
    Node rootNode = new Node(Token.FUNCTION);
    Node thisNode = new Node(Token.IDENT, "this");
    Scope scope = new Scope(null, rootNode, thisNode);
    assertEquals(1, scope.getDepth());
    assertNotNull(scope.getParent());
    assertNotNull(scope.getTypeOfThis());
  }

  @Test
  public void testCreateEmptyScope() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(null, rootNode, thisType);
    assertEquals(0, scope.getDepth());
    assertNull(scope.getParent());
    assertNotNull(scope.getTypeOfThis());
  }

  @Test
  public void testGetParentScope() {
    Node rootNode = new Node(Token.BLOCK);
    Node thisNode = new Node(Token.IDENT, "this");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisNode);
    assertEquals(parentScope, childScope.getParentScope());
  }

  @Test
  public void testDeclareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("String");
    CompilerInput input = new CompilerInput("test.js");
    Var var = childScope.declare(name, nameNode, type, input);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(childScope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertEquals(true, var.isDefine());
    assertEquals(true, var.isTypeInferred());
  }

  @Test
  public void testDeclareVariableWithInfo() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("String");
    CompilerInput input = new CompilerInput("test.js");
    JSDocInfo info = new JSDocInfo();
    info.setName(name);
    info.setDefine(true);
    Var var = childScope.declare(name, nameNode, type, input, info);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(childScope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertEquals(true, var.isDefine());
    assertEquals(true, var.isTypeInferred());
    assertEquals(info, var.getJSDocInfo());
  }

  @Test
  public void testDeclareVariableWithSameNameInParentScope() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("String");
    CompilerInput input = new CompilerInput("test.js");
    childScope.declare(name, nameNode, type, input);
    assertNull(parentScope.getVar(name));
  }

  @Test
  public void testDeclareVariableWithSameNameInAncestorScope() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope grandParentScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(grandParentScope, new Node(Token.FUNCTION), thisType);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("String");
    CompilerInput input = new CompilerInput("test.js");
    childScope.declare(name, nameNode, type, input);
    Var parentVar = parentScope.getVar(name);
    assertNull(parentVar);
    Var grandParentVar = grandParentScope.getVar(name);
    assertNull(grandParentVar);
  }

  @Test
  public void testUndeclareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("String");
    CompilerInput input = new CompilerInput("test.js");
    Var var = childScope.declare(name, nameNode, type, input);
    childScope.undeclare(var);
    assertNull(childScope.getVar(name));
  }

  @Test
  public void testIsDeclared() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("String");
    CompilerInput input = new CompilerInput("test.js");
    childScope.declare(name, nameNode, type, input);
    assertTrue(childScope.isDeclared(name, true));
    assertFalse(parentScope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name1 = "foo1";
    Node nameNode1 = new Node(Token.IDENT, name1);
    JSType type1 = new ObjectType("String");
    CompilerInput input1 = new CompilerInput("test1.js");
    Var var1 = childScope.declare(name1, nameNode1, type1, input1);
    String name2 = "foo2";
    Node nameNode2 = new Node(Token.IDENT, name2);
    JSType type2 = new ObjectType("Number");
    CompilerInput input2 = new CompilerInput("test2.js");
    Var var2 = childScope.declare(name2, nameNode2, type2, input2);
    List<Var> vars = new ArrayList<>(childScope.getVars());
    assertEquals(2, vars.size());
    assertEquals(var1, vars.get(0));
    assertEquals(var2, vars.get(1));
  }

  @Test
  public void testGetVarCount() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), thisType);
    String name1 = "foo1";
    Node nameNode1 = new Node(Token.IDENT, name1);
    JSType type1 = new ObjectType("String");
    CompilerInput input1 = new CompilerInput("test1.js");
    Var var1 = childScope.declare(name1, nameNode1, type1, input1);
    String name2 = "foo2";
    Node nameNode2 = new Node(Token.IDENT, name2);
    JSType type2 = new ObjectType("Number");
    CompilerInput input2 = new CompilerInput("test2.js");
    Var var2 = childScope.declare(name2, nameNode2, type2, input2);
    assertEquals(2, childScope.getVarCount());
  }

  @Test
  public void testGetGlobalScope() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope globalScope = new Scope(null, rootNode);
    Scope childScope = new Scope(globalScope, new Node(Token.FUNCTION), thisType);
    assertEquals(globalScope, childScope.getGlobalScope());
  }

  @Test
  public void testIsGlobalScope() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope globalScope = new Scope(null, rootNode);
    Scope childScope = new Scope(globalScope, new Node(Token.FUNCTION), thisType);
    assertTrue(globalScope.isGlobal());
    assertFalse(childScope.isGlobal());
  }

  @Test
  public void testIsLocalScope() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope globalScope = new Scope(null, rootNode);
    Scope childScope = new Scope(globalScope, new Node(Token.FUNCTION), thisType);
    assertFalse(globalScope.isLocal());
    assertTrue(childScope.isLocal());
  }
}
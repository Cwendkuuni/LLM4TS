package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Node rootNode;
  private Scope scope;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {};
    input = new CompilerInput("test.js") {};
    rootNode = Node.newFunctionNode(null, Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, null, null);
    scope = new Scope(null, rootNode);
  }

  @Test
  public void testCreateScope() {
    assertNotNull(scope);
    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
    assertNull(scope.getTypeOfThis());
  }

  @Test
  public void testCreateFunctionScope() {
    Node functionNode = Node.newFunctionNode("test", Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, rootNode, null, null, null);
    Scope functionScope = new Scope(scope, functionNode);

    assertEquals(1, functionScope.getDepth());
    assertFalse(functionScope.isBottom());
    assertNotNull(functionScope.getTypeOfThis());
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(rootNode, errorReporter, input);

    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
    assertNotNull(globalScope.getTypeOfThis());
  }

  @Test
  public void testCreateEmptyScope() {
    Scope emptyScope = new Scope(rootNode, new ObjectType(null, null));

    assertEquals(0, emptyScope.getDepth());
    assertTrue(emptyScope.isBottom());
    assertNull(emptyScope.getTypeOfThis());
  }

  @Test
  public void testDeclareVariable() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    JSType type = new ObjectType(name, null);
    Var var = scope.declare(name, nameNode, type, input);

    assertTrue(scope.isDeclared(name, false));
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertNull(var.getInputName());
    assertFalse(var.isDefine());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    JSType type = new ObjectType(name, null);
    JSDocInfo info = new JSDocInfo("@param {number} foo");

    Var var = scope.declare(name, nameNode, type, input, info);

    assertTrue(scope.isDeclared(name, false));
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(info, var.getJSDocInfo());
    assertTrue(var.isDefine());
  }

  @Test
  public void testDeclareVariableWithSameName() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    JSType type = new ObjectType(name, null);
    scope.declare(name, nameNode, type, input);

    assertTrue(scope.isDeclared(name, false));
    assertNull(scope.getVar(name));
  }

  @Test
  public void testUndelecareVariable() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    Var var = scope.declare(name, nameNode, new ObjectType(name, null), input);
    scope.undeclare(var);

    assertFalse(scope.isDeclared(name, false));
  }

  @Test
  public void testGetSlot() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    Var var = scope.declare(name, nameNode, new ObjectType(name, null), input);

    StaticSlot<JSType> slot = scope.getSlot(name);

    assertEquals(var, slot);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    Var var = scope.declare(name, nameNode, new ObjectType(name, null), input);

    StaticSlot<JSType> slot = scope.getOwnSlot(name);

    assertEquals(var, slot);
  }

  @Test
  public void testGetVar() {
    String name = "foo";
    Node nameNode = Node.newNameNode(name);
    Var var = scope.declare(name, nameNode, new ObjectType(name, null), input);

    Var result = scope.getVar(name);

    assertEquals(var, result);
  }

  @Test
  public void testIsDeclared() {
    String name1 = "foo";
    Node nameNode1 = Node.newNameNode(name1);
    Var var1 = scope.declare(name1, nameNode1, new ObjectType(name1, null), input);

    String name2 = "bar";
    Node nameNode2 = Node.newNameNode(name2);

    assertFalse(scope.isDeclared(name2, false));
    assertTrue(scope.isDeclared(name1, false));

    Scope parentScope = new Scope(scope, rootNode);
    scope.declare(name2, nameNode2, new ObjectType(name2, null), input);

    assertTrue(scope.isDeclared(name2, true));
  }

  @Test
  public void testGetVars() {
    String name1 = "foo";
    Node nameNode1 = Node.newNameNode(name1);
    Var var1 = scope.declare(name1, nameNode1, new ObjectType(name1, null), input);

    String name2 = "bar";
    Node nameNode2 = Node.newNameNode(name2);
    Var var2 = scope.declare(name2, nameNode2, new ObjectType(name2, null), input);

    Iterator<Var> iterator = scope.getVars();

    Var result1 = iterator.next();
    Var result2 = iterator.next();

    assertEquals(var1, result1);
    assertEquals(var2, result2);
  }

  @Test
  public void testGetVarCount() {
    String name1 = "foo";
    Node nameNode1 = Node.newNameNode(name1);
    Var var1 = scope.declare(name1, nameNode1, new ObjectType(name1, null), input);

    String name2 = "bar";
    Node nameNode2 = Node.newNameNode(name2);
    Var var2 = scope.declare(name2, nameNode2, new ObjectType(name2, null), input);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
  }
}
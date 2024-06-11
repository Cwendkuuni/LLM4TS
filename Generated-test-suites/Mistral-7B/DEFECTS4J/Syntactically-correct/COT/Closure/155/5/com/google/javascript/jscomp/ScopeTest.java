package com.google.javascript.jscomp; // make sure the package name is correct

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  @org.junit.Test
  public void testScopeConstructorWithParentAndRootNode() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertNotNull(scope.thisType);
    assertFalse(scope.isBottom());
  }

  @org.junit.Test
  public void testScopeConstructorWithParentAndRootNodeWithFunctionType() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(null, rootNode, thisType);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertEquals(thisType, scope.thisType);
    assertFalse(scope.isBottom());
  }

  @org.junit.Test
  public void testScopeConstructorWithParentAndRootNodeWithNullThisType() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode, null);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertNull(scope.thisType);
    assertTrue(scope.isBottom());
  }

  @org.junit.Test
  public void testScopeConstructorWithParentAndRootNodeAndIsBottom() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode, null, true);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertNull(scope.thisType);
    assertTrue(scope.isBottom());
  }

  @org.junit.Test
  public void testScopeConstructorWithParentAndRootNodeWithParentScope() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope parentScope = new Scope(null, Node.newBlockNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY));
    Scope scope = new Scope(parentScope, rootNode);
    assertEquals(parentScope, scope.getParent());
    assertEquals(parentScope.getDepth() + 1, scope.getDepth());
    assertNotNull(scope.thisType);
    assertFalse(scope.isBottom());
  }

  @org.junit.Test
  public void testDeclareVariable() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    String variableName = "testVariable";
    Var variable = scope.declare(variableName, Node.newNameNode(variableName), new FunctionType(null, null), null);
    assertNotNull(variable);
    assertEquals(variableName, variable.getName());
    assertEquals(scope, variable.getScope());
  }

  @org.junit.Test
  public void testDeclareVariableWithType() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    String variableName = "testVariable";
    Var variable = scope.declare(variableName, Node.newNameNode(variableName), new FunctionType(null, new JSType[] { new IntType() }), null);
    assertNotNull(variable);
    assertEquals(variableName, variable.getName());
    assertEquals(scope, variable.getScope());
    assertNotNull(variable.getType());
    assertTrue(variable.getType() instanceof IntType);
  }

  @org.junit.Test
  public void testDeclareVariableWithSameNameRecursively() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope outerScope = new Scope(null, rootNode);
    String variableName = "testVariable";
    outerScope.declare(variableName, Node.newNameNode(variableName), new FunctionType(null, null), null);
    Scope innerScope = new Scope(outerScope, Node.newBlockNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY));
    Var innerVariable = innerScope.declare(variableName, Node.newNameNode(variableName), new FunctionType(null, null), null);
    assertNotNull(innerVariable);
    assertEquals(variableName, innerVariable.getName());
    assertEquals(innerScope, innerVariable.getScope());
    assertEquals(outerScope, innerVariable.getScope().getParent());
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testDeclareVariableWithSameNameTwiceInSameScope() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    String variableName = "testVariable";
    scope.declare(variableName, Node.newNameNode(variableName), new FunctionType(null, null), null);
    scope.declare(variableName, Node.newNameNode(variableName), new FunctionType(null, null), null);
  }

  @org.junit.Test
  public void testGetVar() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    Var testVar = scope.declare("test", Node.newNameNode("test"), new FunctionType(null, null), null);
    Var testVarResult = scope.getVar("test");
    assertEquals(testVar, testVarResult);
  }

  @org.junit.Test
  public void testGetVarNull() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    Var testVar = scope.declare("test", Node.newNameNode("test"), new FunctionType(null, null), null);
    Var nonExistentVar = scope.getVar("nonExistent");
    assertNull(nonExistentVar);
  }

  @org.junit.Test
  public void testGetVarInParentScope() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("outer"), Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0), null, null, 0);
    Scope outerScope = new Scope(null, rootNode);
    Var outerTestVar = outerScope.declare("test", Node.newNameNode("test"), new FunctionType(null, null), null);

    Node innerRootNode = Node.newFunctionNode(Node.NEW_VAR_NODE, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope innerScope = new Scope(outerScope, innerRootNode);
    Var innerTestVar = innerScope.declare("test", Node.newNameNode("test"), new FunctionType(null, null), null);

    Var innerTestVarResult = innerScope.getVar("test");
    assertEquals(innerTestVar, innerTestVarResult);

    Var outerTestVarResult = innerScope.getVar("test");
    assertEquals(outerTestVar, outerTestVarResult);
  }

  @org.junit.Test
  public void testIsDeclared() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    scope.declare("test", Node.newNameNode("test"), new FunctionType(null, null), null);
    assertTrue(scope.isDeclared("test", true));
  }

  @org.junit.Test
  public void testIsDeclaredFalse() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    assertFalse(scope.isDeclared("test", true));
  }

  @org.junit.Test
  public void testIsDeclaredFalseRecursively() {
    Node outerRootNode = Node.newFunctionNode(Node.newNameNode("outer"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope outerScope = new Scope(null, outerRootNode);
    outerScope.declare("test", Node.newNameNode("test"), new FunctionType(null, null), null);

    Node innerRootNode = Node.newFunctionNode(Node.NEW_VAR_NODE, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope innerScope = new Scope(outerScope, innerRootNode);

    assertFalse(innerScope.isDeclared("test", true));
  }

  @org.junit.Test
  public void testGetVars() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    scope.declare("test1", Node.newNameNode("test1"), new FunctionType(null, null), null);
    scope.declare("test2", Node.newNameNode("test2"), new FunctionType(null, null), null);

    Var test1 = scope.getVar("test1");
    Var test2 = scope.getVar("test2");

    Iterator<Var> vars = scope.getVars();

    assertTrue(vars.hasNext());
    Var nextVar = vars.next();
    assertEquals(test1, nextVar);

    assertTrue(vars.hasNext());
    nextVar = vars.next();
    assertEquals(test2, nextVar);

    assertFalse(vars.hasNext());
  }

  @org.junit.Test
  public void testGetVarCount() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope scope = new Scope(null, rootNode);
    scope.declare("test1", Node.newNameNode("test1"), new FunctionType(null, null), null);
    scope.declare("test2", Node.newNameNode("test2"), new FunctionType(null, null), null);

    assertEquals(2, scope.getVarCount());
  }

  @org.junit.Test
  public void testIsGlobal() {
    Node rootNode = Node.newFunctionNode(Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope globalScope = new Scope(null, rootNode);
    assertTrue(globalScope.isGlobal());
  }

  @org.junit.Test
  public void testIsLocal() {
    Node rootNode = Node.newFunctionNode(Node.newNameNode("test"), Node.EMPTY_NODE_ARRAY, null, null, 0);
    Scope localScope = new Scope(null, rootNode);
    assertFalse(localScope.isGlobal());
  }
}
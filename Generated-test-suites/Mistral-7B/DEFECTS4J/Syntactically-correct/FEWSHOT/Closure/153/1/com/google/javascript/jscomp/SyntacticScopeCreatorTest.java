package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private static final String TEST_SOURCE_NAME = "testSourceName";

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope globalScope = scopeCreator.createScope(Node.NULL, null);
    assertSame(globalScope, scopeCreator.createScope(Node.NULL, globalScope));
  }

  @Test
  public void testCreateFunctionScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node functionNode = Node.newFunction(null, Node.NULL, Node.NULL, null);
    Scope functionScope = scopeCreator.createScope(functionNode, null);
    String functionName = "functionName";
    declareAndCheckVariable(functionScope, functionName, null, null);
  }

  @Test
  public void testCreateFunctionScopeWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node functionNode = Node.newFunction(null, Node.NULL, Node.NULL, null);
    Node argumentNode = Node.newName("arg1");
    functionNode.setFirstChild(argumentNode);
    Scope functionScope = scopeCreator.createScope(functionNode, null);
    String functionName = "functionName";
    String argumentName = "arg1";
    declareAndCheckVariable(functionScope, functionName, null, argumentName);
  }

  @Test
  public void testCreateFunctionScopeWithArgumentsAndBody() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node functionNode = Node.newFunction(null, Node.NULL, Node.NULL, null);
    Node argumentNode1 = Node.newName("arg1");
    Node argumentNode2 = Node.newName("arg2");
    functionNode.setFirstChild(argumentNode1);
    functionNode.setNext(argumentNode2);
    Node bodyNode = Node.newBlock(null);
    functionNode.setNext(bodyNode);
    Scope functionScope = scopeCreator.createScope(functionNode, null);
    String functionName = "functionName";
    String argumentName1 = "arg1";
    String argumentName2 = "arg2";
    declareAndCheckVariable(functionScope, functionName, null, argumentName1, argumentName2);
  }

  @Test
  public void testCreateBlockScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node blockNode = Node.newBlock(null);
    Scope blockScope = scopeCreator.createScope(blockNode, null);
    String variableName = "variableName";
    declareAndCheckVariable(blockScope, variableName, null);
  }

  @Test
  public void testCreateBlockScopeWithVariableDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node blockNode = Node.newBlock(null);
    Node variableNode = Node.newName("variableName");
    blockNode.setFirstChild(variableNode);
    Scope blockScope = scopeCreator.createScope(blockNode, null);
    String variableName = "variableName";
    declareAndCheckVariable(blockScope, variableName, null);
  }

  @Test
  public void testCreateBlockScopeWithVariableDeclarationAndAssignment() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node blockNode = Node.newBlock(null);
    Node variableNode = Node.newName("variableName");
    Node assignmentNode = Node.newAssign(variableNode, Node.newNum(1));
    blockNode.setFirstChild(variableNode);
    blockNode.setNext(assignmentNode);
    Scope blockScope = scopeCreator.createScope(blockNode, null);
    String variableName = "variableName";
    declareAndCheckVariable(blockScope, variableName, Node.Num.class);
  }

  @Test
  public void testCreateParentChildScopes() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator parentScopeCreator = new SyntacticScopeCreator(compiler);
    SyntacticScopeCreator childScopeCreator = new SyntacticScopeCreator(compiler);
    Node parentBlockNode = Node.newBlock(null);
    Node childFunctionNode = Node.newFunction(null, Node.NULL, Node.NULL, null);
    Scope parentScope = parentScopeCreator.createScope(parentBlockNode, null);
    Scope childScope = childScopeCreator.createScope(childFunctionNode, parentScope);
    String variableName = "variableName";
    declareAndCheckVariable(childScope, variableName, null);
    Var parentVariable = parentScope.getVar(variableName);
    assertNull(parentVariable);
  }

  @Test
  public void testCreateNestedScopes() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node parentBlockNode = Node.newBlock(null);
    Node functionNode1 = Node.newFunction(null, Node.NULL, Node.NULL, null);
    Node functionNode2 = Node.newFunction(null, Node.NULL, Node.NULL, null);
    Node functionNode3 = Node.newFunction(null, Node.NULL, Node.NULL, null);
    functionNode1.setFirstChild(functionNode2);
    functionNode1.setNext(functionNode3);
    parentBlockNode.setFirstChild(functionNode1);
    Scope parentScope = scopeCreator.createScope(parentBlockNode, null);
    String variableName = "variableName";
    declareAndCheckVariable(parentScope, variableName, 0);
    declareAndCheckVariable(parentScope, variableName, 1);
    declareAndCheckVariable(parentScope, variableName, 2);
  }

  @Test
  public void testCreateGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node globalBlockNode = Node.newBlock(null);
    Scope globalScope = scopeCreator.createScope(globalBlockNode, null);
    String variableName = "variableName";
    declareAndCheckVariable(globalScope, variableName, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullParent() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node blockNode = Node.newBlock(null);
    Scope scope = scopeCreator.createScope(blockNode, null);
  }

  @Test
  public void testCreateScopeWithEmptyNode() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope scope = scopeCreator.createScope(Node.NULL, null);
    assertNull(scope);
  }

  @Test
  public void testCreateScopeWithInvalidNodeType() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node node = Node.newName("invalidNodeType");
    try {
      Scope scope = scopeCreator.createScope(node, null);
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Expected a statement node"));
    }
  }

  private void declareAndCheckVariable(Scope scope, String variableName, Class<?>... expectedTypes) {
    Node variableNode = Node.newName(variableName);
    Scope childScope = scopeCreator.createScope(variableNode, scope);
    scope.declare(variableName, variableNode, JSType.UNDEFINED, null);
    Var variable = scope.getVar(variableName);
    Preconditions.checkState(variable != null);
    if (expectedTypes != null && expectedTypes.length > 0) {
      assertTrue(variable.type.isAssignableFrom(expectedTypes[0]));
      if (expectedTypes.length > 1) {
        fail("Multiple expected types are not supported");
      }
    }
  }
}
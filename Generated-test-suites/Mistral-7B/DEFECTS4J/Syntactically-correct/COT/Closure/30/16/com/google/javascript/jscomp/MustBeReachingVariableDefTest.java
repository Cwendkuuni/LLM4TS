package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef mustBeReachingVariableDef;
  private Scope jsScope;

  @Before
  public void setup() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    jsScope = new Scope();
    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, null);
  }

  @Test
  public void testBasicFunctionality() {
    // Test basic functionality of MustBeReachingVariableDef class by checking
    // the method's functionality
    Node testNode = Node.newExpression(Token.ASSIGN, Node.NAME, Node.NAME, null);
    MustDef output = mustBeReachingVariableDef.flowThrough(testNode, null);
    Definition definition = new Definition(testNode);
    assertEquals(definition, output.reachingDef.get(jsScope.getVar(testNode.getString())));
  }

  @Test
  public void testBranching() {
    // Test for branching scenarios
    Node ifNode = Node.newExpression(Token.IF, null, Node.NAME, Node.NAME);
    Node trueBranch = Node.newExpression(Token.ASSIGN, Node.NAME, Node.NAME, null);
    Node falseBranch = Node.newExpression(Token.ASSIGN, Node.NAME, Node.NAME, null);
    ifNode.setNext(trueBranch);
    ifNode.setNext(falseBranch);

    MustDef output = mustBeReachingVariableDef.flowThrough(ifNode, null);

    assertEquals(trueBranch, getDef("A", ifNode));
  }

  @Test
  public void testConditional() {
    // Test for conditional define scenarios
    Node ifNode = Node.newExpression(Token.IF, null, Node.NAME, Node.NAME);
    Node trueBranch = Node.newExpression(Token.ASSIGN, Node.NAME, Node.NAME, null);
    Node falseBranch = Node.newExpression(Token.ASSIGN, Node.NAME, Node.NAME, null);
    ifNode.setNext(trueBranch);
    ifNode.setNext(falseBranch);

    MustDef output = mustBeReachingVariableDef.flowThrough(ifNode, null);

    assertNull(getDef("A", ifNode));
  }

  @Test
  public void testEscapedVariables() {
    // Test for escaped variables scenarios
    Node testNode = Node.newExpression(Token.ASSIGN, Node.NAME, Node.NAME, null);
    Node escapedNode = Node.newExpression(Token.VAR, Node.NAME, null, null);
    testNode.setNext(escapedNode);
    mustBeReachingVariableDef.escaped.add(jsScope.getVar(escapedNode.getString()));

    MustDef output = mustBeReachingVariableDef.flowThrough(testNode, null);

    assertNull(output.reachingDef.get(jsScope.getVar(testNode.getString())));
  }

  @Test
  public void testOuterScopeVariableDependencies() {
    // Test for scenarios where variable depends on outer scope variables
    Node outerScopeVariable = Node.newExpression(Token.VAR, Node.NAME, null, null);
    Node testNode = Node.newExpression(Token.ASSIGN, Node.NAME, outerScopeVariable, null);

    MustDef output = mustBeReachingVariableDef.flowThrough(testNode, null);

    assertTrue(dependsOnOuterScopeVars("outerScopeVariable", testNode));
  }
}
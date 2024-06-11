package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LiveVariablesAnalysisTest {

  @Test
  public void testEmptyFunction() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.forwardPropagate(null);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(scope.getVarCount());
    assertEquals(expected, result);
  }

  @Test
  public void testLocalVariable() {
    Scope scope = new Scope();
    scope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node node = Node.newIntLiteral(1);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    expected.liveSet.set(scope.getVar("x").index);
    assertEquals(expected, result);
  }

  @Test
  public void testDeadVariable() {
    Scope scope = new Scope();
    scope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node assign = Node.newAssign("x", Node.newIntLiteral(1));
    Node use = Node.newName("x");
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(assign, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    assertFalse(expected.isLive(scope.getVar("x").index));
    result = analysis.flowThrough(use, result);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(scope.getVar("x").index);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithReturn() {
    Scope scope = new Scope();
    scope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node assign = Node.newAssign("x", Node.newIntLiteral(1));
    Node returnNode = Node.newReturn(Node.newName("x"));
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(assign, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(scope.getVar("x").index);
    result = analysis.flowThrough(returnNode, result);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(result);
    assertEquals(expected, result);
  }

  @Test
  public void testConditional() {
    Scope scope = new Scope();
    scope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node condition = Node.newBoolLiteral(true);
    Node assignX = Node.newAssign("x", Node.newIntLiteral(1));
    Node assignY = Node.newAssign("y", Node.newIntLiteral(2));
    Node ifNode = Node.newIf(condition, assignX, assignY);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(ifNode, input);
    LiveVariablesAnalysis.LiveVariableLattice expectedX = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expectedX.liveSet.set(scope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice expectedY = new LiveVariablesAnalysis.LiveVariableLattice(input);
    assertEquals(expectedX, result);
  }

  @Test
  public void testWhileLoop() {
    Scope scope = new Scope();
    scope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node condition = Node.newBoolLiteral(true);
    Node assign = Node.newAssign("x", Node.newAssignName("x", Node.newIntLiteral(1)));
    Node whileNode = Node.newWhile(condition, assign);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(whileNode, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(scope.getVar("x").index);
    assertEquals(expected, result);
  }

  @Test
  public void testForLoop() {
    Scope scope = new Scope();
    scope.declareVar("i", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node start = Node.newAssign("i", Node.newIntLiteral(0));
    Node condition = Node.newBoolLiteral(true);
    Node increment = Node.newAssign("i", Node.newAssignName("i", Node.newIntLiteral(1)));
    Node forNode = Node.newFor(start, condition, increment, null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(forNode, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(scope.getVar("i").index);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithEscapedVariable() {
    Scope rootScope = new Scope();
    Scope innerScope = new Scope(rootScope);
    innerScope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, innerScope, null);
    Node functionNode = Node.newFunction(null, null, null, null);
    Node assignX = Node.newAssign("x", Node.newIntLiteral(1));
    Node returnNode = Node.newReturn(Node.newName("x"));
    Node body = Node.newBlock(null);
    body.addChild(assignX);
    body.addChild(returnNode);
    functionNode.setBody(body);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(functionNode, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(innerScope.getVar("x").index);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithEscapedFunction() {
    Scope rootScope = new Scope();
    Scope innerScope = new Scope(rootScope);
    innerScope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, innerScope, null);
    Node functionNode = Node.newFunction(null, null, null, null);
    Node innerFunctionNode = Node.newFunction(null, null, null, null);
    Node assignX = Node.newAssign("x", Node.newIntLiteral(1));
    Node innerFunctionBody = Node.newBlock(null);
    innerFunctionBody.addChild(assignX);
    innerFunctionBody.addChild(Node.newReturn(Node.newName("x")));
    innerFunctionNode.setBody(innerFunctionBody);
    Node callInnerFunction = Node.newCall(innerFunctionNode, Node.newArrayLiteral(Nodes.toList(Node.newArrayLiteral(Nodes.emptyList()))));
    Node body = Node.newBlock(null);
    body.addChild(callInnerFunction);
    functionNode.setBody(body);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(functionNode, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(innerScope.getVar("x").index);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithEscapedVariableAndFunction() {
    Scope rootScope = new Scope();
    Scope innerScope = new Scope(rootScope);
    innerScope.declareVar("x", Node.INT_TYPE, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, innerScope, null);
    Node functionNode = Node.newFunction(null, null, null, null);
    Node innerFunctionNode = Node.newFunction(null, null, null, null);
    Node innerFunctionBody = Node.newBlock(null);
    Node assignX = Node.newAssign("x", Node.newIntLiteral(1));
    innerFunctionBody.addChild(assignX);
    innerFunctionBody.addChild(Node.newReturn(Node.newName("x")));
    innerFunctionNode.setBody(innerFunctionBody);
    Node callInnerFunction = Node.newCall(innerFunctionNode, Node.newArrayLiteral(Nodes.toList(Node.newArrayLiteral(Nodes.emptyList()))));
    Node assignY = Node.newAssign("y", Node.newCall(innerFunctionNode, Node.newArrayLiteral(Nodes.toList(Node.newArrayLiteral(Nodes.emptyList())))));
    Node body = Node.newBlock(null);
    body.addChild(callInnerFunction);
    body.addChild(assignY);
    functionNode.setBody(body);
    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(entry);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(functionNode, input);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(input);
    expected.liveSet.set(innerScope.getVar("x").index);
    expected.liveSet.set(innerScope.getVar("y").index);
    assertEquals(expected, result);
  }
}
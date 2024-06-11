package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private Scope jsScope;
  private ControlFlowGraph<Node> controlFlowGraph;
  private AbstractCompiler compiler;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    jsScope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>();
    compiler = new CompilerOptions();
    liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
  }

  @Test
  public void testConstructor() {
    assertNotNull(liveVariablesAnalysis);
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    assertNotNull(entryLattice);
    assertEquals(jsScope.getVarCount(), entryLattice.liveSet.size());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    assertNotNull(initialEstimateLattice);
    assertEquals(jsScope.getVarCount(), initialEstimateLattice.liveSet.size());
  }

  @Test
  public void testIsForward() {
    assertFalse(liveVariablesAnalysis.isForward());
  }

  @Test
  public void testFlowThroughSimpleCase() {
    Node simpleNode = NodeUtil.createIntLiteralNode(1);
    LiveVariablesAnalysis.LiveVariableLattice inputLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(simpleNode, inputLattice);
    assertTrue(outputLattice.equals(inputLattice));
  }

  @Test
  public void testFlowThroughAssignment() {
    Node assignNode = NodeUtil.createNameNode("x");
    Node assignValueNode = NodeUtil.createIntLiteralNode(1);
    Node assignOperatorNode = NodeUtil.createAssignOperatorNode(assignNode, assignValueNode);
    LiveVariablesAnalysis.LiveVariableLattice inputLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(assignOperatorNode, inputLattice);
    BitSet liveVariables = outputLattice.liveSet;
    assertTrue(liveVariables.get(jsScope.getVar("x").index));
  }

  @Test
  public void testFlowThroughAssignmentWithCondition() {
    Node assignNode = NodeUtil.createNameNode("x");
    Node assignValueNode = NodeUtil.createIntLiteralNode(1);
    Node conditionNode = NodeUtil.createIntLiteralNode(0);
    Node assignOperatorNode = NodeUtil.createConditionalAssignmentOperatorNode(assignNode, assignValueNode, conditionNode);
    LiveVariablesAnalysis.LiveVariableLattice inputLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(assignOperatorNode, inputLattice);
    BitSet liveVariables = outputLattice.liveSet;
    assertTrue(liveVariables.get(jsScope.getVar("x").index));
  }

  @Test
  public void testFlowThroughWithGenAndKill() {
    Node simpleNode = NodeUtil.createIntLiteralNode(1);
    Node assignNode = NodeUtil.createNameNode("x");
    Node assignValueNode = NodeUtil.createIntLiteralNode(2);
    Node assignOperatorNode = NodeUtil.createAssignOperatorNode(assignNode, assignValueNode);
    Node conditionNode = NodeUtil.createIntLiteralNode(1);
    Node ifNode = NodeUtil.createIfNode(conditionNode, simpleNode, assignOperatorNode);
    LiveVariablesAnalysis.LiveVariableLattice inputLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(ifNode, inputLattice);
    BitSet liveVariables = outputLattice.liveSet;
    assertTrue(liveVariables.get(jsScope.getVar("x").index));
    assertFalse(liveVariables.get(jsScope.getVar("y").index));
  }

  @Test
  public void testGetEscapedLocals() {
    String functionName = "testFunction";
    Node rootNode = NodeUtil.createFunctionNode(functionName, Arrays.asList("x", "y"), Arrays.asList(NodeUtil.createIntLiteralNode(1), NodeUtil.createIntLiteralNode(2)), null);
    jsScope.pushScope(NodeUtil.createRootScopeNode());
    jsScope.declareVar(NodeUtil.createNameNode("x"), NodeUtil.createIntTypeNode());
    jsScope.declareVar(NodeUtil.createNameNode("y"), NodeUtil.createIntTypeNode());
    jsScope.setRootNode(rootNode);
    liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertTrue(escapedLocals.isEmpty());
    Node functionBodyNode = rootNode.getFirstChild();
    controlFlowGraph.addNode(rootNode);
    controlFlowGraph.addNode(functionBodyNode);
    controlFlowGraph.addEdge(rootNode, functionBodyNode, null);
    liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    Var x = jsScope.getVar("x");
    Var y = jsScope.getVar("y");
    assertEquals(2, escapedLocals.size());
    assertTrue(escapedLocals.contains(x));
    assertTrue(escapedLocals.contains(y));
  }

  @Test
  public void testGetVarIndex() {
    String variableName = "x";
    int expectedIndex = jsScope.getVar(variableName).index;
    int actualIndex = liveVariablesAnalysis.getVarIndex(variableName);
    assertEquals(expectedIndex, actualIndex);
  }

  @Test(expected = NullPointerException.class)
  public void testGetVarIndexWithNullVariableName() {
    liveVariablesAnalysis.getVarIndex(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetVarIndexWithNonExistentVariable() {
    String nonExistentVariableName = "y";
    liveVariablesAnalysis.getVarIndex(nonExistentVariableName);
  }
}
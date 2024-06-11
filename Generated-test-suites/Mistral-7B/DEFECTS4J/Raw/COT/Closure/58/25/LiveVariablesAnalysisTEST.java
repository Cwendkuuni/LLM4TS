package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;

/**
 * Tests for {@link LiveVariablesAnalysis} class.
 */
@RunWith(JUnit4.class)
public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Add your test cases here
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();

    LiveVariableLattice expectedEntryLattice = new LiveVariableLattice(10);
    assertEquals(expectedEntryLattice, entryLattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    LiveVariableLattice expectedInitialEstimateLattice = new LiveVariableLattice(10);
    assertEquals(expectedInitialEstimateLattice, initialEstimateLattice);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlowThroughWithNullInput() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.flowThrough(Node.NULL, null);
  }

  @Test
  public void testFlowThroughSimpleAssignment() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    String variableName = "testVariable";
    Var testVariable = new Var("testVariable", 0);
    analysis.jsScope.declareVar(variableName, testVariable);

    Node assignmentNode = Node.createAssign(Token.ASSIGN, Node.createName(variableName), Node.createIntLiteral(10));

    LiveVariableLattice inputLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    inputLattice.liveSet.set(testVariable.index);

    LiveVariableLattice outputLattice = analysis.flowThrough(assignmentNode, inputLattice);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.set(testVariable.index);

    assertEquals(expectedOutputLattice, outputLattice);
  }

  @Test
  public void testFlowThroughAssignmentWithDeadVariable() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    String deadVariableName = "deadVariable";
    Var deadVariable = new Var("deadVariable", 0);
    analysis.jsScope.declareVar(deadVariableName, deadVariable);

    Node assignmentNode = Node.createAssign(Token.ASSIGN, Node.createName(variableName), Node.createName(deadVariableName));

    LiveVariableLattice inputLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    inputLattice.liveSet.set(analysis.jsScope.getVar("testVariable").index);
    inputLattice.liveSet.set(analysis.jsScope.getVar("deadVariable").index);

    LiveVariableLattice outputLattice = analysis.flowThrough(assignmentNode, inputLattice);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.set(analysis.jsScope.getVar("testVariable").index);

    assertEquals(expectedOutputLattice, outputLattice);
  }

  @Test
  public void testFlowThroughAssignmentWithCondition() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    String variableName = "testVariable";
    Var testVariable = new Var("testVariable", 0);
    analysis.jsScope.declareVar(variableName, testVariable);

    String conditionVariableName = "conditionVariable";
    Var conditionVariable = new Var("conditionVariable", 1);
    analysis.jsScope.declareVar(conditionVariableName, conditionVariable);

    Node assignmentNode = Node.createAssign(Token.ASSIGN, Node.createName(variableName), Node.createName(conditionVariableName));
    Node conditionNode = Node.createBinaryOp(Token.LOGAND, Node.createLiteral(true), Node.createName(conditionVariableName));

    LiveVariableLattice inputLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    inputLattice.liveSet.set(testVariable.index);
    inputLattice.liveSet.set(conditionVariable.index);

    LiveVariableLattice outputLattice = analysis.flowThrough(assignmentNode, inputLattice);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.set(testVariable.index);

    LiveVariableLattice outputLatticeWithCondition = analysis.flowThrough(conditionNode, outputLattice);

    LiveVariableLattice expectedOutputLatticeWithCondition = new LiveVariableLattice(outputLattice);
    expectedOutputLatticeWithCondition.liveSet.set(testVariable.index);

    assertEquals(expectedOutputLatticeWithCondition, outputLatticeWithCondition);
  }

  @Test(expected = AssertionError.class)
  public void testFlowThroughWithNonAssignNode() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    Node nonAssignNode = Node.createName("testVariable");

    LiveVariableLattice inputLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    inputLattice.liveSet.set(analysis.jsScope.getVar("testVariable").index);

    LiveVariableLattice outputLattice = analysis.flowThrough(nonAssignNode, inputLattice);

    // The test should fail as the method only accepts assignment nodes and not non-assignment nodes
  }

  @Test
  public void testGetEscapedLocals() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    String variableName = "testVariable";
    Var testVariable = new Var("testVariable", 0);
    analysis.jsScope.declareVar(variableName, testVariable);

    String argumentsName = "arguments";
    analysis.markAllParametersEscaped();

    Set<Var> escapedLocals = analysis.getEscapedLocals();

    assertTrue(escapedLocals.contains(testVariable));
    assertTrue(escapedLocals.contains(analysis.jsScope.getVar(argumentsName)));
  }

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    String variableName = "testVariable";
    Var testVariable = new Var("testVariable", 0);
    analysis.jsScope.declareVar(variableName, testVariable);

    int expectedVarIndex = analysis.getVarIndex(variableName);
    int actualVarIndex = testVariable.index;

    assertEquals(expectedVarIndex, actualVarIndex);
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);

    boolean expectedResult = false;
    boolean actualResult = analysis.isForward();

    assertEquals(expectedResult, actualResult);
  }
}
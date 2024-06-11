package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private Scope jsScope;
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    // Setup your scope, compiler and control flow graph here.
    // For simplicity, we will not create them in this test.
    this.jsScope = null; // Initialize your scope here.
    this.compiler = null; // Initialize your compiler here.
    this.cfg = null; // Initialize your control flow graph here.
    this.liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
  }

  @Test
  public void testGetEscapedLocals() {
    Set<Var> expectedEscapedLocals = new HashSet<>();
    expectedEscapedLocals.add(jsScope.getVar("x"));
    expectedEscapedLocals.add(jsScope.getVar("y"));
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);
  }

  @Test
  public void testComputeEntryLattice() {
    LiveVariableLattice expectedEntryLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice actualEntryLattice = liveVariablesAnalysis.createEntryLattice();
    assertEquals(expectedEntryLattice, actualEntryLattice);
  }

  @Test
  public void testComputeInitialEstimateLattice() {
    LiveVariableLattice expectedInitialEstimateLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice actualInitialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    assertEquals(expectedInitialEstimateLattice, actualInitialEstimateLattice);
  }

  @Test
  public void testFlowThroughSimpleAssignment() {
    int varIndex = 0;
    Node assignmentNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newName("x"),
        Node.newNumber(42)
    });

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(varIndex);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    LiveVariableLattice actualOutputLattice = liveVariablesAnalysis.flowThrough(assignmentNode, inputLattice);

    assertEquals(expectedOutputLattice, actualOutputLattice);
  }

  @Test
  public void testFlowThroughSimpleAssignmentWithKill() {
    int varIndex = 0;
    Node assignmentNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newName("x"),
        Node.newName("y")
    });

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(varIndex);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.clear(varIndex);

    LiveVariableLattice actualOutputLattice = liveVariablesAnalysis.flowThrough(assignmentNode, inputLattice);

    assertEquals(expectedOutputLattice, actualOutputLattice);
  }

  @Test
  public void testFlowThroughComplexStatement() {
    int varIndexX = 0;
    int varIndexY = 1;
    int varIndexZ = 2;

    Node statementNode = Node.newBlock(Arrays.asList(
        Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newNumber(1)),
        Node.newAssignment(Token.ASSIGN, Node.newName("y"), Node.newName("x")),
        Node.newAssignment(Token.ASSIGN, Node.newName("z"), Node.newNumber(42))
    ));

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(varIndexX);
    inputLattice.liveSet.set(varIndexY);
    inputLattice.liveSet.set(varIndexZ);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.set(varIndexX);
    expectedOutputLattice.liveSet.set(varIndexY);

    LiveVariableLattice actualOutputLattice = liveVariablesAnalysis.flowThrough(statementNode, inputLattice);

    assertEquals(expectedOutputLattice, actualOutputLattice);
  }

  @Test
  public void testFlowThroughAssignmentWithCondition() {
    int varIndexX = 0;
    int varIndexY = 1;

    Node assignmentNode = Node.newConditionalExpression(
        Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newNumber(1)),
        Node.newBooleanLiteral(true),
        Node.newAssignment(Token.ASSIGN, Node.newName("y"), Node.newNumber(42)),
        Node.newBooleanLiteral(false)
    );

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(varIndexX);
    inputLattice.liveSet.set(varIndexY);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.set(varIndexX);

    LiveVariableLattice actualOutputLattice = liveVariablesAnalysis.flowThrough(assignmentNode, inputLattice);

    assertEquals(expectedOutputLattice, actualOutputLattice);
  }

  @Test
  public void testFlowThroughAssignmentWithConditionAndKill() {
    int varIndexX = 0;
    int varIndexY = 1;

    Node assignmentNode = Node.newConditionalExpression(
        Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newName("y")),
        Node.newBooleanLiteral(true),
        Node.newAssignment(Token.ASSIGN, Node.newName("y"), Node.newNumber(42)),
        Node.newBooleanLiteral(false)
    );

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(varIndexX);
    inputLattice.liveSet.set(varIndexY);

    LiveVariableLattice expectedOutputLattice = new LiveVariableLattice(inputLattice);
    expectedOutputLattice.liveSet.clear(varIndexY);

    LiveVariableLattice actualOutputLattice = liveVariablesAnalysis.flowThrough(assignmentNode, inputLattice);

    assertEquals(expectedOutputLattice, actualOutputLattice);
  }
}
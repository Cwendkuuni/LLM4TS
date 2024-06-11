package org.apache.commons.cli; // for consistency with the original test class

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis_simple() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new NoOpCompiler(); // a dummy compiler for testing

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();

    // Test empty function
    Node emptyFunction = Node.newBlock(Token.SCRIPT, null);
    analysis.compute(emptyFunction, analysis.createEntryLattice());
    assertTrue(analysis.getResult().isEqual(analysis.createEntryLattice()));
    assertFalse(escapedLocals.isEmpty());

    // Test function with no assignments or local variables
    Node noAssignmentFunction = Node.newBlock(Token.SCRIPT, null);
    Node printStatement = Node.newName("print", new Object[] {}, null);
    Node printCall = Node.newCall(printStatement, null);
    noAssignmentFunction.addChild(printCall);
    analysis.compute(noAssignmentFunction, analysis.createEntryLattice());
    assertTrue(analysis.getResult().isEqual(analysis.createEntryLattice()));
    assertFalse(escapedLocals.isEmpty());

    // Test function with local variables and assignments
    Node assignmentFunction = Node.newBlock(Token.SCRIPT, null);
    Node x = Node.newVar("x", null);
    Node y = Node.newVar("y", null);
    Node assignX = Node.newAssign(x, Node.newNumber(1));
    Node assignY = Node.newAssign(y, Node.newNumber(2));
    Node printX = Node.newCall(Node.newName("print", new Object[] {}, null), new Object[] {x});
    Node printY = Node.newCall(Node.newName("print", new Object[] {}, null), new Object[] {y});
    assignmentFunction.addChild(assignX);
    assignmentFunction.addChild(assignY);
    assignmentFunction.addChild(printX);
    assignmentFunction.addChild(printY);
    analysis.compute(assignmentFunction, analysis.createEntryLattice());

    // Check the results
    LiveVariablesAnalysis resultAnalysis = analysis.getResult();
    BitSet liveVariables = resultAnalysis.liveSet;
    Set<Var> expectedLiveVariables = new HashSet<>();
    expectedLiveVariables.add(jsScope.getVar("x"));
    expectedLiveVariables.add(jsScope.getVar("y"));
    assertTrue(liveVariables.equals(BitSet.valueOf(expectedLiveVariables.stream().mapToInt(Var::getIndex).toArray())));

    // Test function with local variables, assignments, and conditional statements
    Node conditionalFunction = Node.newBlock(Token.SCRIPT, null);
    Node xConditional = Node.newVar("x", null);
    Node yConditional = Node.newVar("y", null);
    Node assignX = Node.newAssign(xConditional, Node.newNumber(1));
    Node assignY = Node.newAssign(yConditional, Node.newNumber(2));
    Node ifStatement = Node.newIf(Node.newConditionalExpression(Node.newBinaryOp(Token.LOGICAL_OR, Node.newBinaryOp(Token.LOGICAL_AND, Node.newName("x", null), Node.newNumber(0)), Node.newName("y", null).isNull()), null));
    ifStatement.addChild(Node.newBlock(Token.BLOCK, null));
    ifStatement.getLastChild().addChild(assignX);
    ifStatement.getLastChild().addChild(assignY);
    conditionalFunction.addChild(assignX);
    conditionalFunction.addChild(assignY);
    conditionalFunction.addChild(ifStatement);
    analysis.compute(conditionalFunction, analysis.createEntryLattice());

    // Check the results
    resultAnalysis = analysis.getResult();
    liveVariables = resultAnalysis.liveSet;
    expectedLiveVariables = new HashSet<>();
    expectedLiveVariables.add(jsScope.getVar("x"));
    expectedLiveVariables.add(jsScope.getVar("y"));
    assertTrue(liveVariables.equals(BitSet.valueOf(expectedLiveVariables.stream().mapToInt(Var::getIndex).toArray())));

  }
}
package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LiveVariablesAnalysisTest {

 private Scope jsScope;
 private LiveVariablesAnalysis analysis;

 @Before
 public void setUp() {
 jsScope = Scope.empty();
 jsScope.declareVar("x", true);
 jsScope.declareVar("y", true);
 jsScope.declareVar("z", true);

 analysis = new LiveVariablesAnalysis(ControlFlowGraph.empty(), jsScope, null);
 }

 @Test
 public void testEmpty() {
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(Node.NULL);
 assertNotNull(result);
 assertEquals(ImmutableSet.of(jsScope.getVar("x"), jsScope.getVar("y"), jsScope.getVar("z")), result.getLiveVariables());
 }

 @Test
 public void testAssignments() {
 Node assignX = Node.newAssign(Token.VAR, jsScope.getVar("x"), Node.newName(Token.NAME, "1"));
 Node assignY = Node.newAssign(Token.VAR, jsScope.getVar("y"), Node.newName(Token.NAME, "2"));
 Node assignZ = Node.newAssign(Token.VAR, jsScope.getVar("z"), Node.newName(Token.NAME, "3"));

 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(assignX);
 assertEquals(ImmutableSet.of(jsScope.getVar("x"), jsScope.getVar("y"), jsScope.getVar("z")), result.getLiveVariables());

 result = analysis.computeResult(assignY);
 assertEquals(ImmutableSet.of(jsScope.getVar("x"), jsScope.getVar("y"), jsScope.getVar("z")), result.getLiveVariables());

 result = analysis.computeResult(assignZ);
 assertEquals(ImmutableSet.of(jsScope.getVar("x"), jsScope.getVar("y"), jsScope.getVar("z")), result.getLiveVariables());

 result = analysis.computeResult(assignX); // re-entering the same assignment should not change liveness
 assertEquals(ImmutableSet.of(jsScope.getVar("x"), jsScope.getVar("y"), jsScope.getVar("z")), result.getLiveVariables());
 }

 @Test
 public void testConditional() {
 Node ifNode = Node.newIf(Token.COND, Node.newName(Token.NAME, "true"), Node.NULL, Node.NULL);
 Node assignX = Node.newAssign(Token.VAR, jsScope.getVar("x"), Node.newName(Token.NAME, "1"));

 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(assignX);
 assertEquals(ImmutableSet.of(jsScope.getVar("x")), result.getLiveVariables()); // x is only live within the conditional block

 LiveVariablesAnalysis.LiveVariableLattice resultIf = analysis.computeResult(ifNode);
 assertEquals(ImmutableSet.of(jsScope.getVar("x")), resultIf.getLiveVariables()); // x is only live within the conditional block

 LiveVariablesAnalysis.LiveVariableLattice resultElse = analysis.computeResult(Node.NULL); // empty block after the conditional
 assertEquals(ImmutableSet.of(jsScope.getVar("x")), resultElse.getLiveVariables()); // x is still live after the conditional
 }

 @Test
 public void testArguments() {
 Node function = Node.newFunction(Node.newName(Token.NAME, "test"), ImmutableList.of(), ImmutableList.of(Node.NULL), Node.NULL);
 Node assignArg = Node.newAssign(Token.VAR, jsScope.getVar("x"), Node.newName(Token.NAME, "1"));
 Node callFunction = Node.newCall(Node.newName(Token.NAME, "test"), ImmutableList.of(assignArg));

 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(callFunction);
 assertEquals(ImmutableSet.of(jsScope.getVar("x")), result.getLiveVariables()); // argument is still live after the function call
 }

 @Test
 public void testEscapedVariables() {
 Scope escapingScope = Scope.empty();
 escapingScope.declareVar("outerX", true);
 escapingScope.declareVar("outerY", true);
 escapingScope.declareVar("outerZ", true);
 escapingScope.declareFunc("func", ImmutableList.of(), ImmutableList.of("x"), ImmutableList.of(Node.NULL));
 escapingScope.declareVar("innerX", true);
 escapingScope.declareVar("innerY", true);
 escapingScope.declareVar("innerZ", true);

 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(ControlFlowGraph.empty(), escapingScope, null);
 LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
 LiveVariablesAnalysis.LiveVariableLattice funcEntry = analysis.createEntryLattice();

 LiveVariablesAnalysis.LiveVariableLattice result = analysis.join(ImmutableList.of(entry, funcEntry));
 assertEquals(ImmutableSet.of(escapingScope.getVar("outerX"), escapingScope.getVar("outerY"), escapingScope.getVar("outerZ"),
                            escapingScope.getFunc("func").getLocalVars().get(0)), result.getLiveVariables());
 }

 @Test
 public void testNoLiveness() {
 Node neverAssigned = Node.newName(Token.NAME, "neverAssigned");
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(neverAssigned);
 assertFalse(result.isLive(neverAssigned));
 }

 @Test
 public void testDeadAssignment() {
 Node deadAssignment = Node.newAssign(Token.VAR, Node.NULL, Node.newName(Token.NAME, "x"));
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(deadAssignment);
 assertFalse(result.isLive(jsScope.getVar("x")));
 }
}
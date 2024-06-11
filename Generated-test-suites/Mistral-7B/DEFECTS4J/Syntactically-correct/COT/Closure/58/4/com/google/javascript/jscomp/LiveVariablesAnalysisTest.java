import static org.junit.Assert.*;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createToken;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Create test data: ControlFlowGraph, Scope, and AbstractCompiler
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    // Assuming AbstractCompiler is initialized and passed as an argument

    // Create LiveVariablesAnalysis instance
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    // Test getEscapedLocals() method
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(2, escapedLocals.size());
    assertTrue(escapedLocals.contains(jsScope.getVar("arguments")));

    // Test getVarIndex() method
    int varIndex = analysis.getVarIndex("x");
    assertEquals(0, varIndex);

    // Test isForward() method
    assertFalse(analysis.isForward());

    // Test createEntryLattice() method
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    assertFalse(entryLattice.isLive(createNode(createToken(Token.KEYWORD, "function"), createNode(createToken(Token.NAME, "x")))));

    // Test createInitialEstimateLattice() method
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    assertFalse(initialEstimateLattice.isLive(createNode(createToken(Token.KEYWORD, "function"), createNode(createToken(Token.NAME, "x")))));

    // Test flowThrough() method with a simple statement: x = x
    LiveVariablesAnalysis.LiveVariableLattice inputLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice outputLattice = analysis.flowThrough(createNode(createToken(Token.ASSIGN, null)), inputLattice);
    assertTrue(outputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    // Test flowThrough() method with a complex statement: if (x > 0) x = x + 1
    LiveVariablesAnalysis.LiveVariableLattice ifLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    ifLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice ifOutputLattice = analysis.flowThrough(createNode(createToken(Token.IF, null)), ifLattice);
    assertTrue(ifOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    LiveVariablesAnalysis.LiveVariableLattice assignLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    assignLattice.liveSet.set(jsScope.getVar("x").index);
    Node condition = createNode(createToken(Token.NUM, "1"), createNode(createToken(Token.GT, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "0"))));
    LiveVariablesAnalysis.LiveVariableLattice assignOutputLattice = analysis.flowThrough(createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.PLUS, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "1")))), assignLattice);
    assertTrue(assignOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    // Test flowThrough() method with a complex statement: if (x > 0) for (int i = 0; i < x; i++) { x += i; }
    LiveVariablesAnalysis.LiveVariableLattice forInputLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    forInputLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice forOutputLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());

    LiveVariablesAnalysis.LiveVariableLattice ifForLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    ifForLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice ifForOutputLattice = analysis.flowThrough(condition, ifForLattice);

    LiveVariablesAnalysis.LiveVariableLattice forInitLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    forInitLattice.liveSet.set(jsScope.getVar("i").index);
    LiveVariablesAnalysis.LiveVariableLattice forAssignLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    forAssignLattice.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice forLoopLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    forLoopLattice.liveSet.set(jsScope.getVar("i").index);
    forLoopLattice.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice forOutputLattice = analysis.flowThrough(createNode(createToken(Token.FOR, null), forInitLattice, condition, forAssignLattice, forLoopLattice), ifForOutputLattice);
    assertTrue(forOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    // Test for edge cases
    LiveVariablesAnalysis.LiveVariableLattice unreachableLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    unreachableLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice unreachableOutputLattice = analysis.flowThrough(createNode(createToken(Token.RETURN, null)), unreachableLattice);
    assertFalse(unreachableOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    loopWithDeadAssignLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignOutputLattice = analysis.flowThrough(createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "0"))), loopWithDeadAssignLattice);
    assertFalse(loopWithDeadAssignOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignAndKillLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    loopWithDeadAssignAndKillLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignAndKillOutputLattice = analysis.flowThrough(createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "0"))), loopWithDeadAssignLattice);
    loopWithDeadAssignAndKillOutputLattice.liveSet.set(jsScope.getVar("x").index, false);
    assertFalse(loopWithDeadAssignAndKillOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignAndConditionalKillLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    loopWithDeadAssignAndConditionalKillLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignAndConditionalKillOutputLattice = analysis.flowThrough(createNode(createToken(Token.IF, null), createNode(createToken(Token.NUM, "0")), createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "0"))), createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "1")))), loopWithDeadAssignLattice);
    assertFalse(loopWithDeadAssignAndConditionalKillOutputLattice.isLive(createNode(createToken(Token.NAME, "x"))));

    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignAndConditionalEscapeLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    loopWithDeadAssignAndConditionalEscapeLattice.liveSet.set(jsScope.getVar("x").index);
    LiveVariablesAnalysis.LiveVariableLattice loopWithDeadAssignAndConditionalEscapeOutputLattice = analysis.flowThrough(createNode(createToken(Token.IF, null), createNode(createToken(Token.NUM, "0")), createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "0"))), createNode(createToken(Token.ASSIGN, null), createNode(createToken(Token.NAME, "x")), createNode(createToken(Token.NUM, "1"))), createNode(createToken(Token.NAME, "arguments"))), loopWithDeadAssignLattice);
    assertTrue(loopWithDeadAssignAndConditionalEscapeOutputLattice.isLive(createNode(createToken(Token.NAME, "arguments"))));
  }
}
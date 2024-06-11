import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isVar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Test case for empty CFG
    Scope emptyScope = new Scope();
    ControlFlowGraph emptyCfg = new ControlFlowGraph(emptyScope, null, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(emptyCfg, emptyScope, null);
    assertFalse(analysis.getEscapedLocals().isEmpty());
    assertEquals(0, analysis.getEscapedLocals().size());

    // Test case for simple variable assignment
    Node assignmentNode = new Node(Token.ASSIGN, null);
    Scope simpleScope = new Scope();
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    ControlFlowGraph simpleCfg = new ControlFlowGraph(simpleScope, null, null);
    simpleCfg.addNode(assignmentNode);
    LiveVariablesAnalysis simpleAnalysis = new LiveVariablesAnalysis(simpleCfg, simpleScope, null);
    assertTrue(simpleAnalysis.getEscapedLocals().isEmpty());
    assertEquals(1, simpleAnalysis.getEscapedLocals().size());
    SimpleVariableAssignmentTest(simpleAnalysis, "x", true, true);

    // Test case for variable assignment in 'if' statement
    Node ifNode = new Node(Token.IF, null);
    Node assignmentNodeIf = new Node(Token.ASSIGN, null);
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    simpleScope.declareVar("y", new Var(simpleScope, "y", Token.VAR_QUALIFIED, 1));
    ControlFlowGraph ifCfg = new ControlFlowGraph(simpleScope, null, null);
    ifCfg.addNode(ifNode);
    ifCfg.addNodes(assignmentNodeIf);
    ifCfg.addEdge(ifNode, assignmentNodeIf, null);
    ifCfg.addEdge(ifNode, new Node(Token.ELSE, null), null);
    LiveVariablesAnalysis ifAnalysis = new LiveVariablesAnalysis(ifCfg, simpleScope, null);
    assertTrue(ifAnalysis.getEscapedLocals().isEmpty());
    assertEquals(2, ifAnalysis.getEscapedLocals().size());
    VariableAssignmentInIfTest(ifAnalysis, "x", true, true);
    VariableAssignmentInIfTest(ifAnalysis, "y", false, false);

    // Test case for variable assignment in 'for' statement
    Node forNode = new Node(Token.FOR, null);
    Node assignmentNodeForInit = new Node(Token.ASSIGN, null);
    Node bodyNode = new Node(Token.BLOCK, null);
    Node assignmentNodeFor = new Node(Token.ASSIGN, null);
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    ControlFlowGraph forCfg = new ControlFlowGraph(simpleScope, null, null);
    forCfg.addNode(forNode);
    forCfg.addNodes(assignmentNodeForInit, bodyNode, assignmentNodeFor);
    forCfg.addEdge(forNode, assignmentNodeForInit, null);
    forCfg.addEdge(forNode, bodyNode, null);
    forCfg.addEdge(bodyNode, assignmentNodeFor, null);
    LiveVariablesAnalysis forAnalysis = new LiveVariablesAnalysis(forCfg, simpleScope, null);
    assertTrue(forAnalysis.getEscapedLocals().isEmpty());
    assertEquals(1, forAnalysis.getEscapedLocals().size());
    VariableAssignmentInForTest(forAnalysis, "x", true, true);

    // Test case for variable assignment in 'while' statement
    Node whileNode = new Node(Token.WHILE, null);
    Node assignmentNodeWhileInit = new Node(Token.ASSIGN, null);
    Node bodyNodeWhile = new Node(Token.BLOCK, null);
    Node assignmentNodeWhile = new Node(Token.ASSIGN, null);
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    ControlFlowGraph whileCfg = new ControlFlowGraph(simpleScope, null, null);
    whileCfg.addNode(whileNode);
    whileCfg.addNodes(assignmentNodeWhileInit, bodyNodeWhile, assignmentNodeWhile);
    whileCfg.addEdge(whileNode, assignmentNodeWhileInit, null);
    whileCfg.addEdge(assignmentNodeWhileInit, bodyNodeWhile, null);
    whileCfg.addEdge(bodyNodeWhile, assignmentNodeWhile, null);
    LiveVariablesAnalysis whileAnalysis = new LiveVariablesAnalysis(whileCfg, simpleScope, null);
    assertTrue(whileAnalysis.getEscapedLocals().isEmpty());
    assertEquals(1, whileAnalysis.getEscapedLocals().size());
    VariableAssignmentInWhileTest(whileAnalysis, "x", true, true);

    // Test case for variable assignment in 'try' statement
    Node tryNode = new Node(Token.TRY, null);
    Node assignmentNodeTryInit = new Node(Token.ASSIGN, null);
    Node blockNodeTry = new Node(Token.BLOCK, null);
    Node assignmentNodeTry = new Node(Token.ASSIGN, null);
    Node catchNode = new Node(Token.CATCH, null);
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    ControlFlowGraph tryCfg = new ControlFlowGraph(simpleScope, null, null);
    tryCfg.addNode(tryNode);
    tryCfg.addNodes(assignmentNodeTryInit, blockNodeTry, assignmentNodeTry, catchNode);
    tryCfg.addEdge(tryNode, assignmentNodeTryInit, null);
    tryCfg.addEdge(assignmentNodeTryInit, blockNodeTry, null);
    tryCfg.addEdge(blockNodeTry, assignmentNodeTry, null);
    tryCfg.addEdge(assignmentNodeTry, catchNode, null);
    LiveVariablesAnalysis tryAnalysis = new LiveVariablesAnalysis(tryCfg, simpleScope, null);
    assertTrue(tryAnalysis.getEscapedLocals().isEmpty());
    assertEquals(1, tryAnalysis.getEscapedLocals().size());
    VariableAssignmentInTryTest(tryAnalysis, "x", true, true);

    // Test case for variable assignment in 'if' statement with conditional expression
    Node ifNodeCond = new Node(Token.IF, null);
    Node conditionExpNode = new Node(Token.BINARY_OR, null);
    Node assignmentNodeCond = new Node(Token.ASSIGN, null);
    Node trueBlockNodeCond = new Node(Token.BLOCK, null);
    Node falseBlockNodeCond = new Node(Token.BLOCK, null);
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    ControlFlowGraph ifCondCfg = new ControlFlowGraph(simpleScope, null, null);
    ifCondCfg.addNode(ifNodeCond);
    ifCondCfg.addNodes(conditionExpNode, assignmentNodeCond, trueBlockNodeCond, falseBlockNodeCond);
    ifCondCfg.addEdge(ifNodeCond, conditionExpNode, null);
    ifCondCfg.addEdge(conditionExpNode, assignmentNodeCond, null);
    ifCondCfg.addEdge(conditionExpNode, trueBlockNodeCond, null);
    ifCondCfg.addEdge(conditionExpNode, falseBlockNodeCond, null);
    LiveVariablesAnalysis ifCondAnalysis = new LiveVariablesAnalysis(ifCondCfg, simpleScope, null);
    assertTrue(ifCondAnalysis.getEscapedLocals().isEmpty());
    assertEquals(1, ifCondAnalysis.getEscapedLocals().size());
    VariableAssignmentInIfWithConditionalExpressionTest(ifCondAnalysis, "x", true, true);

    // Test case for variable assignment in 'for' statement with loop condition
    Node forNodeCond = new Node(Token.FOR, null);
    Node conditionExpNodeFor = new Node(Token.BINARY_OR, null);
    Node bodyNodeFor = new Node(Token.BLOCK, null);
    Node assignmentNodeForCond = new Node(Token.ASSIGN, null);
    simpleScope.declareVar("x", new Var(simpleScope, "x", Token.VAR_QUALIFIED, 0));
    ControlFlowGraph forCondCfg = new ControlFlowGraph(simpleScope, null, null);
    forCondCfg.addNode(forNodeCond);
    forCondCfg.addNodes(conditionExpNodeFor, bodyNodeFor, assignmentNodeForCond);
    forCondCfg.addEdge(forNodeCond, conditionExpNodeFor, null);
    forCondCfg.addEdge(conditionExpNodeFor, bodyNodeFor, null);
    forCondCfg.addEdge(bodyNodeFor, assignmentNodeForCond, null);
    LiveVariablesAnalysis forCondAnalysis = new LiveVariablesAnalysis(forCondCfg, simpleScope, null);
    assertTrue(forCondAnalysis.getEscapedLocals().isEmpty());
    assertEquals(1, forCondAnalysis.getEscapedLocals().size());
    VariableAssignmentInForWithConditionalExpressionTest(forCondAnalysis, "x", true, true);
  }

  private void SimpleVariableAssignmentTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    assertEquals(expectedLattice, lattice);
  }

  private void VariableAssignmentInIfTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    LiveVariableLattice latticeIf = analysis.flowThrough(analysis.cfg.getNodeByName("ifNode"), lattice);
    assertEquals(expectedLattice, latticeIf);
  }

  private void VariableAssignmentInForTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    LiveVariableLattice latticeFor = analysis.flowThrough(analysis.cfg.getNodeByName("forNode"), lattice);
    assertEquals(expectedLattice, latticeFor);
  }

  private void VariableAssignmentInWhileTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    LiveVariableLattice latticeWhile = analysis.flowThrough(analysis.cfg.getNodeByName("whileNode"), lattice);
    assertEquals(expectedLattice, latticeWhile);
  }

  private void VariableAssignmentInTryTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    LiveVariableLattice latticeTry = analysis.flowThrough(analysis.cfg.getNodeByName("tryNode"), lattice);
    assertEquals(expectedLattice, latticeTry);
  }

  private void VariableAssignmentInIfWithConditionalExpressionTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    LiveVariableLattice latticeIfCond = analysis.flowThrough(analysis.cfg.getNodeByName("ifNodeCond"), lattice);
    assertEquals(expectedLattice, latticeIfCond);
  }

  private void VariableAssignmentInForWithConditionalExpressionTest(LiveVariablesAnalysis analysis,
      String variableName, boolean isLiveIn, boolean isLiveOut) {
    int variableIndex = analysis.getVarIndex(variableName);
    assertTrue(analysis.isForward());
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice expectedLattice = new LiveVariableLattice(analysis.jsScope.getVarCount());
    expectedLattice.liveSet.set(variableIndex);

    LiveVariableLattice lattice = analysis.flowThrough(analysis.cfg.getStartNode(), entryLattice);
    LiveVariableLattice latticeForCond = analysis.flowThrough(analysis.cfg.getNodeByName("forNodeCond"), lattice);
    assertEquals(expectedLattice, latticeForCond);
  }
}
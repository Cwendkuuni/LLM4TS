import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

public class LiveVariablesAnalysisTest {

  @Test
  public void testEmptyProgram() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null);
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(), escapedLocals);
  }

  @Test
  public void testFunctionWithNoAssignments() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null);
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(), escapedLocals);
  }

  @Test
  public void testFunctionWithLocalAssignments() {
    String code = "function foo() { var x = 1; x = 2; }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraphFromTree(Node.parseTree(code));
    Scope jsScope = new Scope(Node.parseTree(code));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(), escapedLocals);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice result = analysis.run(cfg.getEntryNode(), entryLattice);
    assertTrue(result.isLive(jsScope.getVar("x")));
  }

  @Test
  public void testFunctionWithConditionalAssignment() {
    String code = "function foo() { var x = 1; if (true) x = 2; }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraphFromTree(Node.parseTree(code));
    Scope jsScope = new Scope(Node.parseTree(code));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(), escapedLocals);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice result = analysis.run(cfg.getEntryNode(), entryLattice);
    assertTrue(result.isLive(jsScope.getVar("x")));
  }

  @Test
  public void testFunctionWithDeadCode() {
    String code = "function foo() { var x = 1; x = 2; // dead code }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraphFromTree(Node.parseTree(code));
    Scope jsScope = new Scope(Node.parseTree(code));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(), escapedLocals);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice result = analysis.run(cfg.getEntryNode(), entryLattice);
    assertFalse(result.isLive(jsScope.getVar("x")));
  }

  @Test
  public void testFunctionWithGlobalVariable() {
    String code = "var x = 1; function foo() { x = 2; }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraphFromTree(Node.parseTree(code));
    Scope jsScope = new Scope(Node.parseTree(code));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(jsScope.getVar("x")), escapedLocals);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    LiveVariableLattice result = analysis.run(cfg.getEntryNode(), entryLattice);
    assertTrue(result.isLive(jsScope.getVar("x")));
  }

  @Test
  public void testFunctionWithEscapedLocal() {
    String code = "function foo(x) { x = 2; }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraphFromTree(Node.parseTree(code));
    Scope jsScope = new Scope();
    jsScope.setVariable("x", new Var(0, "x", null, false, false));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(jsScope.getVar("x")), escapedLocals);
  }
}
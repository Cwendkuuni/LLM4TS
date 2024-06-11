package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  private static ControlFlowGraph<Node> createCFG(String jsCode) {
    // Initialize compiler and parser with necessary configurations.
    AbstractCompiler compiler = new AbstractCompiler();
    Scope scope = compiler.getRootScope();
    Node root = compiler.parseTree(jsCode, null, null, null);

    return new ControlFlowGraph<>(root, scope);
  }

  @Test
  public void testEmptyCode() {
    ControlFlowGraph<Node> cfg = createCFG("");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    assertThat(result.toString()).isEqualTo("{}");
  }

  @Test
  public void testNoVarAssignment() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() {}");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    assertThat(result.toString()).isEqualTo("{}");
  }

  @Test
  public void testSimpleVarAssignment() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { var x = 1; }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = ImmutableSet.of(0);
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).containsExactly(expectedLiveVars.toArray());
    assertThat(result.toString()).isEqualTo(actualLiveVars.toString());
  }

  @Test
  public void testSimpleVarAssignmentWithInputParameter() {
    ControlFlowGraph<Node> cfg = createCFG("function foo(x) { var y = x; }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = ImmutableSet.of(0, 1);
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).containsExactly(expectedLiveVars.toArray());
    assertThat(result.toString()).isEqualTo(actualLiveVars.toString());
  }

  @Test
  public void testSimpleVarAssignmentWithDeadBranch() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { if (true) { var x = 1; } }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = ImmutableSet.of(0);
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).containsExactly(expectedLiveVars.toArray());
    assertThat(result.toString()).isEqualTo(actualLiveVars.toString());
  }

  @Test
  public void testSimpleVarAssignmentWithException() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { try { var x = 1; } catch (e) {} }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = ImmutableSet.of(0);
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).containsExactly(expectedLiveVars.toArray());
    assertThat(result.toString()).isEqualTo(actualLiveVars.toString());
  }

  @Test
  public void testSimpleVarAssignmentWithConditional() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { if (true) { var x = 1; } }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = Collections.emptySet();
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).isEmpty();
    assertThat(result.toString()).isEqualTo(expectedLiveVars.toString());
  }

  @Test
  public void testComplexVarAssignmentWithConditional() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { var x; if (false) { x = 1; } }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = Collections.emptySet();
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).isEmpty();
    assertThat(result.toString()).isEqualTo(expectedLiveVars.toString());
  }

  @Test
  public void testComplexVarAssignmentWithConditionalAndException() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { try { var x; if (false) { x = 1; } } catch (e) {} }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = Collections.emptySet();
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).isEmpty();
    assertThat(result.toString()).isEqualTo(expectedLiveVars.toString());
  }

  @Test
  public void testComplexVarAssignmentWithConditionalAndDeadBranch() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { var x; if (true) { if (true) { x = 1; } } }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = Collections.emptySet();
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).isEmpty();
    assertThat(result.toString()).isEqualTo(expectedLiveVars.toString());
  }

  @Test
  public void testComplexVarAssignmentWithConditionalAndExceptionAndDeadBranch() {
    ControlFlowGraph<Node> cfg = createCFG("function foo() { try { var x; if (false) { if (true) { x = 1; } } } catch (e) {} }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = Collections.emptySet();
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).isEmpty();
    assertThat(result.toString()).isEqualTo(expectedLiveVars.toString());
  }

  @Test
  public void testComplexVarAssignmentWithConditionalAndExceptionAndDeadBranchAndArguments() {
    ControlFlowGraph<Node> cfg = createCFG("function foo(x) { try { var x; if (false) { if (true) { x = 1; } } } catch (e) {} }");
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = liveAnalysis.computeLattice();
    Set<Integer> expectedLiveVars = Collections.singleton(0);
    Set<Integer> actualLiveVars = Sets.newHashSet();

    for (Var var : liveAnalysis.jsScope.getVars()) {
      if (liveAnalysis.isLive(var)) {
        actualLiveVars.add(var.index);
      }
    }

    assertThat(actualLiveVars).containsExactly(expectedLiveVars.toArray());
    assertThat(result.toString()).isEqualTo(actualLiveVars.toString());
  }
}
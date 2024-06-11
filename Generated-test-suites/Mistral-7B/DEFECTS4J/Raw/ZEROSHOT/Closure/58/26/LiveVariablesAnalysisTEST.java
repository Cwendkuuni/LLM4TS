import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.TestUtils.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.*;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  private static void testLiveVariablesAnalysis(String jsCode) {
    Node rootNode = parseJs(jsCode);
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, new AbstractCompiler(rootNode, scope));
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    assertEquals( Sets.newHashSet(), escapedLocals );

    LiveVariableLattice expectedLattice = new LiveVariableLattice(jsScope(rootNode).getVarCount());
    LiveVariableLattice actualLattice = liveVariablesAnalysis.forwardAnalyze(rootNode, expectedLattice);

    assertEquals(expectedLattice, actualLattice);

    List<Node> nodes = getAllNodesInOrder(rootNode);
    for (Node node : nodes) {
      LiveVariableLattice lattice = liveVariablesAnalysis.flowThrough(node, expectedLattice);
      assertEquals(expectedLattice, lattice);
    }
  }

  @org.junit.Test
  public void testSimpleFunctionWithNoEscapedVariables() {
    String jsCode = "function foo() { var x = 1; var y = 2; }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testSimpleFunctionWithEscapedVariables() {
    String jsCode = "function foo(x) { var y = x; }";
    testLiveVariablesAnalysis(jsCode);

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals( Arrays.asList(jsScope(rootNode).getVar("x")), escapedLocals );
  }

  @org.junit.Test
  public void testComplexFunction() {
    String jsCode = "function foo(x, y) { var z = x + y; if (z > 5) { var a = x; } else { var a = y; } }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testFunctionWithConditionallyExecutedAssignments() {
    String jsCode = "function foo(x, y) { if (x > y) { var a = x; } var z = x + y; }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testFunctionWithForLoop() {
    String jsCode = "function foo(x, y) { for (var i = 0; i < y; i++) { var z = x + i; } }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testFunctionWithForInLoop() {
    String jsCode = "function foo(x, y) { for (var i in y) { var z = x + i; } }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testFunctionWithAssignmentsAndConditionals() {
    String jsCode = "function foo(x, y) { if (x > y) { x += 1; } var z = x + y; }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testFunctionWithAssignmentsAndConditionalsAndLoops() {
    String jsCode = "function foo(x, y) { if (x > y) { x += 1; } for (var i = 0; i < y; i++) { x += i; } }";
    testLiveVariablesAnalysis(jsCode);
  }

  @org.junit.Test
  public void testFunctionWithAssignmentsAndConditionalsAndLoopsAndNestedFunctions() {
    String jsCode = "function foo(x, y) { if (x > y) { x += 1; } for (var i = 0; i < y; i++) { x += i; function bar() { x += 5; } } }";
    testLiveVariablesAnalysis(jsCode);
  }
}

class LiveVariableLattice implements LatticeElement {
  private final BitSet liveSet;

  LiveVariableLattice(int numVars) {
    this.liveSet = new BitSet(numVars);
  }

  LiveVariableLattice(LiveVariableLattice other) {
    this.liveSet = (BitSet) other.liveSet.clone();
  }

  @Override
  public boolean equals(Object other) {
    Preconditions.checkNotNull(other);
    return (other instanceof LiveVariableLattice) &&
        this.liveSet.equals(((LiveVariableLattice) other).liveSet);
  }

  public boolean isLive(Var v) {
    Preconditions.checkNotNull(v);
    return liveSet.get(v.index);
  }

  public boolean isLive(int index) {
    return liveSet.get(index);
  }

  @Override
  public String toString() {
    return liveSet.toString();
  }

  @Override
  public int hashCode() {
    return liveSet.hashCode();
  }
}
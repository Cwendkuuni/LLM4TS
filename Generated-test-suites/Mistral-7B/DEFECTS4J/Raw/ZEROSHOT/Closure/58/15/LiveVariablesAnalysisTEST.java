import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  private Node rootNode;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;

  @Before
  public void setUp() {
    String jsCode = "function foo(a, b) {" +
        "  var x = a + b;" +
        "  var y = 1;" +
        "  if (x > 5) {" +
        "    x = 10;" +
        "  } else {" +
        "    y = x + 1;" +
        "  }" +
        "  return x + y;" +
        "}";

    scope = new Scope();
    scope.enterScope();
    scope.declareVar("a", Node.INT_TYPE, null);
    scope.declareVar("b", Node.INT_TYPE, null);
    scope.declareVar("x", Node.INT_TYPE, null);
    scope.declareVar("y", Node.INT_TYPE, null);
    rootNode = ParseUtil.parseFunction(jsCode, scope).getBody();

    controlFlowGraph = new ControlFlowGraph<Node>(rootNode, scope, null);
  }

  @Test
  public void testLiveVariablesAnalysis() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeValue(rootNode, entryLattice);

    assertNotNull(result);

    Set<String> expectedLiveVariables = new HashSet<>(Arrays.asList("a", "b", "x", "y"));
    for (Var variable : scope.getVars()) {
      String variableName = variable.getName();
      if (expectedLiveVariables.contains(variableName)) {
        assertTrue(analysis.isLive(variable.getIndex()));
      } else {
        assertFalse(analysis.isLive(variable.getIndex()));
      }
    }
  }

  @Test
  public void testEscapedVariables() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, scope, null);
    Set<Var> escapedVariables = analysis.getEscapedLocals();

    assertNull(escapedVariables);

    String jsCode = "function bar() {" +
        "  function baz() {" +
        "    var a = 1;" +
        "  }" +
        "  baz();" +
        "  return a;" +
        "}";

    scope = new Scope();
    scope.enterScope();
    rootNode = ParseUtil.parseFunction(jsCode, scope).getBody();
    controlFlowGraph = new ControlFlowGraph<Node>(rootNode, scope, null);
    analysis = new LiveVariablesAnalysis(controlFlowGraph, scope, null);

    escapedVariables = analysis.getEscapedLocals();

    assertNotNull(escapedVariables);
    assertEquals(1, escapedVariables.size());
    Var escapedVariable = scope.getVar("a");
    assertTrue(escapedVariables.contains(escapedVariable));
  }
}
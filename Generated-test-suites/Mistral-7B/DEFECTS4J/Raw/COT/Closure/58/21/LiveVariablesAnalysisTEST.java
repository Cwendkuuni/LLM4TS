import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.Token.*;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Given a simple scope with some local variables
    Scope scope = new Scope();
    scope.putVar("x", createVar("x", 0));
    scope.putVar("y", createVar("y", 1));
    scope.putVar("z", createVar("z", 2));

    // LiveVariablesAnalysis under test
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, scope, null);

    // When computing liveness for an empty program
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertTrue(result.isLive("x"));
    assertTrue(result.isLive("y"));
    assertTrue(result.isLive("z"));
    assertTrue(escapedLocals.isEmpty());

    // Given a simple assignment statement
    Node node = NodeFactory.createAssign(ASSIGN, createName("x"),
        NodeFactory.createName("y"));

    // When computing liveness for an assignment statement
    result = analysis.flowThrough(node, analysis.getInitialEstimateLattice());
    assertEquals(Arrays.asList("y"), Arrays.asList(result.liveSet.toArray()));

    // Given a simple conditional statement
    node = NodeFactory.createIf(IF, NodeFactory.createBooleanLiteral(true), NodeFactory.createName("x"), NodeFactory.createName("y"));

    // When computing liveness for a conditional statement
    result = analysis.flowThrough(node, analysis.getInitialEstimateLattice());
    assertTrue(result.isLive("x"));
    assertTrue(result.isLive("y"));

    // Given a simple loop statement
    node = NodeFactory.createWhile(WHILE, NodeFactory.createBooleanLiteral(true), NodeFactory.createName("x"), NodeFactory.createName("y"));

    // When computing liveness for a loop statement
    result = analysis.flowThrough(node, analysis.getInitialEstimateLattice());
    assertTrue(result.isLive("x"));
    assertTrue(result.isLive("y"));

    // Given a simple for loop statement
    node = NodeFactory.createFor(FOR, createName("i"), NodeFactory.createName("j"), NodeFactory.createBlock(BLOCK, null, null), null, null, null, null);

    // When computing liveness for a for loop statement
    result = analysis.flowThrough(node, analysis.getInitialEstimateLattice());
    assertTrue(result.isLive("i"));
    assertTrue(result.isLive("j"));

    // Given a simple function with a local variable and an escaped variable
    Node functionNode = NodeFactory.createFunction(FUNCTION, "f", Arrays.asList("x"), NodeFactory.createBlock(BLOCK, null, null), null);
    Var escapedVar = scope.putVar("escapeVar", createVar("escapeVar", 3));

    // When computing liveness for a function
    analysis = new LiveVariablesAnalysis(null, scope, null);
    result = analysis.computeFunctionLattice(functionNode);
    assertTrue(result.isLive("x"));
    assertTrue(analysis.getEscapedLocals().contains(escapedVar));
  }

  @Test(expected = AssertionError.class)
  public void testNonForwardDataFlowAnalysis() {
    // This test case should fail as LiveVariablesAnalysis is not a forward data flow analysis.
    new LiveVariablesAnalysis(null, null, null).computeLattice(null);
  }
}
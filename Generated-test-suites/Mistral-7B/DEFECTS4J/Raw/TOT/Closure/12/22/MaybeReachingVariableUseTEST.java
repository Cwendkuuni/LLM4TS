import static org.junit.Assert.*;
import static com.google.javascript.jscomp.graph.NodeUtil.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {
  private MaybeReachingVariableUse analysis;
  private ControlFlowGraph<Node> graph;
  private Scope scope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    String code = "function foo() { var x = 1; var y = 2; if (x > y) { x += 1; } }";
    scope = new Scope(new Node(Token.BLOCK, null, null, null, null, null, null, null, null, code));
    graph = new ControlFlowGraph<>(scope);
    compiler = new AbstractCompiler(null, null, null, null, null, null, null, null);
    analysis = new MaybeReachingVariableUse(graph, scope, compiler);
  }

  @Test
  public void testSimpleAssignment() {
    analysis.computeMayReach();
    Node defNode = graph.getNode(NodeUtil.getFirstDefNode(graph, "x"));
    ImmutableSet<Node> uses = analysis.getUses("x", defNode);
    assertTrue(uses.contains(getFirstDefNode(graph, "x")));
    assertTrue(uses.contains(NodeUtil.getConditionExpression(getFirstDefNode(graph, "if"))));
    assertTrue(uses.contains(getLastDefNode(graph, "x")));
  }

  @Test
  public void testVariableInCondition() {
    analysis.computeMayReach();
    Node condNode = getFirstDefNode(graph, "if");
    ImmutableSet<Node> uses = analysis.getUses("x", condNode);
    assertTrue(uses.contains(getFirstDefNode(graph, "x")));
    assertTrue(uses.contains(getLastDefNode(graph, "x")));
  }

  @Test
  public void testVariableInAssignment() {
    analysis.computeMayReach();
    Node assignNode = NodeUtil.getAssignmentNode(getFirstDefNode(graph, "x"), "x", 1);
    ImmutableSet<Node> uses = analysis.getUses("x", assignNode);
    assertTrue(uses.contains(getFirstDefNode(graph, "x")));
    assertTrue(uses.contains(assignNode));
    assertTrue(uses.contains(getLastDefNode(graph, "x")));
  }

  @Test
  public void testNoUses() {
    analysis.computeMayReach();
    ImmutableSet<Node> uses = analysis.getUses("y", getFirstDefNode(graph, "x"));
    assertEmpty(uses);
  }

  @Test(expected = AssertionError.class)
  public void testNullScope() {
    analysis = new MaybeReachingVariableUse(null, null, null);
  }

  @Test(expected = AssertionError.class)
  public void testNullControlFlowGraph() {
    analysis = new MaybeReachingVariableUse(null, scope, null);
  }

  @Test(expected = AssertionError.class)
  public void testNullCompiler() {
    analysis = new MaybeReachingVariableUse(graph, null, null);
  }
}
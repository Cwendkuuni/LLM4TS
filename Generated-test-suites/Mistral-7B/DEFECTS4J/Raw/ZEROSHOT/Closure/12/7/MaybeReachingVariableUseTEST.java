import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNodeWithId;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    // Initialize the test data
    Node node1 = Node.newNode(Token.BLOCK, null);
    Node node2 = Node.newNode(Token.IF, null);
    Node node3 = Node.newNode(Token.NAME, "x", null);
    Node node4 = Node.newNode(Token.NAME, "y", null);
    Node node5 = Node.newNode(Token.NAME, "z", null);
    Node node6 = Node.newNode(Token.ASSIGN, null);
    Node node7 = Node.newNode(Token.ASSIGN, null);
    Node node8 = Node.newNode(Token.NAME, "x", null);
    Node node9 = Node.newNode(Token.NAME, "x", null);
    Node node10 = Node.newNode(Token.NAME, "x", null);

    // Build the control-flow graph
    cfg = new ControlFlowGraph<>();
    GraphNode<Node, Branch> n1 = cfg.addNode(node1);
    GraphNode<Node, Branch> n2_true = cfg.addNode(node2, true);
    GraphNode<Node, Branch> n2_false = cfg.addNode(node2, false);
    cfg.addEdge(new DiGraphEdge<>(n1, n2_true, null));
    cfg.addEdge(new DiGraphEdge<>(n1, n2_false, null));
    cfg.addEdge(new DiGraphEdge<>(n2_true, n3, null));
    cfg.addEdge(new DiGraphEdge<>(n3, n4, null));
    cfg.addEdge(new DiGraphEdge<>(n2_false, n5, null));
    cfg.addEdge(new DiGraphEdge<>(n5, n6, null));
    cfg.addEdge(new DiGraphEdge<>(n6, n7, null));
    cfg.addEdge(new DiGraphEdge<>(n7, n8, null));
    cfg.addEdge(new DiGraphEdge<>(n7, n9, null));
    cfg.addEdge(new DiGraphEdge<>(n7, n10, null));
    cfg.addEdge(new DiGraphEdge<>(n8, n1, null));
    cfg.addEdge(new DiGraphEdge<>(n9, n1, null));
    cfg.addEdge(new DiGraphEdge<>(n10, n1, null));

    // Build the JavaScript scope
    jsScope = new Scope();
    Var xVar = jsScope.addVar("x", null, null, null, false, null, null);
    Var yVar = jsScope.addVar("y", null, null, null, false, null, null);
    Var zVar = jsScope.addVar("z", null, null, null, false, null, null);

    // Initialize the compiler
    compiler = new AbstractCompiler() {
      @Override
      public ControlFlowGraph<Node> getControlFlowGraph() {
        return cfg;
      }

      @Override
      public Scope getCurrentScope() {
        return jsScope;
      }
    };

    // Initialize the MaybeReachingVariableUse analyzer
    MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);
  }

  @Test
  public void testGetUses_xIsDefinedAtNode7_ReturnsNodes3And4() {
    Set<Node> expectedUses = Sets.newHashSet(node3, node4);
    Set<Node> actualUses = analyzer.getUses("x", node7);
    assertEquals(expectedUses, actualUses);
  }

  @Test
  public void testGetUses_xIsDefinedAtNode10_ReturnsEmptySet() {
    Set<Node> actualUses = analyzer.getUses("x", node10);
    assertTrue(actualUses.isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetUses_InvalidVariableName_ThrowsException() {
    analyzer.getUses("invalidVariableName", node7);
  }
}
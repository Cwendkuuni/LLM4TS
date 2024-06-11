import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private static final Logger logger = Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = createTestNode();
    elimination.process(null, root);
    assertUnreachableCodeRemoved(root);

    root = createTestNodeWithReachableDeadCode();
    elimination.process(null, root);
    assertNoDeadCodeRemoved(root);

    root = createTestNodeWithUnconditionalBranching();
    elimination.process(null, root);
    assertUnconditionalBranchingRemoved(root);
  }

  private Node createTestNode() {
    Node root = new Node("function() { return; alert('unreachable'); }");
    root.setLine(1);
    root.setColumn(1);
    return root;
  }

  private Node createTestNodeWithReachableDeadCode() {
    Node root = new Node("function() { if (false) { alert('dead'); } }");
    root.setLine(1);
    root.setColumn(1);
    return root;
  }

  private Node createTestNodeWithUnconditionalBranching() {
    Node root = new Node("function() { switch (1) { case 1: break; case 2: break; } }");
    root.setLine(1);
    root.setColumn(1);
    return root;
  }

  private void assertUnreachableCodeRemoved(Node root) {
    assertTrue("Unreachable code was not removed", root.getLastChild().isNull());
    logger.fine("Test passed: Unreachable code was removed");
  }

  private void assertNoDeadCodeRemoved(Node root) {
    assertFalse("Dead code was removed unnecessarily", root.getLastChild().isNull());
    logger.fine("Test passed: No dead code was removed");
  }

  private void assertUnconditionalBranchingRemoved(Node root) {
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    List<DiGraphEdge<Node, Branch>> outEdges = cfg.getDirectedGraphNode(root.getLastChild())
        .getOutEdges();
    assertTrue("Unconditional branching was not removed", outEdges.isEmpty());
    logger.fine("Test passed: Unconditional branching was removed");
  }
}
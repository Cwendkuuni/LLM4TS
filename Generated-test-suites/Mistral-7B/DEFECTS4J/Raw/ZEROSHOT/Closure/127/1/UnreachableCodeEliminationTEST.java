import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.ControlFlowGraph.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph.DiGraphEdge;
import com.google.javascript.jscomp.ControlFlowGraph.DiGraphNode;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.NodeTraversal.EliminationPass;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private static final Logger logger = Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    compiler = new AbstractCompiler() {};
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, false);

    Node root = createNode("function f() {" +
                          "  if (true) {" +
                          "    return;" +
                          "  }" +
                          "  alert('unreachable');" +
                          "}");

    uce.process(null, root);

    // Check that the alert statement was removed.
    assertTrue(NodeUtil.isNull(root.getLastChild()));

    // Check that the control flow graph is unchanged.
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    DiGraphNode<Node, Branch> startNode = cfg.getEntry();
    DiGraphNode<Node, Branch> endNode = startNode.getSuccessor();
    List<DiGraphEdge<Node, Branch>> edges = startNode.getOutEdges();
    assertTrue(edges.size() == 1);
    assertTrue(edges.get(0).getValue() == Branch.UNCOND);
    assertEquals(endNode, edges.get(0).getDestination());

    // Check that no op statements are removed.
    root = createNode("function f() {" +
                     "  a.b.MyClass.prototype.propertyName;" +
                     "  true;" +
                     "}");
    uce.process(null, root);
    assertTrue(NodeUtil.isTrue(root.getLastChild()));
  }

  private Node createNode(String code) {
    return NodeUtil.createCompilationUnitNode(code);
  }

  private void assertEquals(Node expected, Node actual) {
    String expectedCode = NodeUtil.printCompilationUnit(expected);
    String actualCode = NodeUtil.printCompilationUnit(actual);
    assertEquals(expectedCode, actualCode);
  }
}

class EliminationPassTest extends AbstractShallowCallback {
  private final ControlFlowGraph<Node> cfg;
  private EliminationPassTest(ControlFlowGraph<Node> cfg) {
    this.cfg = cfg;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (parent == null || n.isFunction() || n.isScript()) {
      return;
    }
    DiGraphNode<Node, Branch> gNode = cfg.getDirectedGraphNode(n);
    if (gNode == null) {
      return;
    }
    if (gNode.getAnnotation() != GraphReachability.REACHABLE) {
      fail("Node " + n.toString() + " is unreachable.");
    }
    try {
      super.visit(t, n, parent);
    } catch (AssertionError e) {
      fail("Unexpected assertion error: " + e.getMessage());
    }
  }
}
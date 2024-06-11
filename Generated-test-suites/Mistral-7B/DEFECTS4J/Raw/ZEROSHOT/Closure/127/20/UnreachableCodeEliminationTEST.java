import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.AbstractShallowCallbackType.TRAVERSE;
import static com.google.javascript.rhino.Node.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

import java.util.List;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    String sourceCode =
        "function foo() {\n"
            + "if (true) {\n"
            + " return;\n"
            + " alert('First unreachable code');\n"
            + "}\n"
            + "if (false) {\n"
            + " return;\n"
            + " alert('Second unreachable code');\n"
            + "}\n"
            + "alert('Last reachable code');\n"
            + "}\n";

    CompilerOptions options = new CompilerOptions(true, null);
    Compiler compiler = new Compiler();
    compiler.init(options);

    SourceFile input = new SourceFile("Test.js", sourceCode, 0);
    Node externs = input.getExterns();
    Node toplevel = input.getTree();

    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    ControlFlowGraph<Node> cfg = cfa.getCfg();

    DiGraph<Node, DiGraphEdge<Node, Branch>> graph = cfg.getGraph();

    DiGraphNode<Node, Branch> entryNode = graph.getEntry();
    assertNotNull(entryNode);

    DiGraphNode<Node, Branch> firstUnreachableNode =
        graph.getNode(getIdentifier("foo").getParent());
    assertNotNull(firstUnreachableNode);

    DiGraphNode<Node, Branch> secondUnreachableNode =
        graph.getNode(getIdentifier("foo").getParent().getNext());
    assertNotNull(secondUnreachableNode);

    DiGraphNode<Node, Branch> lastReachableNode =
        graph.getNode(getIdentifier("Last reachable code").getParent());
    assertNotNull(lastReachableNode);

    List<DiGraphEdge<Node, Branch>> edges = Lists.newArrayList();

    edges.addAll(getEdges(firstUnreachableNode, graph));
    edges.addAll(getEdges(secondUnreachableNode, graph));
    edges.addAll(getEdges(lastReachableNode, graph));

    List<DiGraphNode<Node, Branch>> reachableNodes =
        GraphReachability.getReachableNodes(graph, entryNode);

    assertEquals(3, reachableNodes.size());
    assertTrue(reachableNodes.contains(lastReachableNode));
    assertFalse(reachableNodes.contains(firstUnreachableNode));
    assertFalse(reachableNodes.contains(secondUnreachableNode));

    assertEquals(3, edges.size());
    assertEquals(Branch.UNCOND, edges.get(0).getValue().getCondition());
    assertEquals(Branch.UNCOND, edges.get(1).getValue().getCondition());
    assertEquals(Branch.UNCOND, edges.get(2).getValue().getCondition());

    NodeTraversal.traverse(compiler, toplevel, new NodeTraversal.AbstractShallowCallback() {
      @Override
      public void visit(NodeTraversal nt, Node node, Node parent) {
        if (node.isScript() || node.isFunction()) {
          if (node.getLastChild() != null && node.getLastChild().isReturn()) {
            int numAlerts = 0;
            Node currentNode = node.getLastChild();
            while (currentNode != null) {
              if (currentNode.isAlert()) {
                numAlerts++;
              }
              currentNode = currentNode.getNext();
            }
            assertEquals(0, numAlerts);
          }
        }
      }
    }, TRAVERSE);
  }

  private List<DiGraphEdge<Node, Branch>> getEdges(DiGraphNode<Node, Branch> node, DiGraph<Node, DiGraphEdge<Node, Branch>> graph) {
    List<DiGraphEdge<Node, Branch>> edges = Lists.newArrayList();
    for (DiGraphEdge<Node, Branch> edge : node.getOutEdges()) {
      edges.add(edge);
    }
    return edges;
  }

  private Node getIdentifier(String identifier) {
    return new Identifier("Test", identifier);
  }
}
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;

import java.util.Arrays;
import java.util.List;

public class UnreachableCodeEliminationTest {

  @SuppressWarnings("unchecked")
  private static <T> DiGraphNode<AbstractNode, Branch> createGraphNode(T node) {
    return new DiGraphNode<AbstractNode, Branch>() {
      @Override
      public AbstractNode getValue() {
        return (AbstractNode) node;
      }

      @Override
      public List<DiGraphEdge<AbstractNode, Branch>> getOutEdges() {
        return Arrays.asList(new DiGraphEdge<AbstractNode, Branch>(null, Branch.UNCOND));
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
          return false;
        }
        return getValue().equals(((DiGraphNode<AbstractNode, Branch>) obj).getValue());
      }

      @Override
      public int hashCode() {
        return getValue().hashCode();
      }
    };
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void initializeControlFlowGraph(ControlFlowGraph<Node> cfg) {
    cfg.setEntry(createGraphNode(new Node()));
  }

  private static void initializeDiGraph(DiGraph<Node, DiGraphEdge> diGraph) {
    diGraph.setDirected(true);
    diGraph.setWeighted(false);
  }

  private static void initializeGraphReachability(GraphReachability<Node, ControlFlowGraph.Branch> reachability) {
    reachability.setReachable(createGraphNode(new Node()), true);
  }

  @SuppressWarnings("unchecked")
  private static <T> Node createNode(Class<T> nodeClass) {
    try {
      return (Node) nodeClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private static void testUnreachableCodeElimination(boolean removeNoOpStatements) {
    AbstractCompiler compiler = new AbstractCompiler();
    initializeControlFlowGraph(compiler.getCFG());
    initializeDiGraph((DiGraph<Node, DiGraphEdge>) compiler.getGraph());
    initializeGraphReachability(compiler.getGraphReachability());

    Node root = new Node();
    root.setIsFunction(true);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal traversal, Node n) {
        new UnreachableCodeElimination(compiler, removeNoOpStatements).process(null, root);
      }
    });

    DiGraphNode<Node, Branch> rootNode = compiler.getGraph().getDirectedGraphNode(root);
    assertTrue(rootNode.getAnnotation() == GraphReachability.REACHABLE);

    Node n1 = createNode(Node.class);
    Node n2 = createNode(Node.class);
    Node n3 = createNode(Node.class);

    NodeTraversal.addChild(root, n1);
    NodeTraversal.addChild(n1, n2);
    NodeTraversal.addChild(n1, n3);

    DiGraphNode<Node, Branch> n1Node = compiler.getGraph().getDirectedGraphNode(n1);
    DiGraphNode<Node, Branch> n2Node = compiler.getGraph().getDirectedGraphNode(n2);
    DiGraphNode<Node, Branch> n3Node = compiler.getGraph().getDirectedGraphNode(n3);

    if (removeNoOpStatements) {
      // NoOp statement should be removed.
      Node noOp = createNode(Token.NUMBER.toString());
      NodeTraversal.addChild(n2, noOp);
      assertFalse(n2Node.getAnnotation() == GraphReachability.REACHABLE);
    } else {
      // NoOp statement should remain.
      assertFalse(n2Node.getAnnotation() == GraphReachability.UNREACHABLE);
    }

    if (removeNoOpStatements) {
      // Return statement with noOp statement should be removed.
      Node returnNode = createNode(Token.RETURN.toString());
      NodeTraversal.addChild(n3, noOp);
      NodeTraversal.addChild(n3, returnNode);

      DiGraphNode<Node, Branch> returnNodeGraphNode = compiler.getGraph().getDirectedGraphNode(returnNode);
      assertFalse(returnNodeGraphNode.getAnnotation() == GraphReachability.REACHABLE);
    } else {
      // Return statement with noOp statement should remain.
      assertFalse(n3Node.getAnnotation() == GraphReachability.UNREACHABLE);
    }

    if (!removeNoOpStatements) {
      // Dead code after return statement should be removed.
      Node deadCode = createNode(Token.ALERT.toString());
      NodeTraversal.addChild(n3, deadCode);

      DiGraphNode<Node, Branch> deadCodeGraphNode = compiler.getGraph().getDirectedGraphNode(deadCode);
      assertFalse(deadCodeGraphNode.getAnnotation() == GraphReachability.REACHABLE);
    }

    // Test for unreachable code elimination in a function.
    NodeTraversal.addChild(root, new Node());
    Node functionNode = root.getLastChild();
    Node functionRoot = new Node();

    NodeTraversal.setCurrent(root);
    NodeTraversal.traverse(compiler, functionNode, new NodeTraversal.FunctionVisitor() {
      @Override
      public void visit(NodeTraversal t, AbstractCompiler compiler, Node n) {
        NodeTraversal.setCurrent(functionRoot);
        NodeTraversal.traverse(compiler, n, new EliminationPassFunctionTest(compiler));
      }
    });

    DiGraphNode<Node, Branch> functionRootNode = compiler.getGraph().getDirectedGraphNode(functionRoot);
    assertTrue(functionRootNode.getAnnotation() == GraphReachability.REACHABLE);

    Node functionReturn = new Node();
    functionReturn.setIsFunction(false);
    functionReturn.setType(Token.RETURN.toString());
    NodeTraversal.addChild(functionRoot, functionReturn);

    DiGraphNode<Node, Branch> functionReturnNode = compiler.getGraph().getDirectedGraphNode(functionReturn);
    assertTrue(functionReturnNode.getAnnotation() == GraphReachability.REACHABLE);

    Node functionDeadCode = createNode(Token.ALERT.toString());
    NodeTraversal.addChild(functionRoot, functionDeadCode);

    DiGraphNode<Node, Branch> functionDeadCodeNode = compiler.getGraph().getDirectedGraphNode(functionDeadCode);
    assertFalse(functionDeadCodeNode.getAnnotation() == GraphReachability.REACHABLE);
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static class EliminationPassFunctionTest extends AbstractShallowCallback {
    private final ControlFlowGraph<Node> cfg;

    EliminationPassFunctionTest(ControlFlowGraph<Node> cfg) {
      this.cfg = cfg;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      DiGraphNode<Node, Branch> gNode = cfg.getDirectedGraphNode(n);

      if (gNode == null || gNode.getAnnotation() == GraphReachability.UNREACHABLE) {
        return;
      }

      if (gNode.getValue() instanceof Node) {
        tryRemoveUnconditionalBranching((Node) gNode.getValue());
      }
    }

    private void tryRemoveUnconditionalBranching(Node n) {
      List<DiGraphEdge<Node, Branch>> outEdges = n.getOutgoingEdges();

      if (outEdges.isEmpty()) {
        return;
      }

      DiGraphEdge<Node, Branch> edge = outEdges.get(0);
      Branch branchType = edge.getValue();

      if (branchType != Branch.UNCOND) {
        return;
      }

      Node target = edge.getDestination();
      DiGraphNode<Node, Branch> targetNode = cfg.getDirectedGraphNode(target);

      if (targetNode == null || targetNode.getAnnotation() == GraphReachability.UNREACHABLE) {
        removeDeadExprStatementSafely(n);
      }
    }

    private void removeDeadExprStatementSafely(Node n) {
      Node parent = n.getParent();

      if (parent == null) {
        return;
      }

      if (n.isEmpty() || (n.isBlock() && n.getNumChildren() == 0)) {
        return;
      }

      if (n.isVar() && n.getFirstChild().isEmpty()) {
        return;
      }

      switch (n.getType()) {
        case Token.DO:
          return;
        case Token.BLOCK:
          if (parent.isTry()) {
            NodeUtil.maybeAddFinally(parent);
            return;
          }
          break;
        case Token.CATCH:
          Node tryNode = parent.getParent();
          NodeUtil.maybeAddFinally(tryNode);
          break;
      }

      removeNode(n);
    }

    private void removeNode(Node n) {
      NodeUtil.redeclareVarsInsideBranch(n);
      compiler.reportCodeChange();
      NodeUtil.removeChild(parent, n);
    }
  }

  @org.junit.Test
  public void testUnreachableCodeEliminationWithNoOpStatements() {
    testUnreachableCodeElimination(true);
  }

  @org.junit.Test
  public void testUnreachableCodeEliminationWithoutNoOpStatements() {
    testUnreachableCodeElimination(false);
  }
}
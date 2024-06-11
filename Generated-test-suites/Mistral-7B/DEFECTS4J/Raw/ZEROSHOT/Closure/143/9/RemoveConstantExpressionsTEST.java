import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private Result result;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
    result = new Result();
  }

  @org.junit.Test
  public void testRemoveConstantExpressions() {
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    Node root = new Node(Token.PROGRAM, null);

    Node expr1 = new Node(Token.EXPR_RESULT, null);
    expr1.setFirstChild(new Node(Token.PLUS, null,
        new Node(Token.NUMBER, 1),
        new Node(Token.IDENT, "foo"),
        new Node(Token.PLUS, null,
            new Node(Token.IDENT, "bar")))
    );

    Node expr2 = new Node(Token.EXPR_RESULT, null);
    expr2.setFirstChild(new Node(Token.IDENT, "foo"));

    expr1.setParent(root);
    expr2.setParent(root);

    pass.process(null, root);

    List<Node> sideEffectNodes1 = Lists.newArrayList(expr2);
    List<Node> sideEffectNodes2 = Lists.newArrayList(new Node(Token.IDENT, "foo"), expr2);

    List<Node> simplifiedNodes1 = Lists.newArrayList(expr2);
    List<Node> simplifiedNodes2 = Lists.newArrayList(expr2, new Node(Token.IDENT, "bar"));

    assertTrue(result.changed);
    NodeTraversal.traverse(null, root, new NodeChecker(sideEffectNodes1, simplifiedNodes1));

    result.changed = false;
    result.reset();

    NodeTraversal.traverse(null, root, new NodeChecker(sideEffectNodes2, simplifiedNodes2));
    assertTrue(result.changed);
  }

  private static class NodeChecker extends AbstractPostOrderCallback {
    private final List<Node> expectedSideEffectNodes;
    private final List<Node> expectedSimplifiedNodes;

    private NodeChecker(List<Node> expectedSideEffectNodes, List<Node> expectedSimplifiedNodes) {
      this.expectedSideEffectNodes = expectedSideEffectNodes;
      this.expectedSimplifiedNodes = expectedSimplifiedNodes;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (node.getType() == Token.EXPR_RESULT && node.getParent().getType() == Token.EXPR_STATEMENT) {
        List<Node> sideEffectNodes = Lists.newArrayList();
        List<Node> simplifiedNodes = Lists.newArrayList();

        gatherSideEffectNodes(node, sideEffectNodes);
        gatherSimplifiedNodes(node, simplifiedNodes);

        assertEquals("Unexpected number of side effect nodes", expectedSideEffectNodes.size(), sideEffectNodes.size());
        for (int i = 0; i < expectedSideEffectNodes.size(); ++i) {
          assertEquals("Unexpected side effect node", expectedSideEffectNodes.get(i), sideEffectNodes.get(i));
        }

        assertEquals("Unexpected number of simplified nodes", expectedSimplifiedNodes.size(), simplifiedNodes.size());
        for (int i = 0; i < expectedSimplifiedNodes.size(); ++i) {
          assertEquals("Unexpected simplified node", expectedSimplifiedNodes.get(i), simplifiedNodes.get(i));
        }
      }
    }

    private void gatherSideEffectNodes(Node node, List<Node> nodes) {
      NodeTraversal.traverse(null, node, new GatherSideEffectSubexpressionsCallback(null, nodes));
    }

    private void gatherSimplifiedNodes(Node node, List<Node> nodes) {
      NodeTraversal.traverse(null, node, new NodeCheckerGatherer(null, nodes));
    }

    private static class NodeCheckerGatherer extends AbstractPostOrderCallback {
      private final List<Node> nodes;

      NodeCheckerGatherer(List<Node> nodes, List<Node> nodes) {
        this.nodes = nodes;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.EXPR_RESULT && node.getParent().getType() != Token.EXPR_RESULT) {
          nodes.add(node);
        }
      }
    }

    private static void assertEquals(String message, List<Node> expected, List<Node> actual) {
      if (!expected.equals(actual)) {
        throw new AssertionError(String.format("%s: expected %s, but was %s", message, expected, actual));
      }
    }
  }
}

class GatherSideEffectSubexpressionsCallback extends AbstractPostOrderCallback {
  private final List<Node> nodes;
  private final GatherSideEffectSubexpressionsCallback copy;

  GatherSideEffectSubexpressionsCallback(List<Node> nodes, GatherSideEffectSubexpressionsCallback copy) {
    this.nodes = nodes;
    this.copy = copy != null ? copy : this;
  }

  @Override
  public void visit(NodeTraversal traversal, Node node, Node parent) {
    if (node.getType() != Token.EXPR_RESULT) {
      return;
    }

    if (NodeUtil.isSideEffectExpression(node)) {
      nodes.add(node);
    }

    copy.visit(traversal, node, parent);
  }
}
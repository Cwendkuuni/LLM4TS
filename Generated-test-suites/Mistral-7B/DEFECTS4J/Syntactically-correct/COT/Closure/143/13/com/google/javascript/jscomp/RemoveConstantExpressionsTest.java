import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;
  private RemoveConstantRValuesCallback removeConstantRValuesCallback;
  private Result result;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantRValuesCallback =
        new RemoveConstantRValuesCallback();
    result = new Result();
  }

  @Test
  public void testProcessMethod() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    removeConstantExpressions.process(null, root);
    assertNotNull(result);
  }

  @Test
  public void testTrySimplifyMethodWithNoSideEffectsExpression() {
    Node parent = new Node(Token.EXPR_STATEMENT, Lists.newArrayList());
    Node node = new Node(Token.EXPR_RESULT, null);
    removeConstantExpressions.trySimplify(parent, node);
    assertNotNull(node);
  }

  @Test
  public void testTrySimplifyMethodWithSideEffectsExpression() {
    Node parent = new Node(Token.EXPR_STATEMENT, Lists.newArrayList());
    Node node = new Node(Token.CALL_EXPRESSION, Lists.newArrayList(
        new Node(Token.IDENTIFIER, "foo"),
        new Node(Token.DOT, null),
        new Node(Token.IDENTIFIER, "bar")
    ));
    removeConstantExpressions.trySimplify(parent, node);
    assertNotNull(parent.getFirstChild());
    assertNotNull(parent.getFirstChild().getNext());
  }

  @Test
  public void testGetSideEffectNodesMethod() {
    Node node = new Node(Token.BINARY_EXPRESSION, Lists.newArrayList(
        new Node(Token.IDENTIFIER, "a"),
        new Node(Token.PLUS, null),
        new Node(Token.IDENTIFIER, "b")
    ));
    List<Node> sideEffectNodes = removeConstantRValuesCallback.getSideEffectNodes(node);
    assertNotNull(sideEffectNodes);
    assertNotNull(sideEffectNodes.get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSideEffectNodesMethodWithNullNode() {
    removeConstantRValuesCallback.getSideEffectNodes(null);
  }

  @Test
  public void testProcessMethodWithSideEffectSubexpressions() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    Node parent = new Node(Token.EXPR_STATEMENT, Lists.newArrayList());
    Node node = new Node(Token.BINARY_EXPRESSION, Lists.newArrayList(
        new Node(Token.IDENTIFIER, "a"),
        new Node(Token.PLUS, null),
        new Node(Token.IDENTIFIER, "b")
    ));
    parent.addChildToEnd(node);
    root.addChildToEnd(parent);

    removeConstantExpressions.process(null, root);

    Node expectedParent = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.IDENTIFIER, "a")
    ));
    Node expectedNodeA = new Node(Token.IDENTIFIER, "a");
    Node expectedNodeB = new Node(Token.IDENTIFIER, "b");

    Node actualParent = parent;
    Node actualNodeA = null;
    Node actualNodeB = null;

    NodeTraversal.traverse(null, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.EXPR_STATEMENT) {
          actualParent = node;
        } else if (node.getType() == Token.IDENTIFIER) {
          if (actualParent != null) {
            if (actualParent.equals(parent)) {
              if (node.getData() == 'a') {
                actualNodeA = node;
              } else if (node.getData() == 'b') {
                actualNodeB = node;
              }
            }
          }
        }
      }
    });

    assertNotNull(actualParent);
    assertNotNull(actualNodeA);
    assertNotNull(actualNodeB);
    assertNotNull(actualParent.getFirstChild());
    assertSame(actualNodeA, actualParent.getFirstChild());
    assertSame(actualNodeB, actualParent.getFirstChild().getNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testProcessMethodWithSideEffectSubexpressionsAndChangedFlagIsSet() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    Node parent = new Node(Token.EXPR_STATEMENT, Lists.newArrayList());
    Node node = new Node(Token.BINARY_EXPRESSION, Lists.newArrayList(
        new Node(Token.IDENTIFIER, "a"),
        new Node(Token.PLUS, null),
        new Node(Token.IDENTIFIER, "b")
    ));
    parent.addChildToEnd(node);
    root.addChildToEnd(parent);

    Result result = removeConstantExpressions.process(null, root).getResult();
    result.changed = true;

    removeConstantExpressions.process(null, root);
  }
}

class NodeUtilMock {
  static boolean nodeTypeMayHaveSideEffects(Node node) {
    return true;
  }
}

class GatherSideEffectSubexpressionsCallback {
  private final Node parent;
  private final CopySideEffectSubexpressions copySideEffectSubexpressions;

  GatherSideEffectSubexpressionsCallback(Node parent,
                                         CopySideEffectSubexpressions copySideEffectSubexpressions) {
    this.parent = parent;
    this.copySideEffectSubexpressions = copySideEffectSubexpressions;
  }

  @Override
  public void visit(NodeTraversal traversal, Node node, Node parent) {
    if (node.getType() != Token.EXPR_RESULT) {
      return;
    }

    if (NodeUtilMock.nodeTypeMayHaveSideEffects(node.getFirstChild())) {
      copySideEffectSubexpressions.copySideEffectSubexpression(node, node.getFirstChild());
    }
  }
}

class CopySideEffectSubexpressions {
  private final List<Node> subexpressions;

  CopySideEffectSubexpressions(List<Node> subexpressions) {
    this.subexpressions = subexpressions;
  }

  void copySideEffectSubexpression(Node originalNode, Node nodeToCopy) {
    subexpressions.add(nodeToCopy);
  }
}
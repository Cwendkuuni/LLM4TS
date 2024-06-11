package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    Result result = new Result();
    AbstractCompiler compiler = new Compiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    Node root = new Node(Token.PROGRAM, null, Lists.newArrayList());
    Node expr1 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ADD, null, Lists.newArrayList(
            new Node(Token.NUMBER, null, "1"),
            new Node(Token.IDENT, null, "foo"),
            new Node(Token.ADD, null, Lists.newArrayList(
                new Node(Token.IDENT, null, "bar")
            ))
        ))
    ));
    Node expr2 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.IDENT, null, "baz")
    ));
    root.getChildren().add(expr1);
    root.getChildren().add(expr2);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    pass.process(null, root);
    cb.getResult().notifyCompiler(compiler);

    List<Node> expectedNodes = Lists.newArrayList(
        new Node(Token.IDENT, null, "foo"),
        new Node(Token.IDENT, null, "bar"),
        new Node(Token.EXPR_RESULT, null, Lists.newArrayList(new Node(Token.IDENT, null, "baz")))
    );

    NodeTraversal.traverse(null, root, new NodeAssertingVisitor(expectedNodes));

    assertNull(result.changed);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffects() {
    Result result = new Result();
    AbstractCompiler compiler = new Compiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    Node root = new Node(Token.PROGRAM, null, Lists.newArrayList());
    Node expr1 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ADD, null, Lists.newArrayList(
            new Node(Token.NUMBER, null, "1"),
            new Node(Token.IDENT, null, "foo"),
            new Node(Token.ADD, null, Lists.newArrayList(
                new Node(Token.IDENT, null, "bar")
            ))
        ))
    ));
    Node expr2 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "baz")
        )),
        new Node(Token.IDENT, null, "baz")
    ));
    root.getChildren().add(expr1);
    root.getChildren().add(expr2);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    pass.process(null, root);
    cb.getResult().notifyCompiler(compiler);

    List<Node> expectedNodes = Lists.newArrayList(
        new Node(Token.IDENT, null, "foo"),
        new Node(Token.IDENT, null, "bar"),
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "baz")
        )),
        new Node(Token.EXPR_RESULT, null, Lists.newArrayList())
    );

    NodeTraversal.traverse(null, root, new NodeAssertingVisitor(expectedNodes));

    assertNull(result.changed);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffectsAndNoConstants() {
    Result result = new Result();
    AbstractCompiler compiler = new Compiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    Node root = new Node(Token.PROGRAM, null, Lists.newArrayList());
    Node expr1 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "baz")
        )),
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        ))
    ));
    Node expr2 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        ))
    ));
    root.getChildren().add(expr1);
    root.getChildren().add(expr2);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    pass.process(null, root);
    cb.getResult().notifyCompiler(compiler);

    List<Node> expectedNodes = Lists.newArrayList(
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        )),
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        )),
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        ))
    );

    NodeTraversal.traverse(null, root, new NodeAssertingVisitor(expectedNodes));

    assertNull(result.changed);
  }

  @Test
  public void testRemoveConstantExpressionsWithNoConstantExpressions() {
    Result result = new Result();
    AbstractCompiler compiler = new Compiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    Node root = new Node(Token.PROGRAM, null, Lists.newArrayList());
    Node expr1 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        )),
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        ))
    ));
    Node expr2 = new Node(Token.EXPR_RESULT, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, Lists.newArrayList(
            new Node(Token.IDENT, null, "qux"),
            new Node(Token.IDENT, null, "qux")
        ))
    ));
    root.getChildren().add(expr1);
    root.getChildren().add(expr2);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    pass.process(null, root);
    cb.getResult().notifyCompiler(compiler);

    List<Node> expectedNodes = Lists.newArrayList(
        new Node(Token.PROGRAM, null, Lists.newArrayList(
            expr1,
            expr2
        ))
    );

    NodeTraversal.traverse(null, root, new NodeAssertingVisitor(expectedNodes));

    assertNull(result.changed);
  }

  private static class NodeAssertingVisitor extends NodeTraversal.AbstractVisitor {
    private final List<Node> expectedNodes;

    private NodeAssertingVisitor(List<Node> expectedNodes) {
      this.expectedNodes = expectedNodes;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node) {
      Node expectedNode = expectedNodes.remove(0);
      NodeUtil.assertNodeEquals(expectedNode, node);
      if (node.getNumChildren() != expectedNode.getNumChildren()) {
        throw new RuntimeException(
            "Expected node '" + expectedNode + "' to have the same number of children as actual node '" + node);
      }
      for (int i = 0; i < node.getNumChildren(); i++) {
        Node childExpected = expectedNodes.get(i);
        Node childActual = node.getChild(i);
        NodeAssertingVisitor childVisitor = new NodeAssertingVisitor(expectedNodes);
        NodeTraversal.traverse(traversal, childActual, childVisitor);
      }
    }
  }
}
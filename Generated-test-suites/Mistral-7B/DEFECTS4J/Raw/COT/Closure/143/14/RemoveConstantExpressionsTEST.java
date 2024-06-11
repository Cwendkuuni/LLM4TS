import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeToken.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.test_util.AbstractCompilerForTests;
import com.google.javascript.jscomp.test_util.TreeUtil;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setUp() {
    compiler = new AbstractCompilerForTests();
    nodeFactory = compiler.getFactory();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testProcess_noConstantExpression() {
    Node root = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(
            nodeFactory.createIdentifier("x"),
            nodeFactory.createNumberLiteral(1)
        ));

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    assertEquals(false, result.changed);
  }

  @Test
  public void testProcess_constantExpression() {
    Node root = nodeFactory.createExpressionStatement(
        nodeFactory.createExpression(
            nodeFactory.createBinaryOperator(
                nodeFactory.createIdentifier("x"),
                ADD,
                nodeFactory.createNumberLiteral(1),
                null
            )
        ));

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    Node expected = nodeFactory.createExpressionStatement(
        nodeFactory.createExpression(
            nodeFactory.createIdentifier("x")
        ));
    assertEquals(expected, TreeUtil.deepClone(root));
    assertEquals(true, result.changed);
  }

  @Test
  public void testProcess_multipleConstantExpressions() {
    Node root = nodeFactory.createExpressionStatement(
        nodeFactory.createExpression(
            nodeFactory.createBinaryOperator(
                nodeFactory.createBinaryOperator(
                    nodeFactory.createIdentifier("x"),
                    ADD,
                    nodeFactory.createNumberLiteral(1),
                    null
                ),
                ADD,
                nodeFactory.createNumberLiteral(2),
                null
            )
        ));

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    Node expected = nodeFactory.createExpressionStatement(
        nodeFactory.createExpression(
            nodeFactory.createIdentifier("x")
        ));
    assertEquals(expected, TreeUtil.deepClone(root.getFirstChild()));
    Node expected2 = nodeFactory.createExpressionStatement(
        nodeFactory.createNumberLiteral(2)
    );
    assertEquals(expected2, TreeUtil.deepClone(root.getLastChild()));
    assertEquals(true, result.changed);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_assignmentExpression() {
    Node root = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createBinaryOperator(
                    nodeFactory.createIdentifier("x"),
                    ADD,
                    nodeFactory.createNumberLiteral(1),
                    null
                )
            )));

    Result result = new Result();
    removeConstantExpressions.process(null, root);
  }

  @Test
  public void testProcess_complexExpression() {
    Node root = nodeFactory.createExpressionStatement(
        nodeFactory.createExpression(
            nodeFactory.createBinaryOperator(
                nodeFactory.createBinaryOperator(
                    nodeFactory.createIdentifier("x"),
                    ADD,
                    nodeFactory.createNumberLiteral(1),
                    null
                ),
                ADD,
                nodeFactory.createBinaryOperator(
                    nodeFactory.createIdentifier("y"),
                    ADD,
                    nodeFactory.createNumberLiteral(2),
                    null
                ),
                null
            )
        ));

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    Node expected = nodeFactory.createExpressionStatement(
        nodeFactory.createExpression(
            nodeFactory.createBinaryOperator(
                nodeFactory.createIdentifier("x"),
                ADD,
                nodeFactory.createIdentifier("y")
            )));
    assertEquals(expected, TreeUtil.deepClone(root));
    assertEquals(true, result.changed);
  }

  @Test
  public void testGetSideEffectNodes_noSideEffect() {
    Node node = nodeFactory.createExpression(
        nodeFactory.createNumberLiteral(1)
    );
    List<Node> sideEffectNodes =
        removeConstantExpressions.new RemoveConstantRValuesCallback().getSideEffectNodes(node);
    assertEquals(0, sideEffectNodes.size());
  }

  @Test
  public void testGetSideEffectNodes_sideEffectExpression() {
    Node node = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createNumberLiteral(1)
            )
        )
    );
    List<Node> sideEffectNodes =
        removeConstantExpressions.new RemoveConstantRValuesCallback().getSideEffectNodes(node);
    assertEquals(1, sideEffectNodes.size());
    assertEquals(node.getFirstChild(), sideEffectNodes.get(0));
  }

  @Test
  public void testGetSideEffectNodes_sideEffectAssignment() {
    Node node = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createAssign(
                    nodeFactory.createIdentifier("y"),
                    nodeFactory.createNumberLiteral(1)
                )
            )
        )
    );
    List<Node> sideEffectNodes =
        removeConstantExpressions.new RemoveConstantRValuesCallback().getSideEffectNodes(node);
    assertEquals(2, sideEffectNodes.size());
    assertEquals(node.getFirstChild().getFirstChild(), sideEffectNodes.get(0));
    assertEquals(node.getFirstChild().getLastChild(), sideEffectNodes.get(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTrySimplify_nonExpressionResultNode() {
    Node node = nodeFactory.createIdentifier("x");
    removeConstantExpressions.new RemoveConstantRValuesCallback().trySimplify(
        null, node);
  }

  @Test
  public void testTrySimplify_noSideEffectNodes() {
    Node node = nodeFactory.createExpression(
        nodeFactory.createNumberLiteral(1)
    );
    removeConstantExpressions.new RemoveConstantRValuesCallback().trySimplify(
        null, node.getParent(), node);
  }

  @Test
  public void testTrySimplify_sideEffectNodesPresent() {
    Node parent = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createNumberLiteral(1)
            )
        )
    );
    Node node = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("y"),
                nodeFactory.createNumberLiteral(2)
            )
        )
    );
    removeConstantExpressions.new RemoveConstantRValuesCallback().trySimplify(
        null, parent, node);
  }

  @Test
  public void testReplaceWith_noReplacements() {
    Node parent = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createNumberLiteral(1)
            )
        )
    );
    Node node = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("y"),
                nodeFactory.createNumberLiteral(2)
            )
        )
    );

    RemoveConstantRValuesCallback callback =
        removeConstantExpressions.new RemoveConstantRValuesCallback();
    List<Node> sideEffectNodes = Lists.newArrayList(node);
    callback.getSideEffectNodes(node);

    Node replacement = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(
            nodeFactory.createIdentifier("z"),
            nodeFactory.createNumberLiteral(3)
        )
    );
    callback.replaceWith(parent, node, sideEffectNodes);

    assertEquals(node, parent.getLastChild());
  }

  @Test
  public void testReplaceWith_singleReplacement() {
    Node parent = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createNumberLiteral(1)
            )
        )
    );
    Node node = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("y"),
                nodeFactory.createNumberLiteral(2)
            )
        )
    );

    RemoveConstantRValuesCallback callback =
        removeConstantExpressions.new RemoveConstantRValuesCallback();
    List<Node> sideEffectNodes = Lists.newArrayList(node);
    callback.getSideEffectNodes(node);

    Node replacement = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(
            nodeFactory.createIdentifier("z"),
            nodeFactory.createNumberLiteral(3)
        )
    );
    callback.replaceWith(parent, node, sideEffectNodes);

    assertEquals(replacement, parent.getLastChild());
  }

  @Test
  public void testReplaceWith_multipleReplacements() {
    Node parent = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("x"),
                nodeFactory.createNumberLiteral(1)
            )
        )
    );
    Node node = nodeFactory.createExpressionStatement(
        nodeFactory.createExpressionStatement(
            nodeFactory.createAssign(
                nodeFactory.createIdentifier("y"),
                nodeFactory.createNumberLiteral(2)
            )
        )
    );

    RemoveConstantRValuesCallback callback =
        removeConstantExpressions.new RemoveConstantRValuesCallback();
    List<Node> sideEffectNodesX = Lists.newArrayList(node);
    callback.getSideEffectNodes(node);

    Node replacementX = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(
            nodeFactory.createIdentifier("z"),
            nodeFactory.createNumberLiteral(3)
        )
    );
    callback.replaceWith(parent, node, sideEffectNodesX);

    Node sideEffectNodeY = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(
            nodeFactory.createIdentifier("w"),
            nodeFactory.createNumberLiteral(4)
        )
    );
    List<Node> sideEffectNodesY = Lists.newArrayList(sideEffectNodeY);
    callback.getSideEffectNodes(node.getLastChild());

    Node replacementY = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(
            nodeFactory.createIdentifier("v"),
            nodeFactory.createNumberLiteral(5)
        )
    );
    callback.replaceWith(parent, node.getLastChild(), sideEffectNodesY);

    assertEquals(replacementX, parent.getLastChild());
    assertEquals(replacementY, parent.getLastChild().getLastChild());
  }
}
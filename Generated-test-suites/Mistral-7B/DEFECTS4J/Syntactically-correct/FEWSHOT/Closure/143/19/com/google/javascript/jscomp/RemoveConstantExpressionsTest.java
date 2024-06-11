package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;

/**
 * Test cases for RemoveConstantExpressions class.
 */
public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compilerMock);

    Node rootNode = createNode("1 + foo() + bar()");

    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootNode, cb);
    pass.process(null, rootNode);
    cb.getResult().notifyCompiler(compilerMock);

    List<Node> expectedNodes = Lists.newArrayList(
        createNode("foo()"),
        createNode("bar()")
    );

    List<Node> actualNodes = getSideEffectNodes(rootNode);

    assertThat(result.changed).isTrue();
    assertThat(actualNodes).isEqualTo(expectedNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithSingleNode() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compilerMock);

    Node rootNode = createNode("1 + foo()");

    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootNode, cb);
    pass.process(null, rootNode);
    cb.getResult().notifyCompiler(compilerMock);

    List<Node> expectedNodes = Lists.newArrayList(createNode("foo()"));

    List<Node> actualNodes = getSideEffectNodes(rootNode);

    assertThat(result.changed).isTrue();
    assertThat(actualNodes).isEqualTo(expectedNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithNoSideEffects() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compilerMock);

    Node rootNode = createNode("1 + 2 + 3");

    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootNode, cb);
    pass.process(null, rootNode);
    cb.getResult().notifyCompiler(compilerMock);

    List<Node> expectedNodes = Collections.emptyList();

    List<Node> actualNodes = getSideEffectNodes(rootNode);

    assertThat(result.changed).isFalse();
    assertThat(actualNodes).isEqualTo(expectedNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithNullNode() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compilerMock);

    Node rootNode = null;

    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootNode, cb);
    pass.process(null, rootNode);
    cb.getResult().notifyCompiler(compilerMock);

    List<Node> expectedNodes = Collections.emptyList();

    List<Node> actualNodes = getSideEffectNodes(rootNode);

    assertNull(result.changed);
    assertThat(actualNodes).isEqualTo(expectedNodes);
  }

  private Node createNode(String nodeName) {
    return new Node(new Node(Token.SCRIPTLET, null),
        new Node(Token.EXPR_RESULT,
            new Node(Token.EXPR_CALL, null,
                new Node(Token.IDENT, nodeName),
                Collections.emptyList()
            )
        )
    );
  }

  private List<Node> getSideEffectNodes(Node node) {
    if (node == null) {
      return Collections.emptyList();
    }

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, node, cb);
    return cb.getResult().getSideEffectNodes();
  }
}
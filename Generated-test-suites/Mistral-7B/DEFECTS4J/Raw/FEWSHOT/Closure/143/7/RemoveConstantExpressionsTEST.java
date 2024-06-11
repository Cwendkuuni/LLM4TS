package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RemoveConstantExpressionsTest {
  private RemoveConstantExpressions pass;
  private AbstractCompiler compiler;
  private AbstractPostOrderCallback callback;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    pass = new RemoveConstantExpressions(compiler);
    callback = mock(AbstractPostOrderCallback.class);
  }

  @Test
  public void testSimpleCase() {
    Node node1 = new Node(Token.EXPR_RESULT, new Node(Token.PLUS, new Node(Token.NUMBER, 1), new Node(Token.IDENT, "foo")));
    Node node2 = new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "bar"));
    root = new Node(Token.EXPR_STATEMENT, node1, node2);

    RemoveConstantExpressions.RemoveConstantRValuesCallback cb = pass.new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    ArgumentCaptor<Node> parentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<List<Node>> replacementsCaptor = ArgumentCaptor.forClass(List.class);

    when(compiler.getAstChangeProxy()).thenReturn(new AstChangeProxy());

    cb.getResult().notifyCompiler(compiler);

    assertEquals(2, root.getChildCount());
    assertEquals(Token.EXPR_STATEMENT, root.getType());

    Node node1Parent = root.getChild(0).getParent();
    assertEquals(parentCaptor.capture(), node1Parent);

    assertEquals(Token.IDENT, node1Parent.getType());
    assertEquals("foo", node1Parent.getFirstChild().getFirstChild().getString());

    Node node2Parent = root.getChild(1).getParent();
    assertEquals(parentCaptor.capture(), node2Parent);

    assertEquals(Token.IDENT, node2Parent.getType());
    assertEquals("bar", node2Parent.getFirstChild().getString());

    List<Node> replacements = replacementsCaptor.getValue();

    assertEquals(2, replacements.size());

    Node replacementOne = replacements.get(0);
    assertEquals(Token.EXPR_RESULT, replacementOne.getType());
    assertEquals(node1Parent, replacementOne.getParent());

    Node replacementTwo = replacements.get(1);
    assertEquals(Token.EXPR_RESULT, replacementTwo.getType());
    assertEquals(node2Parent, replacementTwo.getParent());
  }

  @Test
  public void testNoSideEffects() {
    Node node = new Node(Token.EXPR_RESULT, new Node(Token.PLUS, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2)));
    root = new Node(Token.EXPR_STATEMENT, node);

    RemoveConstantExpressions.RemoveConstantRValuesCallback cb = pass.new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    assertEquals(1, root.getChildCount());
    assertEquals(Token.EXPR_STATEMENT, root.getType());
    assertEquals(node, root.getFirstChild());

    assertNull(cb.getResult().changed);
  }

  @Test
  public void testMultipleSideEffects() {
    Node node1 = new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "foo"));
    Node node2 = new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "bar"));
    Node node3 = new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "baz"));

    Node nodeStatement1 = new Node(Token.EXPR_STATEMENT, node1);
    Node nodeStatement2 = new Node(Token.EXPR_STATEMENT, node2);
    Node nodeStatement3 = new Node(Token.EXPR_STATEMENT, node3);

    root = new Node(Token.BLOCK, nodeStatement1, nodeStatement2, nodeStatement3);

    RemoveConstantExpressions.RemoveConstantRValuesCallback cb = pass.new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    assertEquals(3, root.getChildCount());
    assertEquals(Token.BLOCK, root.getType());

    Node firstChild = root.getFirstChild();
    assertEquals(Token.EXPR_STATEMENT, firstChild.getType());
    assertEquals(node1, firstChild.getFirstChild());

    Node secondChild = root.getChild(1);
    assertEquals(Token.EXPR_STATEMENT, secondChild.getType());
    assertEquals(node2, secondChild.getFirstChild());

    Node thirdChild = root.getChild(2);
    assertEquals(Token.EXPR_STATEMENT, thirdChild.getType());
    assertEquals(node3, thirdChild.getFirstChild());

    assertNull(cb.getResult().changed);
  }
}
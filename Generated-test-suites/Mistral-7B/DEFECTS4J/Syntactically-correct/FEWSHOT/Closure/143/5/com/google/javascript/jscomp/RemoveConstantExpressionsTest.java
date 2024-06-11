package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private Node root;
  private NodeTraversal traversal;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    root = new Node(Token.PROGRAM, null);
    traversal = NodeTraversal.forTree(root);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testRemoveConstantExpressionWithNoSideEffects() {
    Node constantExpression = new Node(Token.EXPR_RESULT, new Node(Token.NUMBER, 1));
    Node parent = new Node(Token.ASSIGN, null, constantExpression);
    root.addChild(parent);

    RemoveConstantRValuesCallback cb = removeConstantExpressions.new RemoveConstantRValuesCallback();
    traversal.traverse(null, root, cb);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    assertNull(compiler.callMethod("replaceWith", nodeCaptor.capture(parent), nodeCaptor.capture(constantExpression), null));
    Node replacement = nodeCaptor.getValue();
    assertThat(replacement.getFirstChild()).isInstanceOf(Token.class);
    assertThat(replacement.getFirstChild().getValue()).isEqualTo(NodeUtil.getIdentifier(""));
  }

  @Test
  public void testRemoveConstantExpressionWithSideEffects() {
    Node sideEffectExpression = new Node(Token.CALL, null, new Node(Token.IDENTIFIER, "foo"), Lists.newArrayList());
    Node parent = new Node(Token.ASSIGN, null, new Node(Token.EXPR_RESULT, sideEffectExpression));
    root.addChild(parent);

    RemoveConstantRValuesCallback cb = removeConstantExpressions.new RemoveConstantRValuesCallback();
    traversal.traverse(null, root, cb);

    ArgumentCaptor<Node> parentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<List<Node>> nodesCaptor = ArgumentCaptor.forClass(List.class);
    when(compiler.callMethod("replaceWith", parentCaptor.capture(parent), nodeCaptor.capture(sideEffectExpression), nodesCaptor.capture()))
        .thenReturn(null);
    Node replacement = nodeCaptor.getValue();
    List<Node> replacementNodes = nodesCaptor.getValue();

    Node replacementParent = parentCaptor.getValue();
    replacementParent.setFirstChild(replacement);
    for (Node replacementNode : replacementNodes) {
      replacementParent.addChild(replacementNode);
    }

    assertThat(parent.getFirstChild()).isNull();
    assertThat(replacementParent.getNumChildren()).isEqualTo(sideEffectExpression.getNumChildren() + 1);
  }

  @Test
  public void testRemoveConstantExpressionWithMultipleSideEffects() {
    Node sideEffectExpression1 = new Node(Token.ASSIGN, null, new Node(Token.IDENTIFIER, "foo"), new Node(Token.NUMBER, 1));
    Node sideEffectExpression2 = new Node(Token.ASSIGN, null, new Node(Token.IDENTIFIER, "bar"), new Node(Token.NUMBER, 2));
    Node parent = new Node(Token.ASSIGN, null, new Node(Token.EXPR_RESULT, sideEffectExpression1), new Node(Token.EXPR_RESULT, sideEffectExpression2));
    root.addChild(parent);

    RemoveConstantRValuesCallback cb = removeConstantExpressions.new RemoveConstantRValuesCallback();
    traversal.traverse(null, root, cb);

    ArgumentCaptor<Node> parentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> sideEffectExpression1Captor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> sideEffectExpression2Captor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<List<Node>> nodesCaptor = ArgumentCaptor.forClass(List.class);
    when(compiler.callMethod("replaceWith", parentCaptor.capture(parent), sideEffectExpression1Captor.capture(sideEffectExpression1), nodesCaptor.capture()))
        .thenReturn(null);
    when(compiler.callMethod("replaceWith", parentCaptor.capture(parent), sideEffectExpression2Captor.capture(sideEffectExpression2), nodesCaptor.capture()))
        .thenReturn(null);
    Node replacementParent = parentCaptor.getValue();
    Node replacementSideEffectExpression1 = sideEffectExpression1Captor.getValue();
    Node replacementSideEffectExpression2 = sideEffectExpression2Captor.getValue();
    List<Node> replacementNodes1 = nodesCaptor.getValue();
    List<Node> replacementNodes2 = nodesCaptor.getValue();

    replacementParent.setFirstChild(replacementSideEffectExpression1);
    for (Node replacementNode : replacementNodes1) {
      replacementParent.addChild(replacementNode);
    }

    replacementParent.addChild(replacementSideEffectExpression2);
    for (Node replacementNode : replacementNodes2) {
      replacementParent.addChild(replacementNode);
    }

    assertThat(parent.getNumChildren()).isEqualTo(2);
    for (int i = 0; i < parent.getNumChildren(); i++) {
      Node child = parent.getChild(i);
      if (child.getType() == Token.ASSIGN) {
        assertThat(child.getFirstChild().getType()).isEqualTo(Token.IDENTIFIER);
        assertThat(child.getFirstChild().getValue()).isEqualTo("foo");
      } else if (child.getType() == Token.ASSIGN) {
        assertThat(child.getFirstChild().getType()).isEqualTo(Token.IDENTIFIER);
        assertThat(child.getFirstChild().getValue()).isEqualTo("bar");
      }
    }
  }

  @Test
  public void testRemoveConstantExpressionWithMultipleSideEffectsInDifferentOrder() {
    Node sideEffectExpression1 = new Node(Token.ASSIGN, null, new Node(Token.IDENTIFIER, "foo"), new Node(Token.NUMBER, 1));
    Node sideEffectExpression2 = new Node(Token.ASSIGN, null, new Node(Token.IDENTIFIER, "bar"), new Node(Token.NUMBER, 2));
    Node parent = new Node(Token.ASSIGN, null, new Node(Token.EXPR_RESULT, sideEffectExpression2), new Node(Token.EXPR_RESULT, sideEffectExpression1));
    root.addChild(parent);

    RemoveConstantRValuesCallback cb = removeConstantExpressions.new RemoveConstantRValuesCallback();
    traversal.traverse(null, root, cb);

    ArgumentCaptor<Node> parentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> sideEffectExpression1Captor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> sideEffectExpression2Captor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<List<Node>> nodesCaptor = ArgumentCaptor.forClass(List.class);
    when(compiler.callMethod("replaceWith", parentCaptor.capture(parent), sideEffectExpression2Captor.capture(sideEffectExpression2), nodesCaptor.capture()))
        .thenReturn(null);
    when(compiler.callMethod("replaceWith", parentCaptor.capture(parent), sideEffectExpression1Captor.capture(sideEffectExpression1), nodesCaptor.capture()))
        .thenReturn(null);
    Node replacementParent = parentCaptor.getValue();
    Node replacementSideEffectExpression1 = sideEffectExpression1Captor.getValue();
    Node replacementSideEffectExpression2 = sideEffectExpression2Captor.getValue();
    List<Node> replacementNodes1 = nodesCaptor.getValue();
    List<Node> replacementNodes2 = nodesCaptor.getValue();

    replacementParent.setFirstChild(replacementSideEffectExpression1);
    for (Node replacementNode : replacementNodes1) {
      replacementParent.addChild(replacementNode);
    }

    replacementParent.addChild(replacementSideEffectExpression2);
    for (Node replacementNode : replacementNodes2) {
      replacementParent.addChild(replacementNode);
    }

    assertThat(parent.getNumChildren()).isEqualTo(2);
    for (int i = 0; i < parent.getNumChildren(); i++) {
      Node child = parent.getChild(i);
      if (child.getType() == Token.ASSIGN) {
        assertThat(child.getFirstChild().getType()).isEqualTo(Token.IDENTIFIER);
        assertThat(child.getFirstChild().getValue()).isEqualTo("bar");
      } else if (child.getType() == Token.ASSIGN) {
        assertThat(child.getFirstChild().getType()).isEqualTo(Token.IDENTIFIER);
        assertThat(child.getFirstChild().getValue()).isEqualTo("foo");
      }
    }
  }
}
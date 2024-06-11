package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantExpressions.RemoveConstantRValuesCallback;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    // Given a root node
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());

    // When the RemoveConstantExpressions pass is applied
    RemoveConstantExpressions pass = new RemoveConstantExpressions(null);
    pass.process(null, root);

    // Then the side effect nodes should be present in the tree
    RemoveConstantRValuesCallback cb = (RemoveConstantRValuesCallback) pass.getPassManager().getCallback(RemoveConstantRValuesCallback.class);
    List<Node> sideEffectNodes = cb.getResult().getSideEffectNodes();
    assertTrue(sideEffectNodes.size() > 0);

    // Test edge cases and exception handling

    // Test empty tree
    Node emptyTree = new Node(Token.PROGRAM, Lists.newArrayList());
    RemoveConstantExpressions emptyPass = new RemoveConstantExpressions(null);
    emptyPass.process(null, emptyTree);
    RemoveConstantRValuesCallback emptyCb = (RemoveConstantRValuesCallback) emptyPass.getPassManager().getCallback(RemoveConstantRValuesCallback.class);
    List<Node> emptySideEffectNodes = emptyCb.getResult().getSideEffectNodes();
    assertFalse(emptySideEffectNodes.isEmpty());

    // Test null parent node
    Node nullParentNode = new Node(Token.PROGRAM, Lists.newArrayList());
    Node nullChildNode = new Node(Token.EXPR_RESULT, Lists.newArrayList());
    NodeTraversal.traverse(null, nullParentNode, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        node.setParent(nullParentNode);
        node.setFirstChild(nullChildNode);
        nullParentNode.setFirstChild(node);
      }
    });
    Node nullRoot = new Node(Token.PROGRAM, Lists.newArrayList(nullParentNode));
    RemoveConstantExpressions nullPass = new RemoveConstantExpressions(null);
    nullPass.process(null, nullRoot);
    RemoveConstantRValuesCallback nullCb = (RemoveConstantRValuesCallback) nullPass.getPassManager().getCallback(RemoveConstantRValuesCallback.class);
    List<Node> nullSideEffectNodes = nullCb.getResult().getSideEffectNodes();
    assertFalse(nullSideEffectNodes.isEmpty());

    // Test null root node
    Node nullRoot2 = new Node(null, Lists.newArrayList());
    RemoveConstantExpressions nullPass2 = new RemoveConstantExpressions(null);
    nullPass2.process(null, nullRoot2);
    RemoveConstantRValuesCallback nullCb2 = (RemoveConstantRValuesCallback) nullPass2.getPassManager().getCallback(RemoveConstantRValuesCallback.class);
    List<Node> nullSideEffectNodes2 = nullCb2.getResult().getSideEffectNodes();
    assertFalse(nullSideEffectNodes2.isEmpty());
  }
}
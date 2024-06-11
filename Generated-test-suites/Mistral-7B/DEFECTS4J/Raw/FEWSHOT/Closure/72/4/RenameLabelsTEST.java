package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    NodeTraversal nodeTraversal = NodeTraversal.create(root, null);
    renameLabels.process(null, root);

    NodeTraversal spyNodeTraversal = spy(nodeTraversal);
    ProcessLabels processLabels = new ProcessLabels();
    when(spyNodeTraversal.enterScope(any())).then(invocation -> processLabels.enterScope(invocation.getArgument(0)));
    when(spyNodeTraversal.exitScope(any())).then(invocation -> processLabels.exitScope(invocation.getArgument(0)));

    List<Node> originalNodes = Lists.newArrayList(
        NodeFactory.createLabel("label1"),
        NodeFactory.createLabel("label2"),
        NodeFactory.createLabel("label3"),
        NodeFactory.createLabel("label4"),
        NodeFactory.createLabel("label5"),
        NodeFactory.createLabel("label6"),
        NodeFactory.createBreak(),
        NodeFactory.createContinue(),
        NodeFactory.createLabel("label7"),
        NodeFactory.createLabel("label8"),
        NodeFactory.createLabel("label9")
    );

    for (Node originalNode : originalNodes) {
      Node node = NodeUtil.cloneTree(originalNode);
      Node parentNode = NodeFactory.createBlock();
      parentNode.addChild(node);
      NodeTraversal.traverse(spyNodeTraversal, parentNode);
      Node renamedNode = node;
      if (node.getType() == Token.LABEL) {
        renamedNode = node.getFirstChild();
      }
      assertEquals("a", renamedNode.getString());
    }
  }

  @Test
  public void testRemoveUnusedLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    Node root = NodeFactory.createProgram();
    NodeTraversal nodeTraversal = NodeTraversal.create(root, null);
    renameLabels.process(null, root);

    NodeTraversal spyNodeTraversal = spy(nodeTraversal);
    ProcessLabels processLabels = new ProcessLabels();
    when(spyNodeTraversal.enterScope(any())).then(invocation -> processLabels.enterScope(invocation.getArgument(0)));
    when(spyNodeTraversal.exitScope(any())).then(invocation -> processLabels.exitScope(invocation.getArgument(0)));

    List<Node> originalNodes = Lists.newArrayList(
        NodeFactory.createLabel("label1"),
        NodeFactory.createLabel("label2"),
        NodeFactory.createLabel("label3"),
        NodeFactory.createLabel("label4"),
        NodeFactory.createLabel("label5"),
        NodeFactory.createLabel("label6"),
        NodeFactory.createLabel("label7"),
        NodeFactory.createLabel("label8"),
        NodeFactory.createLabel("label9")
    );

    Map<String, LabelInfo> renameMap = Maps.newHashMap();
    for (Node originalNode : originalNodes) {
      Node node = NodeUtil.cloneTree(originalNode);
      Node parentNode = NodeFactory.createBlock();
      parentNode.addChild(node);

      LabelInfo labelInfo = new LabelInfo(1);
      renameMap.put("label" + labelInfo.id, labelInfo);
      when(spyNodeTraversal.getScope(any())).thenReturn(renameMap);

      NodeTraversal.traverse(spyNodeTraversal, parentNode);

      Node renamedNode = node;
      if (node.getType() == Token.LABEL) {
        renamedNode = node.getFirstChild();
      }
      assertEquals("a", renamedNode.getString());

      // Unreferenced label should be removed
      Node removedNode = node;
      if (node.getType() != Token.LABEL) {
        removedNode = node.getLastChild();
      }
      assertNull(removedNode);
    }

    // Referenced label should not be removed
    Node referencedNode = NodeFactory.createLabel("label1");
    Node parentNode = NodeFactory.createBlock();
    parentNode.addChild(referencedNode);
    Node breakNode = NodeFactory.createBreak();
    breakNode.setParentNode(parentNode);
    breakNode.setFirstChild(NodeFactory.createLabel("label1"));
    parentNode.addChild(breakNode);
    NodeTraversal.traverse(spyNodeTraversal, parentNode);
    Node renamedReferencedNode = referencedNode;
    if (referencedNode.getType() == Token.LABEL) {
      renamedReferencedNode = referencedNode.getFirstChild();
    }
    assertEquals("a", renamedReferencedNode.getString());
    Node referencedNodeInTree = parentNode.getFirstChild();
    assertFalse(referencedNodeInTree.isDead());
  }

  @Test
  public void testRenameLabelsWithNestedScopes() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    NodeTraversal nodeTraversal = NodeTraversal.create(root, null);
    renameLabels.process(null, root);

    NodeTraversal spyNodeTraversal = spy(nodeTraversal);
    ProcessLabels processLabels = new ProcessLabels();
    when(spyNodeTraversal.enterScope(any())).then(invocation -> processLabels.enterScope(invocation.getArgument(0)));
    when(spyNodeTraversal.exitScope(any())).then(invocation -> processLabels.exitScope(invocation.getArgument(0)));

    Multimap<String, LabelInfo> renameMap = ArrayListMultimap.create();

    Node outerScope = NodeFactory.createBlock();
    outerScope.setParentNode(root);
    Node innerScope = NodeFactory.createBlock();
    innerScope.setParentNode(outerScope);

    List<Node> originalNodes = Lists.newArrayList(
        NodeFactory.createLabel("label1"),
        NodeFactory.createLabel("label2"),
        NodeFactory.createLabel("label3"),
        NodeFactory.createLabel("label4"),
        NodeFactory.createLabel("label5"),
        NodeFactory.createLabel("label6"),
        NodeFactory.createLabel("label7"),
        NodeFactory.createLabel("label8"),
        NodeFactory.createLabel("label9")
    );

    for (int i = 0; i < originalNodes.size(); i++) {
      Node originalNode = originalNodes.get(i);
      Node node = NodeUtil.cloneTree(originalNode);
      Node parentNode = i % 2 == 0 ? outerScope : innerScope;
      parentNode.addChild(node);

      LabelInfo labelInfo = new LabelInfo(1 + (i / 2) * 2);
      renameMap.put("label" + labelInfo.id, labelInfo);
      when(spyNodeTraversal.getScope(any())).thenReturn(renameMap);

      NodeTraversal.traverse(spyNodeTraversal, parentNode);

      Node renamedNode = node;
      if (node.getType() == Token.LABEL) {
        renamedNode = node.getFirstChild();
      }
      assertEquals("a" + (i / 2), renamedNode.getString());

      // Unreferenced label should be removed
      Node removedNode = node;
      if (node.getType() != Token.LABEL) {
        removedNode = node.getLastChild();
      }
      assertNull(removedNode);
    }
  }

  private static class LabelInfo {
    int id;

    LabelInfo(int id) {
      this.id = id;
    }
  }
}